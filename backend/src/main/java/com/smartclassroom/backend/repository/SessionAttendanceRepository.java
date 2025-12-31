package com.smartclassroom.backend.repository;

import com.smartclassroom.backend.model.SessionAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionAttendanceRepository extends JpaRepository<SessionAttendance, Long> {
    List<SessionAttendance> findByStudentIdAndDate(Long studentId, LocalDate date);

    Optional<SessionAttendance> findByStudentIdAndDateAndSessionNumber(Long studentId, LocalDate date,
            Integer sessionNumber);

    // Find UNCERTAIN sessions to block daily generation
    boolean existsByStudentIdAndDateAndStatus(Long studentId, LocalDate date, String status);
}
