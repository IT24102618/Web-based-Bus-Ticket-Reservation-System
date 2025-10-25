package com.example.busreservation.repository;

import com.example.busreservation.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    // Find all feedback by user ID
    List<Feedback> findByUserId(Long userId);
    
    // Find all feedback by type (feedback or complain)
    List<Feedback> findByType(String type);
    
    // Find all feedback with replies
    @Query("SELECT f FROM Feedback f WHERE f.reply IS NOT NULL AND f.reply != ''")
    List<Feedback> findWithReplies();
    
    // Find all feedback without replies
    @Query("SELECT f FROM Feedback f WHERE f.reply IS NULL OR f.reply = ''")
    List<Feedback> findWithoutReplies();
    
    // Find feedback by user ID and type
    List<Feedback> findByUserIdAndType(Long userId, String type);
    
    // Count feedback by type
    long countByType(String type);
    
    // Count feedback by user
    long countByUserId(Long userId);
}

