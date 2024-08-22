package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.Course;
import com.thanhdat.quanlyhoctap.entity.EducationProgramCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationProgramCourseRepository extends JpaRepository<EducationProgramCourse, Integer> {
    List<EducationProgramCourse> findByCourse(Course course);
}
