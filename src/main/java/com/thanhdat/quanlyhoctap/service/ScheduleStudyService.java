package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.entity.ScheduleStudy;
import com.thanhdat.quanlyhoctap.util.DateTimeRange;

import java.util.List;

public interface ScheduleStudyService {
    List<DateTimeRange> convertToDateTimeRanges(ScheduleStudy scheduleStudies);
}
