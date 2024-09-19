package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<Major> findByName(String name);

    @Query("SELECT sc.major FROM StudentClass sc " +
            "LEFT JOIN sc.students st " +
            "WHERE st.id = :studentId")
    Major getByStudentId(Long studentId);
}
