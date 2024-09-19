package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.CourseOutline;
import com.thanhdat.quanlyhoctap.entity.OutlineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseOutlineRepository
        extends JpaRepository<CourseOutline, Long>,
        JpaSpecificationExecutor<CourseOutline> {
    List<CourseOutline> findByTeacherIdAndStatus(Long teacherId, OutlineStatus status);
}
