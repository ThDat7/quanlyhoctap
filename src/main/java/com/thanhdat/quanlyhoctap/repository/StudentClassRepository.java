package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentClassRepository extends JpaRepository<StudentClass, Long> {
    List<StudentClass> findByYearAndMajorName(int grade, String majorName);

    @Query("SELECT sc FROM StudentClass sc " +
            "JOIN EducationProgram ep ON sc.year = ep.schoolYear AND sc.major = ep.major " +
            "INNER JOIN ep.educationProgramCourses epc " +
            "WHERE epc.semester.id = :semesterId AND epc.course.id = :courseId")
    List<StudentClass> findByCourseAndSemesterInEducationProgram(long courseId, long semesterId);
}
