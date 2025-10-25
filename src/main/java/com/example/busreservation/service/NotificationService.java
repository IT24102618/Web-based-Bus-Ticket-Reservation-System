package com.example.busreservation.service;

import com.example.busreservation.model.Booking;
import com.example.busreservation.model.Notification;
import com.example.busreservation.model.Payment;
import com.example.busreservation.model.User;
import com.example.busreservation.observer.PaymentObserver;
import com.example.busreservation.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * NotificationService - Implements Observer pattern for payment notifications
 * Observes payment events and creates notifications for passengers
 */
@Service
public class NotificationService implements PaymentObserver {

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Observer method - called when a payment is completed
     * Creates a notification for the passenger with booking details
     */
    @Override
    public void onPaymentCompleted(Payment payment) {
        try {
            Booking booking = payment.getBooking();
            User user = booking.getUser();
            
            // Create notification message with booking details
            String message = String.format(
                "Payment successful! Booking confirmed for %s to %s on %s. Amount: LKR %.2f. Tickets: %d",
                booking.getSchedule().getRoute().getFromStand(),
                booking.getSchedule().getRoute().getToStand(),
                booking.getSchedule().getStartTime(),
                payment.getTotalPrice(),
                booking.getBookedTickets()
            );
            
            Notification notification = new Notification(
                user,
                booking,
                payment,
                message,
                "PAYMENT_SUCCESS"
            );
            
            notificationRepository.save(notification);
            System.out.println("Notification created for user: " + user.getNic());
        } catch (Exception e) {
            System.err.println("Error creating notification: " + e.getMessage());
        }
    }

    /**
     * Get all notifications for a user
     */
    public List<Notification> getNotificationsForUser(String userNic) {
        return notificationRepository.findByUserNicOrderByCreatedAtDesc(userNic);
    }

    /**
     * Get unread notifications for a user
     */
    public List<Notification> getUnreadNotificationsForUser(String userNic) {
        return notificationRepository.findByUserNicAndIsReadFalseOrderByCreatedAtDesc(userNic);
    }

    /**
     * Get unread notification count for a user
     */
    public long getUnreadCount(String userNic) {
        return notificationRepository.countByUserNicAndIsReadFalse(userNic);
    }

    /**
     * Mark a notification as read
     */
    public boolean markAsRead(Integer notificationId) {
        try {
            Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
            if (notificationOpt.isPresent()) {
                Notification notification = notificationOpt.get();
                notification.setIsRead(true);
                notificationRepository.save(notification);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error marking notification as read: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mark all notifications as read for a user
     */
    public boolean markAllAsRead(String userNic) {
        try {
            List<Notification> notifications = notificationRepository.findByUserNicAndIsReadFalseOrderByCreatedAtDesc(userNic);
            for (Notification notification : notifications) {
                notification.setIsRead(true);
            }
            notificationRepository.saveAll(notifications);
            return true;
        } catch (Exception e) {
            System.err.println("Error marking all notifications as read: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete all notifications associated with a booking ID
     */
    public void deleteNotificationsByBookingId(Integer bookingId) {
        notificationRepository.deleteByBookingBookingId(bookingId);
    }

    /**
     * Get notification with formatted data for frontend
     */
    public Map<String, Object> formatNotification(Notification notification) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", notification.getNotificationId());
        data.put("message", notification.getMessage());
        data.put("type", notification.getType());
        data.put("createdAt", notification.getCreatedAt().toString());
        data.put("isRead", notification.getIsRead());
        
        if (notification.getBooking() != null) {
            Map<String, Object> bookingData = new HashMap<>();
            Booking booking = notification.getBooking();
            bookingData.put("bookingId", booking.getBookingId());
            bookingData.put("passengerName", booking.getFirstName() + " " + booking.getLastName());
            bookingData.put("tickets", booking.getBookedTickets());
            
            if (booking.getSchedule() != null && booking.getSchedule().getRoute() != null) {
                bookingData.put("route", booking.getSchedule().getRoute().getFromStand() + " to " + 
                              booking.getSchedule().getRoute().getToStand());
                bookingData.put("departureTime", booking.getSchedule().getStartTime().toString());
            }
            
            data.put("booking", bookingData);
        }
        
        if (notification.getPayment() != null) {
            Map<String, Object> paymentData = new HashMap<>();
            Payment payment = notification.getPayment();
            paymentData.put("paymentId", payment.getPaymentId());
            paymentData.put("amount", payment.getTotalPrice());
            paymentData.put("paymentMethod", payment.getPaymentMethod());
            paymentData.put("paymentDate", payment.getPaymentDate().toString());
            data.put("payment", paymentData);
        }
        
        return data;
    }
}

