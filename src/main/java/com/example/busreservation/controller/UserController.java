package com.example.busreservation.controller;

import com.example.busreservation.model.User;
import com.example.busreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * UserController - Handles user-related API endpoints
 * Used by: ALL ROLES (Passenger, IT Support, Customer Service, Finance, Ticketing, Operation Manager)
 * Functions: User registration, login, profile management, password reset, user listing
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // allow frontend requests
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        Optional<User> u = userService.loginUser(request.get("email"), request.get("password"));
        if (u.isPresent()) {
            return ResponseEntity.ok(u.get());
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        boolean ok = userService.resetPassword(request.get("email"), request.get("newPassword"));
        if (ok) return ResponseEntity.ok("Password reset successful");
        return ResponseEntity.badRequest().body("Email not found");
    }

    @GetMapping("/role")
    public ResponseEntity<?> getRole(@RequestParam("email") String email) {
        Optional<User> byEmail = userService.findByEmail(email);
        if (byEmail.isPresent()) {
            return ResponseEntity.ok(Map.of("role", byEmail.get().getRole()));
        }
        return ResponseEntity.badRequest().body("User not found");
    }

    @GetMapping("")
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userService.listAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updates) {
        Optional<User> updated = userService.updateUser(id, updates);
        if (updated.isPresent()) return ResponseEntity.ok(updated.get());
        return ResponseEntity.badRequest().body("User not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().body("User not found");
    }
}
