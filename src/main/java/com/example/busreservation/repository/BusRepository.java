package com.example.busreservation.repository;

import com.example.busreservation.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus, Integer> {
    Optional<Bus> findByRegistrationNo(String registrationNo);
}

