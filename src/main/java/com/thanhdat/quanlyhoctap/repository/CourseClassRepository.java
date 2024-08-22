package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CourseClassRepository extends JpaRepository<CourseClass, Integer> {
    List<CourseClass> findBySemesterIdAndStudentClassId(Integer semesterId, Integer studentClassId);
}
