package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.ScheduleStudyCourseRegisterResponse;
import com.thanhdat.quanlyhoctap.dto.response.ScheduleStudyTimeTableResponse;
import com.thanhdat.quanlyhoctap.entity.ScheduleStudy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ScheduleStudyMapper {
    @Mapping(source = "classroom.name", target = "roomName")
    ScheduleStudyCourseRegisterResponse toScheduleStudyCourseRegisterResponse(ScheduleStudy scheduleStudy);

    @Mapping(source = "classroom.roomType", target = "roomType")
    @Mapping(source = "classroom.name", target = "roomName")
    @Mapping(source = "classroom.id", target = "roomId")
    ScheduleStudyTimeTableResponse toScheduleStudyTimeTableResponse(ScheduleStudy scheduleStudy);
}
