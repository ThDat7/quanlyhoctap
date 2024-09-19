package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.EducationProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EducationProgramRepository extends JpaRepository<EducationProgram, Long>,
            JpaSpecificationExecutor<EducationProgram> {
    Optional<EducationProgram> findByMajorName(String majorName);

    @Modifying
    @Query(value="INSERT INTO education_programs (major_id, school_year) " +
            "SELECT major_id, :toYear FROM education_programs " +
            "WHERE school_year = :fromYear", nativeQuery = true)
    Integer cloneBatching(int fromYear, int toYear);

    Integer countBySchoolYear(int toYear);
}
