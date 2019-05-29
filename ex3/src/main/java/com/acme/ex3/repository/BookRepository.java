package com.acme.ex3.repository;

import com.acme.ex3.model.entity.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByTitleIgnoreCaseContaining(@Param("title") String title);

}