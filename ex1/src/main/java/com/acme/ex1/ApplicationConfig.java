package com.acme.ex1;

import com.acme.ex1.repository.MovieRepository;
import com.acme.ex1.repository.impl.FoxMovieRepositoryImpl;
import com.acme.ex1.repository.impl.WarnerMovieRepositoryImpl;
import com.acme.ex1.service.MovieService;
import com.acme.ex1.service.impl.MovieServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class ApplicationConfig {

    @Bean(name="service1")
    public MovieService service1(FoxMovieRepositoryImpl repository){
        return new MovieServiceImpl(repository);
    }
    @Bean
    public MovieService service2(WarnerMovieRepositoryImpl repository){
        return new MovieServiceImpl(repository);
    }

    public static void main(String[] args) {
        try(var ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class)){
            System.out.println(ctx.getBeansOfType(MovieRepository.class));
            System.out.println(ctx.getBeansOfType(MovieService.class));
        }
    }
}
