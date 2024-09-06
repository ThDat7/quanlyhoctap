package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.CourseOutline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseOutlineRepository
        extends JpaRepository<CourseOutline, Integer>,
        JpaSpecificationExecutor<CourseOutline> {
}
