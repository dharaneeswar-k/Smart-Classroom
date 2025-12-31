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
@Table(name = "STUDENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "student_roll_no", unique = true, nullable = false)
    private String studentRollNo;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "face_image_path")
    private String faceImagePath;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
