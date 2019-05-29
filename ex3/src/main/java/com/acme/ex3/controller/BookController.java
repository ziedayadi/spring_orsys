package com.acme.ex3.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;

import com.acme.ex3.repository.BookRepository;
import com.acme.ex3.repository.MemberRepository;
import com.acme.ex3.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.acme.ex3.exception.ApplicationException;
import com.acme.ex3.model.entity.Book;
import com.acme.ex3.model.entity.Member;
import com.acme.ex3.model.entity.Reservation;

@Controller
public class BookController {

	@Autowired
	private BookRepository repository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@GetMapping("books")
	public String list(Map<String, Object> model) {
		model.put("probe", new Book());
		return "books/list";
	}

	@GetMapping(path = "books", params = "title")
	public String list(@Valid @ModelAttribute("probe") Book probe, Map<String, Object> model) {
		// TODO : Remplacer null ci dessous par un appel au repository pour obtenir les livres correspondant à l'exemple reçu en argument.
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		List<@Valid Book> results = repository.findAll(Example.of(probe, matcher));
		
		model.put("results", results);
		return "books/list";
	}

	@GetMapping("books/{id}")
	public String book(@PathVariable int id, Map<String, Object> model) {
		// TODO : Remplacer null ci dessous par un appel au repository pour obtenir le Book dont l'id est id
		Optional<Book> maybeBook = this.repository.findById(id);
		maybeBook.ifPresent(b -> model.put("entity", b));
		
		model.putIfAbsent("command", new ReservationCommand());
		return "books/detail";
	}

	public static class ReservationCommand {
		public int bookId;

		public String username;

		@FutureOrPresent @DateTimeFormat(pattern = "yyyy-MM-dd")
		public LocalDate pickupDate;

		@Future @DateTimeFormat(pattern = "yyyy-MM-dd")
		public LocalDate returnDate;
	}

	
	@PostMapping("/reservations")
	@PreAuthorize("hasAuthority('borrow-books')")
	@Transactional
	public String borrow(Authentication auth, @Valid ReservationCommand command, BindingResult br, RedirectAttributes ra) {
		if (br.hasErrors()) {
			ra.addFlashAttribute("command", command).addFlashAttribute(BindingResult.MODEL_KEY_PREFIX+"command", br);
			return "redirect:/books/" + command.bookId;
		}
		// TODO : Remplacer null ci dessous par un appel au repository pour obtenir le livre dont l'id est command.bookId
		// S'il n'y en pas : new ApplicationException("reservation-book.unavailable", true);
		// Suggestion : utiliser la méthode orElseThrow de la classe Optional
		Book book = this.repository.findById(command.bookId)
				.orElseThrow(()->new ApplicationException("reservation-book.unavailable", true));

		command.username = auth.getName();

		Predicate<Reservation> conflict = r -> r.getPickupDate().isBefore(command.returnDate) && r.getReturnDate().isAfter(command.pickupDate);
		
		// TODO : Remplacer null ci dessous par un appel au repository pour obtenir le
		// Member dont le username est command.username 
		// S'il n'y en pas : new ApplicationException("reservation-member.not.found", true) 
		// Suggestion : utiliser la méthode orElseThrow de la classe Optional
		
		Member member = memberRepository.findByAccountUsername(command.username)
				.orElseThrow(()->new ApplicationException("reservation-member.not.found", true));

		if (member.getReservations().stream().anyMatch(conflict)) {
			throw new ApplicationException("reservation-member.quota.exceeded", true);
		}



		if (book.getReservations().stream().anyMatch(conflict)) {
			throw new ApplicationException("reservation-book.unavailable", true);
		}

		Reservation reservation = new Reservation();
		reservation.setMember(member);
		reservation.setBook(book);
		reservation.setPickupDate(command.pickupDate);
		reservation.setReturnDate(command.returnDate);

		
		// TODO : sauvegarde de la réservation
		this.reservationRepository.save(reservation);

		return "redirect:/books";
	}
}
