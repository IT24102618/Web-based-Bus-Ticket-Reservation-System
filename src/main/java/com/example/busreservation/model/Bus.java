package com.example.busreservation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Bus Model - Represents bus fleet information
 * Used by: Operation Manager (primary), Ticketing (view bus info)
 * Contains: Bus details like registration number, seat capacity, and bus type
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Table(name = "Bus")
public class Bus {

    public Bus() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_id")
    private Integer busId; // Unique bus identifier

    @Column(name = "registration_no", nullable = false, unique = true, length = 50)
    private String registrationNo; // Bus registration/license plate number

    @Column(name = "no_of_seats", nullable = false)
    private Integer noOfSeats; // Total number of seats in the bus

    @Column(name = "type", nullable = false, length = 50)
    private String type; // Bus type (e.g., AC, Non-AC, Luxury)

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public Integer getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(Integer noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}



