package com.smartclassroom.backend.repository;

import com.smartclassroom.backend.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    java.util.List<Session> findByClassroomId(Long classroomId);
}
