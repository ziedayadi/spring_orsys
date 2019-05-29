package com.acme.ex2.model.component;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.acme.ex2.model.entity.Book;
import com.acme.ex2.model.entity.Member;

@Embeddable
public class Comment {

	@ManyToOne
	private Member member;

	@ManyToOne
	private Book book;


	private String text;

	private Instant date;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}
}
