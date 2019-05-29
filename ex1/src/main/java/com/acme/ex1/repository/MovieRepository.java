package com.acme.ex1.repository;

import com.acme.ex1.model.Movie;

import java.util.stream.Stream;

public interface MovieRepository {

    Stream<Movie> findByTitle(String title);
}
