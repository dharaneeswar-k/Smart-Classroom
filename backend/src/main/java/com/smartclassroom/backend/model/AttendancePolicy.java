package com.smartclassroom.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ATTENDANCE_POLICY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendancePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_presence_percentage")
    private Double minPresencePercentage;

    @Column(name = "slot_duration_minutes")
    private Integer slotDurationMinutes;

    @Column(name = "confidence_threshold")
    private Double confidenceThreshold;

    @Column(name = "late_entry_grace_minutes")
    private Integer lateEntryGraceMinutes;

    @Column(name = "early_exit_grace_minutes")
    private Integer earlyExitGraceMinutes;

    @Column(name = "late_entry_handling")
    private String lateEntryHandling;

    @Column(name = "early_exit_handling")
    private String earlyExitHandling;

    @Column(name = "od_override_enabled")
    private Boolean odOverrideEnabled;

    @Column(name = "manual_override_enabled")
    private Boolean manualOverrideEnabled;

    @Column(name = "effective_from")
    private LocalDateTime effectiveFrom;

    @Column(name = "is_active")
    private Boolean isActive;
}
