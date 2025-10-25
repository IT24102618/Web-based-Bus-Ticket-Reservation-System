package com.example.busreservation.controller;

import com.example.busreservation.model.Schedule;
import com.example.busreservation.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ScheduleController - Handles schedule and fare management API endpoints
 * Used by: Operation Manager (create schedules), Finance (manage fares), Passengers (view schedules)
 * Functions: Create, read, update, delete schedules, manage fare pricing
 */
@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("")
    public ResponseEntity<List<Schedule>> list() {
        List<Schedule> allSchedules = scheduleService.listAll();
        System.out.println("Total schedules in database: " + allSchedules.size());
        return ResponseEntity.ok(allSchedules);
    }

    @GetMapping(params = {"from", "to", "date"})
    public ResponseEntity<List<Schedule>> searchSchedules(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String date) {
        System.out.println("Searching schedules for: from=" + from + ", to=" + to + ", date=" + date);
        List<Schedule> schedules = scheduleService.findByRouteAndDate(from, to, date);
        System.out.println("Found " + schedules.size() + " schedules");
        return ResponseEntity.ok(schedules);
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Map<String, Object> req) {
        try {
            Integer busId = (Integer) req.get("busId");
            Integer routeId = (Integer) req.get("routeId");
            Schedule s = new Schedule();
            if (req.get("travelDate") != null) s.setTravelDate(java.time.LocalDate.parse((String) req.get("travelDate")));
            if (req.get("startTime") != null) s.setStartTime(java.time.LocalTime.parse((String) req.get("startTime")));
            if (req.get("endTime") != null) s.setEndTime(java.time.LocalTime.parse((String) req.get("endTime")));
            if (req.get("fare") != null) s.setFare(new java.math.BigDecimal(req.get("fare").toString()));
            Optional<Schedule> created = scheduleService.create(busId, routeId, s);
            if (created.isEmpty()) return ResponseEntity.badRequest().body("Invalid busId or routeId");
            return ResponseEntity.ok(created.get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating schedule: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Map<String, Object> req) {
        Integer busId = req.get("busId") == null ? null : (Integer) req.get("busId");
        Integer routeId = req.get("routeId") == null ? null : (Integer) req.get("routeId");
        Schedule updates = new Schedule();
        if (req.get("travelDate") != null) updates.setTravelDate(java.time.LocalDate.parse((String) req.get("travelDate")));
        if (req.get("startTime") != null) updates.setStartTime(java.time.LocalTime.parse((String) req.get("startTime")));
        if (req.get("endTime") != null) updates.setEndTime(java.time.LocalTime.parse((String) req.get("endTime")));
        if (req.get("fare") != null) updates.setFare(new java.math.BigDecimal(req.get("fare").toString()));
        Optional<Schedule> updated = scheduleService.update(id, busId, routeId, updates);
        if (updated.isEmpty()) return ResponseEntity.badRequest().body("Schedule not found");
        return ResponseEntity.ok(updated.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        boolean ok = scheduleService.delete(id);
        if (ok) return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().body("Schedule not found");
    }

    // Fare management endpoints
    @PostMapping("/{id}/fare")
    public ResponseEntity<?> addFare(@PathVariable Integer id, @RequestBody Map<String, Object> req) {
        try {
            if (req.get("fare") == null) {
                return ResponseEntity.badRequest().body("Fare amount is required");
            }
            BigDecimal fare = new BigDecimal(req.get("fare").toString());
            Optional<Schedule> updated = scheduleService.updateFare(id, fare);
            if (updated.isEmpty()) {
                return ResponseEntity.badRequest().body("Schedule not found");
            }
            return ResponseEntity.ok(updated.get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating fare: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/fare")
    public ResponseEntity<?> updateFare(@PathVariable Integer id, @RequestBody Map<String, Object> req) {
        try {
            if (req.get("fare") == null) {
                return ResponseEntity.badRequest().body("Fare amount is required");
            }
            BigDecimal fare = new BigDecimal(req.get("fare").toString());
            Optional<Schedule> updated = scheduleService.updateFare(id, fare);
            if (updated.isEmpty()) {
                return ResponseEntity.badRequest().body("Schedule not found");
            }
            return ResponseEntity.ok(updated.get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating fare: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/fare")
    public ResponseEntity<?> deleteFare(@PathVariable Integer id) {
        try {
            Optional<Schedule> updated = scheduleService.updateFare(id, BigDecimal.ZERO);
            if (updated.isEmpty()) {
                return ResponseEntity.badRequest().body("Schedule not found");
            }
            return ResponseEntity.ok(updated.get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting fare: " + e.getMessage());
        }
    }
}


