package com.acme.ex1.service.impl;

import com.acme.ex1.ApplicationConfig;
import com.acme.ex1.model.Movie;
import com.acme.ex1.repository.MovieRepository;
import com.acme.ex1.service.MovieService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;


public class MovieServiceImpl implements MovieService {

    // j'ai besoin de FoxMovieRepository : NON
    // j'ai besoin de WarnerMovieRepository : NON
    // j'ai besoin d'une MovieRepository
    private final MovieRepository repository;

    public MovieServiceImpl(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public Stream<Movie> find(String title) {
        return this.repository.findByTitle(title);
    }
}
