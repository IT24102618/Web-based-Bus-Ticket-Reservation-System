package com.example.busreservation.service;

import com.example.busreservation.model.Bus;
import com.example.busreservation.model.Route;
import com.example.busreservation.model.Schedule;
import com.example.busreservation.repository.BusRepository;
import com.example.busreservation.repository.RouteRepository;
import com.example.busreservation.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private BusRepository busRepository;
    @Autowired
    private RouteRepository routeRepository;

    public List<Schedule> listAll() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findByRouteAndDate(String from, String to, String date) {
        try {
            java.time.LocalDate localDate = java.time.LocalDate.parse(date);
            return scheduleRepository.findByRouteAndDate(from, to, localDate);
        } catch (Exception e) {
            System.out.println("Error parsing date: " + date + ", Error: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    public Optional<Schedule> findById(Integer id) {
        return scheduleRepository.findById(id);
    }

    public Optional<Schedule> create(Integer busId, Integer routeId, Schedule payload) {
        Optional<Bus> bus = busRepository.findById(busId);
        Optional<Route> route = routeRepository.findById(routeId);
        if (bus.isEmpty() || route.isEmpty()) return Optional.empty();
        payload.setBus(bus.get());
        payload.setRoute(route.get());
        return Optional.of(scheduleRepository.save(payload));
    }

    public Optional<Schedule> update(Integer id, Integer busId, Integer routeId, Schedule updates) {
        return scheduleRepository.findById(id).map(existing -> {
            if (busId != null) busRepository.findById(busId).ifPresent(existing::setBus);
            if (routeId != null) routeRepository.findById(routeId).ifPresent(existing::setRoute);
            if (updates.getTravelDate() != null) existing.setTravelDate(updates.getTravelDate());
            if (updates.getStartTime() != null) existing.setStartTime(updates.getStartTime());
            if (updates.getEndTime() != null) existing.setEndTime(updates.getEndTime());
            if (updates.getFare() != null) existing.setFare(updates.getFare());
            return scheduleRepository.save(existing);
        });
    }

    public boolean delete(Integer id) {
        return scheduleRepository.findById(id).map(s -> {
            scheduleRepository.delete(s);
            return true;
        }).orElse(false);
    }

    public Optional<Schedule> updateFare(Integer id, BigDecimal fare) {
        return scheduleRepository.findById(id).map(existing -> {
            existing.setFare(fare);
            return scheduleRepository.save(existing);
        });
    }
}


