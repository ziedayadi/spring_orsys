package com.acme.ex2.projection;


import com.acme.ex2.model.entity.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "book-projection", types = Book.class)
public interface  BookProjection {



    @Value("#{target.id}")
     Integer getId();

    @Value("#{target.title}")
     String getTitle();

    @Value("#{target.author.firstname} #{target.author.lastname}")
     String getAuthorFullName();


    @Value("#{target.category.name}")
     String getCategory();

}
