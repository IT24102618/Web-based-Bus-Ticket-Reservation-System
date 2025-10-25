package com.example.busreservation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Route Model - Represents bus routes between locations
 * Used by: Operation Manager (primary), Passengers (view routes), Ticketing (view route info)
 * Contains: Route information with starting and destination points
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Table(name = "Route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Integer routeId; // Unique route identifier

    @Column(name = "district", nullable = false, length = 50)
    private String district; // District name

    @Column(name = "from_stand", nullable = false, length = 100)
    private String fromStand; // Starting bus stand/location

    @Column(name = "to_stand", nullable = false, length = 100)
    private String toStand; // Destination bus stand/location

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getFromStand() {
        return fromStand;
    }

    public void setFromStand(String fromStand) {
        this.fromStand = fromStand;
    }

    public String getToStand() {
        return toStand;
    }

    public void setToStand(String toStand) {
        this.toStand = toStand;
    }
}



