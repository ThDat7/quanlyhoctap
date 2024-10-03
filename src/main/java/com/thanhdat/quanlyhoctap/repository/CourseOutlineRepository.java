package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.CourseOutline;
import com.thanhdat.quanlyhoctap.entity.OutlineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseOutlineRepository
        extends JpaRepository<CourseOutline, Long>,
        JpaSpecificationExecutor<CourseOutline> {
    List<CourseOutline> findByTeacherIdAndStatus(Long teacherId, OutlineStatus status);

    @Query("SELECT co FROM CourseOutline co " +
            "INNER JOIN co.educationProgramCourses epc " +
            "INNER JOIN epc.educationProgram ep " +
            "JOIN StudentClass sc ON sc.year = ep.schoolYear AND sc.major = ep.major " +
            "WHERE sc.id = :studentClassId AND co.course.id = :courseId")
    Optional<CourseOutline> findByStudentClassAndCourse(long studentClassId, long courseId);
}
