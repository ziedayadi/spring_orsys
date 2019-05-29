package com.acme.ex2.repository;

import com.acme.ex2.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {

    @Override
    @RestResource(exported = false)
    void delete(Reservation entity);


}
