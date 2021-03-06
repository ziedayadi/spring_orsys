package com.acme.ex1.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.Closeable;
import java.io.IOException;

import com.acme.ex1.ApplicationConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import com.acme.ex1.service.MovieService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfig.class)
class MovieServiceImplTest {

    @Autowired @Qualifier("service1")
	private MovieServiceImpl service;

    @Test
    void testFindMoviesWithResults() {
        service.find("e").forEach(m -> {
            System.out.println(m.getTitle());
            assertTrue(m.getTitle().contains("e"));
        });
    }

    @Test
    void testFindMoviesWithNoResult() {
        assertNotNull(service.find("_*_*_*_*_*_*"));
    }
}
