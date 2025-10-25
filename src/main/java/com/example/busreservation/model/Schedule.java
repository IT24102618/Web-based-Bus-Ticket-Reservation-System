package com.example.busreservation.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Schedule Model - Represents bus schedules with timing and fare information
 * Used by: Operation Manager (create schedules), Finance (manage fares), Passengers (view schedules)
 * Contains: Schedule details, bus and route references, timing, and fare information
 */
@Entity
@Table(name = "Schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Integer scheduleId; // Unique schedule identifier

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus; // Reference to the bus assigned to this schedule

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route; // Reference to the route for this schedule

    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate; // Date of travel

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime; // Departure time

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime; // Arrival time

    @Column(name = "fare", nullable = true, precision = 10, scale = 2)
    private BigDecimal fare; // Ticket price for this schedule

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getFare() {
        return fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }
}



