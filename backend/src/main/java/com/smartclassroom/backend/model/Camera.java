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

@Entity
@Table(name = "CAMERA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Camera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "camera_code", unique = true, nullable = false)
    private String cameraCode;

    @Column(name = "camera_http_url")
    private String cameraHttpUrl;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    private String status;
}
