package com.smartclassroom.backend.repository;

import com.smartclassroom.backend.model.FacultyClassroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyClassroomRepository extends JpaRepository<FacultyClassroom, Long> {
    java.util.List<FacultyClassroom> findByFacultyId(Long facultyId);

    void deleteByFacultyId(Long facultyId);
}
