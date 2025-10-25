package com.example.busreservation.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Notification Model - Represents notifications sent to passengers
 * Used by: Passengers (receive booking/payment notifications)
 * Contains: Notification message, booking details, timestamp, and read status
 */
@Entity
@Table(name = "Notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_nic", referencedColumnName = "nic", nullable = false)
    private User user; // The passenger receiving the notification

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id", nullable = true)
    private Booking booking; // Related booking (if applicable)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_id", nullable = true)
    private Payment payment; // Related payment (if applicable)

    @Column(name = "message", nullable = false, length = 500)
    private String message; // Notification message

    @Column(name = "type", nullable = false, length = 50)
    private String type; // Type: PAYMENT_SUCCESS, BOOKING_CONFIRMED, etc.

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // When the notification was created

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false; // Whether the notification has been read

    // Constructors
    public Notification() {
        this.createdAt = LocalDateTime.now();
    }

    public Notification(User user, Booking booking, Payment payment, String message, String type) {
        this.user = user;
        this.booking = booking;
        this.payment = payment;
        this.message = message;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    // Getters and Setters
    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}

