package com.example.busreservation.service;

import com.example.busreservation.model.Feedback;
import com.example.busreservation.model.User;
import com.example.busreservation.repository.FeedbackRepository;
import com.example.busreservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Create new feedback
    public Feedback createFeedback(Feedback feedback) {
        // Set current date and time
        feedback.setComplainDate(LocalDate.now());
        feedback.setComplainTime(LocalTime.now());
        
        return feedbackRepository.save(feedback);
    }
    
    // Create feedback with user ID
    public Feedback createFeedbackWithUserId(String name, String type, String description, Long userId) {
        Feedback feedback = new Feedback(name, type, description, userId);
        return feedbackRepository.save(feedback);
    }
    
    // Get all feedback
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
    
    // Get feedback by ID
    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }
    
    // Get feedback by user ID
    public List<Feedback> getFeedbackByUserId(Long userId) {
        return feedbackRepository.findByUserId(userId);
    }
    
    // Get feedback by type
    public List<Feedback> getFeedbackByType(String type) {
        return feedbackRepository.findByType(type);
    }
    
    // Get feedback with replies
    public List<Feedback> getFeedbackWithReplies() {
        return feedbackRepository.findWithReplies();
    }
    
    // Get feedback without replies
    public List<Feedback> getFeedbackWithoutReplies() {
        return feedbackRepository.findWithoutReplies();
    }
    
    // Update feedback
    public Feedback updateFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }
    
    // Add reply to feedback
    public Feedback addReply(Long feedbackId, String reply) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findById(feedbackId);
        if (feedbackOpt.isPresent()) {
            Feedback feedback = feedbackOpt.get();
            feedback.setReply(reply);
            return feedbackRepository.save(feedback);
        }
        throw new RuntimeException("Feedback not found with id: " + feedbackId);
    }
    
    // Delete feedback
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
    
    // Get feedback statistics
    public long getFeedbackCountByType(String type) {
        return feedbackRepository.countByType(type);
    }
    
    public long getFeedbackCountByUser(Long userId) {
        return feedbackRepository.countByUserId(userId);
    }
    
    // Get total feedback count
    public long getTotalFeedbackCount() {
        return feedbackRepository.count();
    }
}
