package com.smartclassroom.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PRESENCE_EVENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresenceEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "camera_id")
    private Camera camera;

    // Session removed
    // @ManyToOne
    // @JoinColumn(name = "session_id")
    // private Session session;

    @Column(name = "attendance_date")
    private java.time.LocalDate date;

    @Column(name = "event_time")
    private LocalDateTime eventTime;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    public java.time.LocalDate getDate() {
        return date;
    }

    public void setDate(java.time.LocalDate date) {
        this.date = date;
    }
}
