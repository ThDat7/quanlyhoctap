package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.entity.Exam;
import com.thanhdat.quanlyhoctap.entity.ScheduleStudy;
import com.thanhdat.quanlyhoctap.util.DateRange;
import com.thanhdat.quanlyhoctap.util.DateTimeRange;

import java.util.List;

public interface ScheduleStudyService {
    List<DateTimeRange> convertToDateTimeRanges(ScheduleStudy scheduleStudies);
    List<DateTimeRange> convertToDateTimeRanges(List<ScheduleStudy> scheduleStudies, List<Exam> exams);
    List<DateTimeRange> getFreeTimeRanges(List<DateTimeRange> busyTimeRanges,
                                          DateRange toUseTimeRange);
    void validateTimeRangeIsNotBusy(List<DateTimeRange> busyTimeRanges, DateTimeRange toUse);
}
