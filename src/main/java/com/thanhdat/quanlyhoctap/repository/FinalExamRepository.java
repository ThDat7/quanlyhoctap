package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.FinalExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FinalExamRepository extends JpaRepository<FinalExam, Integer> {
    @Query("SELECT fx FROM FinalExam fx WHERE fx.courseClass.semester.id = :semesterId")
    List<FinalExam> findBySemesterId(Integer semesterId);
}
