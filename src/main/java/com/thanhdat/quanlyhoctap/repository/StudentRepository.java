package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Long countByStudentClassId(Integer studentClassId);
}
