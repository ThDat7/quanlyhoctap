package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.EducationProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EducationProgramRepository extends JpaRepository<EducationProgram, Integer> {
    Optional<EducationProgram> findByMajorName(String majorName);
}
