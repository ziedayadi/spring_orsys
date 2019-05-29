package com.acme.ex2.repository;

import com.acme.ex2.model.entity.Book;
import com.acme.ex2.projection.BookProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;


@RepositoryRestResource(excerptProjection = BookProjection.class)
public interface BookRepository extends JpaRepository<Book,Integer> {

    @RestResource(path="byTitle")
    Page<Book> findByTitleContaining(@Param("title") String title, Pageable pageable);

    @RestResource(exported = false)
    @Override
    void delete(Book entity);
}
