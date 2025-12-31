package com.smartclassroom.backend.repository;

import com.smartclassroom.backend.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // java.util.List<Attendance> findBySessionId(Long sessionId);
    java.util.List<Attendance> findByDate(java.time.LocalDate date);

    java.util.List<Attendance> findByStudentId(Long studentId);

    // boolean existsByStudentIdAndSessionId(Long studentId, Long sessionId);
    boolean existsByStudentIdAndDate(Long studentId, java.time.LocalDate date);

    Optional<Attendance> findByStudentIdAndDate(Long studentId, java.time.LocalDate date);
}
