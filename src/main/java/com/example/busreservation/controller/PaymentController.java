package com.example.busreservation.controller;

import com.example.busreservation.model.Payment;
import com.example.busreservation.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * PaymentController - Handles payment and refund management API endpoints
 * Used by: Passengers (make payments), Finance (manage payments and refunds)
 * Functions: Create payments, update payment status, process refunds
 */
@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Integer id) {
        Optional<Payment> payment = paymentService.getPaymentById(id);
        if (payment.isPresent()) {
            return ResponseEntity.ok(payment.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<Payment>> getPaymentsByBookingId(@PathVariable Integer bookingId) {
        List<Payment> payments = paymentService.getPaymentsByBookingId(bookingId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable String status) {
        List<Payment> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }

    @PostMapping("")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> req) {
        try {
            Integer bookingId = (Integer) req.get("bookingId");
            String paymentMethod = (String) req.get("paymentMethod");
            String paymentStatus = (String) req.get("paymentStatus");
            BigDecimal totalPrice = new BigDecimal(req.get("totalPrice").toString());

            if (bookingId == null || paymentMethod == null || paymentStatus == null || totalPrice == null) {
                return ResponseEntity.badRequest().body("Missing required fields");
            }

            Optional<Payment> payment = paymentService.createPayment(
                bookingId, paymentMethod, paymentStatus, totalPrice
            );

            if (payment.isPresent()) {
                return ResponseEntity.ok(payment.get());
            } else {
                return ResponseEntity.badRequest().body("Unable to create payment - check if booking exists");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating payment: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable Integer id, @RequestBody Map<String, String> req) {
        try {
            String newStatus = req.get("paymentStatus");
            if (newStatus == null) {
                return ResponseEntity.badRequest().body("Payment status is required");
            }

            boolean success = paymentService.updatePaymentStatus(id, newStatus);
            if (success) {
                return ResponseEntity.ok().body("Payment status updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Payment not found or could not be updated");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating payment status: " + e.getMessage());
        }
    }
}

