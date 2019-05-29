package com.acme.ex1.repository.impl;

import com.acme.ex1.model.Movie;
import com.acme.ex1.repository.MovieRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Repository
public class FoxMovieRepositoryImpl implements MovieRepository {
    private final List<Movie> someMovies = List.of(
            new Movie("A new hope", 1977),
            new Movie("Empire strikes back", 1980),
            new Movie("Return of the jedi", 1983));
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
