package com.chikilev.planesale.Repository;

import com.chikilev.planesale.Entity.ReservationEntity;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepo extends CrudRepository<ReservationEntity, Integer> {
    Iterable<ReservationEntity> findAllByUser_Username(String user_username);

    void deleteAllByUser_Username(String user_username);

    void deleteByUser_UsernameAndFlightCode(String extractUsername, String flightCode);
}
