package com.smartclassroom.backend.repository;

import com.smartclassroom.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    java.util.List<Student> findByClassroomId(Long classroomId);

    java.util.Optional<Student> findByUserUsername(String username);

    Optional<Student> findByStudentRollNo(String studentRollNo);
}
