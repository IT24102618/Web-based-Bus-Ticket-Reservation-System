package com.example.busreservation.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Payment Model - Represents payment transactions for bookings
 * Used by: Passengers (make payments), Finance (manage payments and refunds)
 * Contains: Payment details, booking reference, payment method, and status
 */
@Entity
@Table(name = "Payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId; // Unique payment identifier

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking; // Reference to the booking being paid for

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate; // Date when payment was made

    @Column(name = "payment_status", nullable = false, length = 20)
    private String paymentStatus; // Payment status (paid, refund requested, refund accepted)

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod; // Payment method (credit card, cash, etc.)

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice; // Total amount paid

    // Constructors
    public Payment() {}

    public Payment(Booking booking, LocalDate paymentDate, String paymentStatus, 
                   String paymentMethod, BigDecimal totalPrice) {
        this.booking = booking;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}

