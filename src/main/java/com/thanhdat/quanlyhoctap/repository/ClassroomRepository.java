package com.thanhdat.quanlyhoctap.repository;

import com.thanhdat.quanlyhoctap.entity.Classroom;
import com.thanhdat.quanlyhoctap.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
//    @Query("SELECT c FROM Classroom c WHERE c.roomType = :roomType AND c.isAvailable = true AND " +
//            "c.id NOT IN (" +
//            "SELECT s.classroom.id FROM ScheduleStudy s " +
//            "WHERE s.dayInWeek = :dayInWeek " +
//            "AND ((s.startDate <= :endDate AND (s.startDate + (s.weekLength - 1) * 7) >= :startDate) OR " +
//            "((:startDate <= (s.startDate + (s.weekLength - 1) * 7) AND :endDate >= s.startDate))) " +
//            "AND ((s.shiftStart < :shiftEnd AND (s.shiftStart + s.shiftLength) > :shiftStart)) OR " +
//            "((:shiftStart < (s.shiftStart + s.shiftLength) AND :shiftEnd > s.shiftStart)))")
//@Query(value = "SELECT c FROM Classroom c WHERE c.roomType = :roomType AND c.isAvailable = true AND " +
//        "c.id NOT IN (" +
//        "SELECT s.classroom.id FROM ScheduleStudy s " +
//        "WHERE s.dayInWeek = :dayInWeek " +
//        "AND ((s.startDate <= :endDate AND DATE_ADD(s.startDate, INTERVAL (s.weekLength - 1) * 7 DAY) >= :startDate) OR " +
//        "(:startDate <= DATE_ADD(s.startDate, INTERVAL (s.weekLength - 1) * 7 DAY) AND :endDate >= s.startDate)) " +
//        "AND (s.shiftStart < :shiftEnd AND s.shiftStart + s.shiftLength > :shiftStart))",
//        nativeQuery = true)
@Query("SELECT c FROM Classroom c WHERE c.roomType = :roomType AND c.isAvailable = true AND " +
        "c.id NOT IN (" +
        "SELECT s.classroom.id FROM ScheduleStudy s " +
        "WHERE s.dayInWeek = :dayInWeek AND " +
        "( " +
            "(s.startDate <= :endDate AND (s.startDate + ((s.weekLength - 1) * 7) day) >= :startDate) OR " +
            "(:startDate <= (s.startDate + ((s.weekLength - 1) * 7) day) AND :endDate >= s.startDate) " +
        ") AND " +
        "( " +
            "(s.shiftStart < :shiftEnd AND (s.shiftStart + s.shiftLength) > :shiftStart) OR " +
            "(:shiftStart < (s.shiftStart + s.shiftLength) AND :shiftEnd > s.shiftStart) " +
        ")" +
        ")")
List<Classroom> findAvailableClassrooms(@Param("roomType") RoomType roomType,
                                            @Param("dayInWeek") Integer dayInWeek,
                                            @Param("startDate") Date startDate,
                                            @Param("endDate") Date endDate,
                                            @Param("shiftStart") Integer shiftStart,
                                            @Param("shiftEnd") Integer shiftEnd);
}
