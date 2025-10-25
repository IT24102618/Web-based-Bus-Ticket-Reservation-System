package com.example.busreservation.service;

import com.example.busreservation.model.SeatBalance;
import com.example.busreservation.model.Bus;
import com.example.busreservation.model.Schedule;
import com.example.busreservation.model.Booking;
import com.example.busreservation.repository.SeatBalanceRepository;
import com.example.busreservation.repository.BusRepository;
import com.example.busreservation.repository.ScheduleRepository;
import com.example.busreservation.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SeatBalanceService {

    @Autowired
    private SeatBalanceRepository seatBalanceRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public Optional<SeatBalance> getSeatBalanceByScheduleId(Integer scheduleId) {
        return seatBalanceRepository.findByScheduleId(scheduleId);
    }

    public Integer getAvailableTickets(Integer scheduleId) {
        Optional<SeatBalance> seatBalance = seatBalanceRepository.findByScheduleId(scheduleId);
        if (seatBalance.isPresent()) {
            return seatBalance.get().getAvailableTickets();
        }
        
        // If no seat balance record exists, return full bus capacity
        Optional<Schedule> scheduleOpt = scheduleRepository.findById(scheduleId);
        if (scheduleOpt.isPresent()) {
            return scheduleOpt.get().getBus().getNoOfSeats();
        }
        
        return 0;
    }

    @Transactional
    public SeatBalance createOrUpdateSeatBalance(Integer scheduleId) {
        Optional<Schedule> scheduleOpt = scheduleRepository.findById(scheduleId);
        if (scheduleOpt.isEmpty()) {
            throw new RuntimeException("Schedule not found with ID: " + scheduleId);
        }

        Schedule schedule = scheduleOpt.get();
        Bus bus = schedule.getBus();
        Integer totalSeats = bus.getNoOfSeats();

        // Calculate booked seats
        List<Booking> bookings = bookingRepository.findByScheduleScheduleId(scheduleId);
        Integer bookedSeats = bookings.stream()
            .mapToInt(Booking::getBookedTickets)
            .sum();

        Integer availableTickets = Math.max(0, totalSeats - bookedSeats);

        // Check if record exists
        Optional<SeatBalance> existingRecord = seatBalanceRepository.findByScheduleId(scheduleId);
        
        if (existingRecord.isPresent()) {
            // Update existing record
            SeatBalance record = existingRecord.get();
            record.setAvailableTickets(availableTickets);
            return seatBalanceRepository.save(record);
        } else {
            // Create new record
            SeatBalance newRecord = new SeatBalance(bus, schedule, availableTickets);
            return seatBalanceRepository.save(newRecord);
        }
    }

    @Transactional
    public void updateSeatBalanceForBooking(Integer scheduleId, Integer bookedTickets) {
        Optional<SeatBalance> seatBalanceOpt = seatBalanceRepository.findByScheduleId(scheduleId);
        
        if (seatBalanceOpt.isPresent()) {
            SeatBalance seatBalance = seatBalanceOpt.get();
            seatBalance.setAvailableTickets(Math.max(0, seatBalance.getAvailableTickets() - bookedTickets));
            seatBalanceRepository.save(seatBalance);
        } else {
            // Create new record if it doesn't exist
            createOrUpdateSeatBalance(scheduleId);
        }
    }

    @Transactional
    public void updateSeatBalanceForCancellation(Integer scheduleId, Integer cancelledTickets) {
        Optional<SeatBalance> seatBalanceOpt = seatBalanceRepository.findByScheduleId(scheduleId);
        
        if (seatBalanceOpt.isPresent()) {
            SeatBalance seatBalance = seatBalanceOpt.get();
            seatBalance.setAvailableTickets(seatBalance.getAvailableTickets() + cancelledTickets);
            seatBalanceRepository.save(seatBalance);
        }
    }

    @Transactional
    public void deleteSeatBalance(Integer scheduleId) {
        seatBalanceRepository.deleteByScheduleScheduleId(scheduleId);
    }

    public List<SeatBalance> getAllSeatBalances() {
        return seatBalanceRepository.findAll();
    }

    @Transactional
    public void refreshAllSeatBalances() {
        List<Schedule> allSchedules = scheduleRepository.findAll();
        for (Schedule schedule : allSchedules) {
            createOrUpdateSeatBalance(schedule.getScheduleId());
        }
    }
}
