package com.example.busreservation.repository;

import com.example.busreservation.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    
    List<Payment> findByBookingBookingId(Integer bookingId);
    
    List<Payment> findByPaymentStatus(String paymentStatus);
}

