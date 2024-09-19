package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.Classroom;
import com.thanhdat.quanlyhoctap.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    @Query("SELECT c FROM Classroom c WHERE c.roomType = :roomType AND c.isAvailable = true AND c.id NOT IN :ids")
    List<Classroom> findByRoomTypeAndIdNotIn(RoomType roomType, Collection<Long> ids);
}
