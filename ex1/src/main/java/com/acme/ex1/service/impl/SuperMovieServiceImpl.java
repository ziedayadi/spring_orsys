package com.acme.ex1.service.impl;

import com.acme.ex1.model.Movie;
import com.acme.ex1.repository.MovieRepository;
import com.acme.ex1.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Stream;

@Service
public class SuperMovieServiceImpl implements MovieService {
    private final Set<MovieRepository> repositories;

    public SuperMovieServiceImpl(Set<MovieRepository> repositories) {
        this.repositories = repositories;
    }

    @Override
    public Stream<Movie> find(String title) {
        return this.repositories.parallelStream().flatMap(r -> r.findByTitle(title));
    }
}
