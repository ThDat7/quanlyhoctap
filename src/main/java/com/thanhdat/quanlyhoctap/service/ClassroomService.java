package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.ClassroomAvailableRequest;
import com.thanhdat.quanlyhoctap.entity.Classroom;
import com.thanhdat.quanlyhoctap.entity.RoomType;
import com.thanhdat.quanlyhoctap.util.DateTimeRange;

import java.util.List;

public interface ClassroomService {
    List<Classroom> getUnUsedClassrooms(List<DateTimeRange> timeToUse, RoomType roomType);
    List<Classroom> getUnUsedClassrooms(List<DateTimeRange> timeToUseClassrooms);
}
