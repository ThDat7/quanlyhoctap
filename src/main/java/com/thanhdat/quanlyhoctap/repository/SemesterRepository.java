package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer> {
    Semester findByYearAndSemester(Integer year, Integer semester);

    @Query("SELECT s FROM Semester s WHERE s.startDate <= :start AND s.startDate + ((s.durationWeeks * 7 - 1) day) >= :end")
    Semester findByDateRange(LocalDate start, LocalDate end);
}