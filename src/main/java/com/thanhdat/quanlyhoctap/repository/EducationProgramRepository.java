package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.EducationProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EducationProgramRepository extends JpaRepository<EducationProgram, Integer>,
            JpaSpecificationExecutor<EducationProgram> {
    Optional<EducationProgram> findByMajorName(String majorName);
}
