package com.acme.ex2.model.entity;

import java.util.List;

import javax.persistence.*;

import com.acme.ex2.model.component.Comment;
import com.acme.ex2.model.entity.reference.Category;

@Entity
public class Book extends AbstractPersistentEntity<Integer> {

    private String title;

    @ManyToOne
    private Author author;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "book")
    private List<Reservation> reservations;

    @ElementCollection
    private List<Comment> comments;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author=" + author +
                ", category=" + category +
                ", reservations=" + reservations +
                ", comments=" + comments +
                '}';
    }
}
