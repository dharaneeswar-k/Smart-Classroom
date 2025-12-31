package com.smartclassroom.backend.repository;

import com.smartclassroom.backend.model.ODRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ODRequestRepository extends JpaRepository<ODRequest, Long> {
    // java.util.List<ODRequest> findBySessionId(Long sessionId);
    java.util.List<ODRequest> findByDate(java.time.LocalDate date);

    java.util.List<ODRequest> findByStudentId(Long studentId);

    // java.util.Optional<ODRequest> findByStudentIdAndSessionId(Long studentId,
    // Long sessionId);
    java.util.Optional<ODRequest> findByStudentIdAndDate(Long studentId, java.time.LocalDate date);
}
