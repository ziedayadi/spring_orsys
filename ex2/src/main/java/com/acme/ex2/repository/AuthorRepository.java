package com.acme.ex2.repository;

import com.acme.ex2.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface AuthorRepository extends JpaRepository<Author,Integer> {

    @RestResource(exported = false)
    @Override
    void delete(Author entity);
}
