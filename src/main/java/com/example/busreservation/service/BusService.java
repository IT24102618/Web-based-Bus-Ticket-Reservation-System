package com.example.busreservation.service;

import com.example.busreservation.model.Bus;
import com.example.busreservation.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;

    public List<Bus> listAllBuses() {
        return busRepository.findAll();
    }

    public Bus createBus(Bus bus) {
        return busRepository.save(bus);
    }

    public Optional<Bus> updateBus(Integer id, Bus updates) {
        return busRepository.findById(id).map(existing -> {
            if (updates.getRegistrationNo() != null) existing.setRegistrationNo(updates.getRegistrationNo());
            if (updates.getNoOfSeats() != null) existing.setNoOfSeats(updates.getNoOfSeats());
            if (updates.getType() != null) existing.setType(updates.getType());
            return busRepository.save(existing);
        });
    }

    public boolean deleteBus(Integer id) {
        return busRepository.findById(id).map(bus -> {
            busRepository.delete(bus);
            return true;
        }).orElse(false);
    }

    public Optional<Bus> findById(Integer id) {
        return busRepository.findById(id);
    }
}

