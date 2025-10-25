package com.example.busreservation.repository;

import com.example.busreservation.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    
    @Query("SELECT s FROM Schedule s JOIN s.route r WHERE r.fromStand = :from AND r.toStand = :to AND s.travelDate = :date")
    List<Schedule> findByRouteAndDate(@Param("from") String from, @Param("to") String to, @Param("date") java.time.LocalDate date);
}


