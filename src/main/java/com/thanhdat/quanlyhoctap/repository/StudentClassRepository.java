package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentClassRepository extends JpaRepository<StudentClass, Long> {
    List<StudentClass> findByYearAndMajorName(int grade, String majorName);
}
