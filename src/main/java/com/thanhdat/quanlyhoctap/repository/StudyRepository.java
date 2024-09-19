package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long>,
        JpaSpecificationExecutor<Study> {
    List<Study> findByStudentId(Long studentId);
    @Query("SELECT s FROM Study s WHERE s.student.id = :studentId AND s.courseClass.semester.id = :semesterId")
    List<Study> findByStudentIdAndSemesterId(Long studentId, Long semesterId);

    Boolean existsByStudentIdAndCourseClassId(Long studentId, Long courseClassId);

    void deleteByStudentIdAndCourseClassId(Long studentId, Long courseClassId);

    List<Study> findByCourseClassId(Long courseClassId);
}
