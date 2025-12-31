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
@Table(name = "ATTENDANCE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // Session removed
    // @ManyToOne
    // @JoinColumn(name = "session_id")
    // private Session session;

    @Column(name = "attendance_date")
    private java.time.LocalDate date;

    private String status;

    @Column(name = "attendance_percentage")
    private Double attendancePercentage;

    @Column(name = "late_entry_flag")
    private Boolean lateEntryFlag;

    @Column(name = "early_exit_flag")
    private Boolean earlyExitFlag;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
}
