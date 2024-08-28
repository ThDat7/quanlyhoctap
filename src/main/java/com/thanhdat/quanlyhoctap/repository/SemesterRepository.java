package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer> {
    Semester findByYearAndSemester(Integer year, Integer semester);

    @Query("SELECT s FROM Semester s WHERE s.startDate <= :start AND s.startDate + ((s.durationWeeks * 7 - 1) day) >= :end")
    Semester findByDateRange(LocalDate start, LocalDate end);
    @Query("SELECT epc.semester FROM EducationProgramCourse epc JOIN epc.educationProgram ep JOIN StudentClass sc ON " +
            "sc.major = ep.major AND sc.year = ep.schoolYear JOIN sc.students s WHERE s.id = :studentId " +
            "ORDER BY epc.semester.year DESC, epc.semester.semester DESC")
    List<Semester> findByStudentId(Integer studentId);
}