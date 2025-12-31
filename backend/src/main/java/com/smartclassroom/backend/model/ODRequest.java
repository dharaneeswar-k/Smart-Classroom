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
@Table(name = "OD_REQUEST")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ODRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // @ManyToOne
    // @JoinColumn(name = "session_id")
    // private Session session;

    @Column(name = "od_date")
    private java.time.LocalDate date;

    private String reason;
    private String status;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Faculty approvedBy;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "decision_at")
    private LocalDateTime decisionAt;
}
