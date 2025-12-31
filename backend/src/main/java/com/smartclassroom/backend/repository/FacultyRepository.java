package com.smartclassroom.backend.repository;

import com.smartclassroom.backend.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    java.util.Optional<Faculty> findByUserUsername(String username);
}
