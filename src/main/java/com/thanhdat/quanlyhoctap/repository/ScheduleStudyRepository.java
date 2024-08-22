package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.ScheduleStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleStudyRepository extends JpaRepository<ScheduleStudy, Integer> {
    List<ScheduleStudy> findByCourseClassSemesterIdAndCourseClassStudentClassId(Integer courseClassSemesterId, Integer courseClassStudentClassId);
}
