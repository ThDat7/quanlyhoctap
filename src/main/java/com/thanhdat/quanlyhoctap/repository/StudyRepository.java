package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<Study, Integer> {
    List<Study> findByStudentId(Integer studentId);
    @Query("SELECT s FROM Study s WHERE s.student.id = :studentId AND s.courseClass.semester.id = :semesterId")
    List<Study> findByStudentIdAndSemesterId(Integer studentId, Integer semesterId);

    Boolean existsByStudentIdAndCourseClassId(Integer studentId, Integer courseClassId);

    void deleteByStudentIdAndCourseClassId(Integer studentId, Integer courseClassId);
}
