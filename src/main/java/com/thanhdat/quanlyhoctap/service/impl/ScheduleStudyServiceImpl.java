package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.config.DateTimeFormatters;
import com.thanhdat.quanlyhoctap.entity.ScheduleStudy;
import com.thanhdat.quanlyhoctap.entity.Setting;
import com.thanhdat.quanlyhoctap.helper.settingbag.SettingBag;
import com.thanhdat.quanlyhoctap.helper.settingbag.StudySettingType;
import com.thanhdat.quanlyhoctap.repository.SettingRepository;
import com.thanhdat.quanlyhoctap.service.ScheduleStudyService;
import com.thanhdat.quanlyhoctap.util.DateTimeRange;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleStudyServiceImpl implements ScheduleStudyService {
    private SettingRepository settingRepository;

    @Override
    public List<DateTimeRange> convertToDateTimeRanges(ScheduleStudy schedule) {
        List<Setting> settings = settingRepository.findAll();
        SettingBag settingBag = new SettingBag(settings);
        String timeStartStudyString = settingBag.getValue(StudySettingType.TIME_START_STUDY.name());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DateTimeFormatters.TIME_FORMAT);
        LocalTime timeStartStudy = LocalTime.parse(timeStartStudyString, timeFormatter);

        String shiftLengthMinutesString = settingBag.getValue(StudySettingType.SHIFT_LENGTH_MINUTES.name());
        Integer shiftLengthMinutes = Integer.parseInt(shiftLengthMinutesString);

        List<DateTimeRange> scheduleDateTimeRanges = new ArrayList<>();
        for (int week = 0; week < schedule.getWeekLength(); week++) {
            LocalDate currentStartDate = schedule.getStartDate().plusWeeks(week);
            LocalTime calculatedStartTime = timeStartStudy.plusMinutes(schedule.getShiftStart() * shiftLengthMinutes);
            Integer shiftEnd = schedule.getShiftStart() + schedule.getShiftLength();
            LocalTime calculatedEndTime = timeStartStudy.plusMinutes(shiftEnd * shiftLengthMinutes);

            LocalDateTime scheduleStartTime = LocalDateTime.of(currentStartDate, calculatedStartTime);
            LocalDateTime scheduleEndTime = LocalDateTime.of(currentStartDate, calculatedEndTime);
            scheduleDateTimeRanges.add(DateTimeRange.builder()
                    .start(scheduleStartTime)
                    .end(scheduleEndTime)
                    .build());
        }
        return scheduleDateTimeRanges;
    }
}
