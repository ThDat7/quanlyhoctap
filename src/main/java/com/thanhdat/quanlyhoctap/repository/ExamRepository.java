package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.Exam;
import com.thanhdat.quanlyhoctap.entity.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
    @Query("SELECT fx FROM Exam fx WHERE fx.courseClass.semester.id = :semesterId AND fx.type = :type")
    List<Exam> findBySemesterIdAndType(Integer semesterId, ExamType type);

    Optional<Exam> findByCourseClassIdAndType(Integer courseClassId, ExamType type);
}
