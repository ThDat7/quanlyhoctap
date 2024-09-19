package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.Course;
import com.thanhdat.quanlyhoctap.entity.EducationProgramCourse;
import com.thanhdat.quanlyhoctap.entity.Semester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EducationProgramCourseRepository
        extends JpaRepository<EducationProgramCourse, Long>,
        JpaSpecificationExecutor<EducationProgramCourse> {
    List<EducationProgramCourse> findByCourse(Course course);

    @Modifying
    @Query(value="INSERT INTO education_program_courses (education_program_id, course_id, semester_id) " +
            "SELECT newEp.id ,epc.course_id, " +
                "(SELECT newSemester.id FROM semesters newSemester " +
                "WHERE newSemester.semester = oldSemester.semester " +
                "and newSemester.year = oldSemester.year + :toYear - :fromYear)" +
            "FROM education_program_courses epc " +
            "JOIN semesters oldSemester ON oldSemester.id = epc.semester_id " +
            "JOIN education_programs oldEp ON oldEp.school_year = :fromYear " +
            "JOIN education_programs newEp ON newEp.school_year = :toYear AND newEp.major_id = oldEp.major_id " +
            "WHERE epc.education_program_id = oldEp.id", nativeQuery = true)
    void cloneBatching(int fromYear, int toYear);
}
