package com.acme.ex2.repository;

import com.acme.ex2.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {


    Optional<Member> findByAccountUsername(String st);

    @RestResource(exported = false)
    @Override
    void delete(Member entity);

}
