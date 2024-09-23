package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Optional<Semester> findByYearAndSemester(Integer year, Integer semester);

    @Query("SELECT s FROM Semester s WHERE s.startDate <= :start AND s.startDate + ((s.durationWeeks * 7 - 1) day) >= :end")
    Optional<Semester> findByDateRange(LocalDate start, LocalDate end);
    @Query("SELECT epc.semester FROM EducationProgramCourse epc JOIN epc.educationProgram ep JOIN StudentClass sc ON " +
            "sc.major = ep.major AND sc.year = ep.schoolYear JOIN sc.students s WHERE s.id = :studentId " +
            "ORDER BY epc.semester.year DESC, epc.semester.semester DESC")
    List<Semester> findByStudentId(Long studentId);

    @Query("SELECT s FROM Semester s WHERE s.id = cast((SELECT st.value FROM Setting st WHERE st.key = 'SEMESTER_ID_FOR_REGISTER') as int)")
    Optional<Semester> findForRegister();

    @Query("SELECT s FROM Semester s JOIN CourseClass cc ON cc.semester = s JOIN cc.teacher t WHERE t.id = :teacherId " +
            "ORDER BY s.year DESC, s.semester DESC")
    List<Semester> findByTeacherId(Long teacherId);

    List<Semester> findByLockTimeGreaterThan(LocalDateTime lockTime);

    Optional<Semester> findBySemesterAndYear(Integer semester, Integer year);

    List<Semester> findByYearBetween(int fromYear, int toYear);
}