package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.Exam;
import com.thanhdat.quanlyhoctap.entity.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    @Query("SELECT fx FROM Exam fx WHERE fx.courseClass.semester.id = :semesterId AND fx.type = :type")
    List<Exam> findBySemesterIdAndType(Long semesterId, ExamType type);

    Optional<Exam> findByCourseClassIdAndType(Long courseClassId, ExamType type);

    @Query("SELECT c.id, c.code, c.name, " +
            "CASE WHEN COUNT(cc.id) = COUNT(ex.id) " +
            "THEN TRUE ELSE FALSE END AS allFinalExamsPresent " +
            "FROM Course c " +
            "JOIN c.courseClasses cc " +
            "LEFT JOIN cc.exams ex " +
            "ON ex.type = :type AND ex.startTime IS NOT NULL AND ex.classroom IS NOT NULL " +
            "WHERE cc.semester.id = :semesterId " +
            "GROUP BY c.id, c.name " +
            "ORDER BY allFinalExamsPresent ASC, c.name ASC")
    List<Object[]> findCourseWithFinalExamStatus(Long semesterId, ExamType type);

    List<Exam> findByCourseClassIdInAndType(List<Long> courseClassIds, ExamType examType);
}
