package com.acme.ex2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface SoftDeleteJpaRepository<T, ID> extends JpaRepository<T, ID> {

    @Override
    @Query("select e from #{entityName} where e.deleted is true")
    List<T> findAll();
}
