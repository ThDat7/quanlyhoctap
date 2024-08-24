package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.ClassroomAvailableRequest;
import com.thanhdat.quanlyhoctap.entity.Classroom;

import java.util.List;

public interface ClassroomService {
    List<Classroom> getUnUsedClassrooms(ClassroomAvailableRequest classroomAvailableRequest);
}
