package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentStatusRepository extends JpaRepository<StudentStatus, Integer> {
    @Query("SELECT s FROM StudentStatus s WHERE s.student.id = :studentId " +
            "ORDER BY s.semester.year DESC, s.semester.semester DESC")
    List<StudentStatus> findByStudentId(Integer studentId);
}
