package com.example.busreservation.controller;

import com.example.busreservation.model.Feedback;
import com.example.busreservation.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * FeedbackController - Handles feedback and complaint management API endpoints
 * Used by: Passengers (submit feedback/complaints), Customer Service (manage complaints)
 * Functions: Create feedback, reply to complaints, view feedback by user
 */
@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {
    
    @Autowired
    private FeedbackService feedbackService;
    
    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Feedback API is working!");
    }
    
    // Create new feedback
    @PostMapping
    public ResponseEntity<?> createFeedback(@RequestBody Map<String, Object> feedbackData) {
        try {
            String name = (String) feedbackData.get("name");
            String type = (String) feedbackData.get("type");
            String description = (String) feedbackData.get("description");
            Long userId = feedbackData.get("userId") != null ? 
                Long.valueOf(feedbackData.get("userId").toString()) : null;
            
            // Validate required fields
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Name is required");
            }
            if (type == null || type.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Type is required");
            }
            if (description == null || description.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Description is required");
            }
            
            // Validate type
            if (!type.equals("feedback") && !type.equals("complain")) {
                return ResponseEntity.badRequest().body("Type must be either 'feedback' or 'complain'");
            }
            
            Feedback feedback = feedbackService.createFeedbackWithUserId(name, type, description, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(feedback);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating feedback: " + e.getMessage());
        }
    }
    
    // Get all feedback
    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        try {
            List<Feedback> feedback = feedbackService.getAllFeedback();
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            e.printStackTrace();
            // Return empty list instead of error for now
            return ResponseEntity.ok(new java.util.ArrayList<>());
        }
    }
    
    // Get feedback by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable Long id) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        if (feedback.isPresent()) {
            return ResponseEntity.ok(feedback.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Get feedback by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Feedback>> getFeedbackByUserId(@PathVariable Long userId) {
        List<Feedback> feedback = feedbackService.getFeedbackByUserId(userId);
        return ResponseEntity.ok(feedback);
    }
    
    // Get feedback by type
    @GetMapping("/type/{type}")
    public ResponseEntity<?> getFeedbackByType(@PathVariable String type) {
        if (!type.equals("feedback") && !type.equals("complain")) {
            return ResponseEntity.badRequest().body("Type must be either 'feedback' or 'complain'");
        }
        List<Feedback> feedback = feedbackService.getFeedbackByType(type);
        return ResponseEntity.ok(feedback);
    }
    
    // Get feedback with replies
    @GetMapping("/with-replies")
    public ResponseEntity<List<Feedback>> getFeedbackWithReplies() {
        List<Feedback> feedback = feedbackService.getFeedbackWithReplies();
        return ResponseEntity.ok(feedback);
    }
    
    // Get feedback without replies
    @GetMapping("/without-replies")
    public ResponseEntity<List<Feedback>> getFeedbackWithoutReplies() {
        List<Feedback> feedback = feedbackService.getFeedbackWithoutReplies();
        return ResponseEntity.ok(feedback);
    }
    
    // Add reply to feedback (only for complaints)
    @PutMapping("/{id}/reply")
    public ResponseEntity<?> addReply(@PathVariable Long id, @RequestBody Map<String, String> replyData) {
        try {
            String reply = replyData.get("reply");
            if (reply == null || reply.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Reply content is required");
            }
            
            // Check if the feedback exists and is a complaint
            Optional<Feedback> existingFeedback = feedbackService.getFeedbackById(id);
            if (existingFeedback.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            if (!existingFeedback.get().getType().equals("complain")) {
                return ResponseEntity.badRequest().body("Replies can only be added to complaints, not feedback");
            }
            
            Feedback feedback = feedbackService.addReply(id, reply);
            return ResponseEntity.ok(feedback);
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error adding reply: " + e.getMessage());
        }
    }
    
    // Update feedback
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeedback(@PathVariable Long id, @RequestBody Feedback feedback) {
        try {
            Optional<Feedback> existingFeedback = feedbackService.getFeedbackById(id);
            if (existingFeedback.isPresent()) {
                feedback.setFeedbackId(id);
                Feedback updatedFeedback = feedbackService.updateFeedback(feedback);
                return ResponseEntity.ok(updatedFeedback);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating feedback: " + e.getMessage());
        }
    }
    
    // Delete feedback
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {
        try {
            Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
            if (feedback.isPresent()) {
                feedbackService.deleteFeedback(id);
                return ResponseEntity.ok().body("Feedback deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting feedback: " + e.getMessage());
        }
    }
    
    // Get feedback statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getFeedbackStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFeedback", feedbackService.getTotalFeedbackCount());
        stats.put("feedbackCount", feedbackService.getFeedbackCountByType("feedback"));
        stats.put("complainCount", feedbackService.getFeedbackCountByType("complain"));
        stats.put("withReplies", feedbackService.getFeedbackWithReplies().size());
        stats.put("withoutReplies", feedbackService.getFeedbackWithoutReplies().size());
        
        return ResponseEntity.ok(stats);
    }
}
