package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.CourseClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseClassRepository
        extends JpaRepository<CourseClass, Long>,
        JpaSpecificationExecutor<CourseClass> {
    List<CourseClass> findBySemesterIdAndStudentClassId(Long semesterId, Long studentClassId);
    @Query("SELECT cc FROM CourseClass cc JOIN cc.studies s WHERE cc.semester.id = ?1 AND s.student.id = ?2")
    List<CourseClass> findBySemesterIdAndStudentId(Long semesterId, Long studentId);

    @Query("SELECT cs, (cs.capacity - COUNT(study.id)) " +
            "FROM CourseClass cs " +
            "LEFT JOIN cs.studies study " +
            "WHERE cs.semester.id = " +
            "(SELECT cast(st.value as int) FROM Setting st WHERE st.key = 'SEMESTER_ID_FOR_REGISTER') " +
            "AND cs.course IN (" +
                "SELECT epc.course FROM EducationProgramCourse epc " +
                "WHERE epc.educationProgram.major = (" +
                    "SELECT student.studentClass.major FROM Student student WHERE student.id = :studentId" +
                ") " +
                "AND epc.educationProgram.schoolYear = (" +
                    "SELECT student.studentClass.year FROM Student student WHERE student.id = :studentId" +
                ")" +
            ") " +
            "GROUP BY cs.id, cs.capacity")
    List<Object[]> findOpenRegisterInStudentEducationProgram(Long studentId);

    @Query("SELECT COUNT(cs.id) > 0  FROM CourseClass cs LEFT JOIN cs.studies st " +
            "WHERE cs.id = :courseClassId " +
            "GROUP BY cs.id, cs.capacity " +
            "HAVING COUNT(st.id) < cs.capacity")
    Boolean existsByIdAndNotFull(Long courseClassId);
    @Query("SELECT COUNT(cs.id) > 0 " +
            "FROM CourseClass cs WHERE cs.id = :courseClassId " +
            "AND cs.course IN (" +
                "SELECT epc.course FROM EducationProgramCourse epc " +
                "WHERE epc.educationProgram.major = (" +
                    "SELECT student.studentClass.major FROM Student student WHERE student.id = :studentId" +
                ") " +
                "AND epc.educationProgram.schoolYear = (" +
                    "SELECT student.studentClass.year FROM Student student WHERE student.id = :studentId" +
                ")" +
            ")")
    Boolean isCourseClassInStudentEducationProgram(Long courseClassId, Long studentId);

    @Query("SELECT cc FROM CourseClass cc WHERE cc.semester.id = :semesterId AND cc.teacher.id = :teacherId")
    List<CourseClass> findBySemesterIdAndTeacherId(Long semesterId, Long teacherId);


    Boolean existsByIdAndTeacherId(Long id, Long teacherId);

    @Query("SELECT COUNT(cc.id) > 0 FROM CourseClass cc WHERE cc.id = :id AND cc.semester.lockTime > CURRENT_TIMESTAMP")
    Boolean existsByIdAndSemesterNotLocked(Long id);

    List<CourseClass> findByTeacherIdAndSemesterId(Long teacherId, Long semesterId);

    Page<CourseClass> findBySemesterIdAndCourseId(Long semesterId, Long courseId, Pageable pageable);
}
