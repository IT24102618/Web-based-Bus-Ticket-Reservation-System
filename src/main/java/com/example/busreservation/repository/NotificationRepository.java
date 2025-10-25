package com.example.busreservation.repository;

import com.example.busreservation.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
    // Find all notifications for a specific user (by NIC)
    List<Notification> findByUserNicOrderByCreatedAtDesc(String userNic);
    
    // Find unread notifications for a specific user
    List<Notification> findByUserNicAndIsReadFalseOrderByCreatedAtDesc(String userNic);
    
    // Find notifications by type
    List<Notification> findByType(String type);
    
    // Count unread notifications for a user
    long countByUserNicAndIsReadFalse(String userNic);
    
    // Find notifications by booking ID
    List<Notification> findByBookingBookingId(Integer bookingId);
    
    // Delete notifications by booking ID (for cascade delete when booking is deleted)
    void deleteByBookingBookingId(Integer bookingId);
}

