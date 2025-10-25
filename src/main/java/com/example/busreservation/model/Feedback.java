package com.example.busreservation.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Feedback Model - Represents customer feedback and complaints
 * Used by: Passengers (submit feedback/complaints), Customer Service (manage complaints)
 * Contains: Feedback details, complaint information, and customer service replies
 */
@Entity
@Table(name = "feedback")
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long feedbackId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "type", nullable = false)
    private String type; // feedback or complain
    
    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;
    
    @Column(name = "complain_date")
    private LocalDate complainDate;
    
    @Column(name = "complain_time")
    private LocalTime complainTime;
    
    @Column(name = "reply", columnDefinition = "TEXT")
    private String reply;
    
    @Column(name = "user_id")
    private Long userId;
    
    // Constructors
    public Feedback() {}
    
    public Feedback(String name, String type, String description, Long userId) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.userId = userId;
        this.complainDate = LocalDate.now();
        this.complainTime = LocalTime.now();
    }
    
    // Getters and Setters
    public Long getFeedbackId() {
        return feedbackId;
    }
    
    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getComplainDate() {
        return complainDate;
    }
    
    public void setComplainDate(LocalDate complainDate) {
        this.complainDate = complainDate;
    }
    
    public LocalTime getComplainTime() {
        return complainTime;
    }
    
    public void setComplainTime(LocalTime complainTime) {
        this.complainTime = complainTime;
    }
    
    public String getReply() {
        return reply;
    }
    
    public void setReply(String reply) {
        this.reply = reply;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    
    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId=" + feedbackId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", complainDate=" + complainDate +
                ", complainTime=" + complainTime +
                ", reply='" + reply + '\'' +
                ", userId=" + userId +
                '}';
    }
}
