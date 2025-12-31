package com.smartclassroom.backend.repository;

import com.smartclassroom.backend.model.AttendancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendancePolicyRepository extends JpaRepository<AttendancePolicy, Long> {
    java.util.Optional<AttendancePolicy> findByIsActiveTrue();
}
