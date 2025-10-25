package com.example.busreservation.service;

import com.example.busreservation.model.Booking;
import com.example.busreservation.model.Payment;
import com.example.busreservation.observer.PaymentObserver;
import com.example.busreservation.repository.BookingRepository;
import com.example.busreservation.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    // List of observers (Observer pattern)
    private List<PaymentObserver> observers = new ArrayList<>();
    
    /**
     * Register an observer to be notified when payments are completed
     */
    public void registerObserver(PaymentObserver observer) {
        observers.add(observer);
    }
    
    /**
     * Notify all observers when a payment is completed
     */
    private void notifyObservers(Payment payment) {
        for (PaymentObserver observer : observers) {
            observer.onPaymentCompleted(payment);
        }
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Integer paymentId) {
        return paymentRepository.findById(paymentId);
    }

    public List<Payment> getPaymentsByBookingId(Integer bookingId) {
        return paymentRepository.findByBookingBookingId(bookingId);
    }

    public List<Payment> getPaymentsByStatus(String paymentStatus) {
        return paymentRepository.findByPaymentStatus(paymentStatus);
    }

    public Optional<Payment> createPayment(Integer bookingId, String paymentMethod, 
                                         String paymentStatus, BigDecimal totalPrice) {
        try {
            Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
            if (bookingOpt.isEmpty()) {
                return Optional.empty();
            }
            
            Booking booking = bookingOpt.get();
            Payment payment = new Payment(booking, LocalDate.now(), paymentStatus, 
                                        paymentMethod, totalPrice);
            
            Payment savedPayment = paymentRepository.save(payment);
            
            // Notify observers if payment is successful (Observer pattern)
            if ("paid".equalsIgnoreCase(paymentStatus)) {
                notifyObservers(savedPayment);
            }
            
            return Optional.of(savedPayment);
        } catch (Exception e) {
            System.err.println("Error creating payment: " + e.getMessage());
            return Optional.empty();
        }
    }

    public boolean updatePaymentStatus(Integer paymentId, String newStatus) {
        try {
            Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
            if (paymentOpt.isPresent()) {
                Payment payment = paymentOpt.get();
                payment.setPaymentStatus(newStatus);
                paymentRepository.save(payment);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error updating payment status: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePaymentForBooking(Integer bookingId, BigDecimal newTotalPrice) {
        try {
            List<Payment> payments = paymentRepository.findByBookingBookingId(bookingId);
            if (payments.isEmpty()) {
                return false;
            }
            
            // Update the first payment found for this booking
            Payment payment = payments.get(0);
            payment.setTotalPrice(newTotalPrice);
            paymentRepository.save(payment);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating payment for booking: " + e.getMessage());
            return false;
        }
    }

    public boolean deletePaymentsByBookingId(Integer bookingId) {
        try {
            List<Payment> payments = paymentRepository.findByBookingBookingId(bookingId);
            if (!payments.isEmpty()) {
                paymentRepository.deleteAll(payments);
                System.out.println("Deleted " + payments.size() + " payment(s) for booking ID: " + bookingId);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting payments for booking: " + e.getMessage());
            return false;
        }
    }
}

