package com.example.busreservation.service;

import com.example.busreservation.model.Route;
import com.example.busreservation.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    public List<Route> listAllRoutes() {
        return routeRepository.findAll();
    }

    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    public Optional<Route> updateRoute(Integer id, Route updates) {
        return routeRepository.findById(id).map(existing -> {
            if (updates.getDistrict() != null) existing.setDistrict(updates.getDistrict());
            if (updates.getFromStand() != null) existing.setFromStand(updates.getFromStand());
            if (updates.getToStand() != null) existing.setToStand(updates.getToStand());
            return routeRepository.save(existing);
        });
    }

    public boolean deleteRoute(Integer id) {
        return routeRepository.findById(id).map(route -> {
            routeRepository.delete(route);
            return true;
        }).orElse(false);
    }

    public Optional<Route> findById(Integer id) {
        return routeRepository.findById(id);
    }

    public List<Route> findByDistrict(String district) {
        return routeRepository.findByDistrict(district);
    }
}


