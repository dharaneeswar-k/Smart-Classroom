package com.smartclassroom.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "SESSION_ATTENDANCE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate date;

    @Column(name = "session_number", nullable = false)
    private Integer sessionNumber; // 1, 2, or 3

    @Column(nullable = false)
    private String status; // PRESENT, ABSENT, UNCERTAIN

    @ManyToOne
    @JoinColumn(name = "resolved_by")
    private Faculty resolvedBy;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
