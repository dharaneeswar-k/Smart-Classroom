package com.smartclassroom.backend.repository;

import com.smartclassroom.backend.model.PresenceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresenceEventRepository extends JpaRepository<PresenceEvent, Long> {
    // java.util.List<PresenceEvent> findBySessionId(Long sessionId); // Removed
    java.util.List<PresenceEvent> findByStudentIdAndDate(Long studentId, java.time.LocalDate date);

    java.util.List<PresenceEvent> findByDate(java.time.LocalDate date);
}
