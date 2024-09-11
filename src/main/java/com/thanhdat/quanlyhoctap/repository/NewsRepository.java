package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
}
