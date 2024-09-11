package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
}
