package com.acme.ex1.repository.impl;

import com.acme.ex1.model.Movie;
import com.acme.ex1.repository.MovieRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public class WarnerMovieRepositoryImpl implements MovieRepository {
    private final List<Movie> someMovies = List.of(
            new Movie("Fellowship of the ring", 2001),
            new Movie("The two towers", 2003),
            new Movie("Return of the king", 2005));
    @Override
    public Stream<Movie> findByTitle(String title) {
        return this.someMovies.stream().filter(m -> m.getTitle().contains(title));
        /*
        return this.someMovies.stream().filter(new Predicate<Movie>() {
            @Override
            public boolean test(Movie movie) {
                return movie.getTitle().contains(title);
            }
        });*/
    }
}
