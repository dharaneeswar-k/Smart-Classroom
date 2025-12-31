package com.smartclassroom.backend.repository;

import com.smartclassroom.backend.model.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CameraRepository extends JpaRepository<Camera, Long> {
}
