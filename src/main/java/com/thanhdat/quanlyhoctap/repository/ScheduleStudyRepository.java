package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.RoomType;
import com.thanhdat.quanlyhoctap.entity.ScheduleStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleStudyRepository extends JpaRepository<ScheduleStudy, Long> {
    List<ScheduleStudy> findByCourseClassSemesterIdAndCourseClassStudentClassId(Long courseClassSemesterId, Long courseClassStudentClassId);

    @Query("SELECT s FROM ScheduleStudy s WHERE s.courseClass.semester.id = :semesterId")
    List<ScheduleStudy> findBySemesterId(Long semesterId);

    List<ScheduleStudy> findByCourseClassId(Long courseClassId);

    List<ScheduleStudy> findAllByCourseClassIdIn(List<Long> courseClassIds);
}