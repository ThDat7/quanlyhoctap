package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CourseClassRepository extends JpaRepository<CourseClass, Integer> {
    List<CourseClass> findBySemesterIdAndStudentClassId(Integer semesterId, Integer studentClassId);
    @Query("SELECT cc FROM CourseClass cc JOIN cc.studies s WHERE cc.semester.id = ?1 AND s.student.id = ?2")
    List<CourseClass> findBySemesterIdAndStudentId(Integer semesterId, Integer studentId);
}
