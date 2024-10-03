package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.config.DateTimeFormatters;
import com.thanhdat.quanlyhoctap.entity.Exam;
import com.thanhdat.quanlyhoctap.entity.ScheduleStudy;
import com.thanhdat.quanlyhoctap.entity.Setting;
import com.thanhdat.quanlyhoctap.exception.code.ErrorCode;
import com.thanhdat.quanlyhoctap.exception.type.AppException;
import com.thanhdat.quanlyhoctap.helper.settingbag.SettingBag;
import com.thanhdat.quanlyhoctap.helper.settingbag.StudySettingType;
import com.thanhdat.quanlyhoctap.repository.SettingRepository;
import com.thanhdat.quanlyhoctap.service.ScheduleStudyService;
import com.thanhdat.quanlyhoctap.util.DateRange;
import com.thanhdat.quanlyhoctap.util.DateTimeRange;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduleStudyServiceImpl implements ScheduleStudyService {
    SettingRepository settingRepository;

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
            LocalTime calculatedStartTime = timeStartStudy.plusMinutes((schedule.getShiftStart() - 1) * shiftLengthMinutes);
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

    @Override
    public List<DateTimeRange> getFreeTimeRanges(List<DateTimeRange> busyTimeRanges,
                                                 DateRange toUseTimeRange) {
        List<DateTimeRange> availableTime = new ArrayList<>();

        LocalTime timeStartStudy = getStartTimeStudy();
        LocalTime timeEndStudy = getEndTimeStudy();

        LocalDate currentDay = toUseTimeRange.getStart();
        LocalDate lastDay = toUseTimeRange.getEnd();

        busyTimeRanges.sort(Comparator.comparing(DateTimeRange::getStart));

        while (!currentDay.isAfter(lastDay)) {
            LocalDateTime studyStart = currentDay.atTime(timeStartStudy);
            LocalDateTime studyEnd = currentDay.atTime(timeEndStudy);
            LocalDateTime currentStart = studyStart;

            for (DateTimeRange busyTime : busyTimeRanges) {
                LocalDateTime busyStart = busyTime.getStart();
                LocalDateTime busyEnd = busyTime.getEnd();

                if (busyStart.toLocalDate().isAfter(currentDay) || busyEnd.toLocalDate().isBefore(currentDay)) {
                    continue;
                }

                busyStart = busyStart.isBefore(studyStart) ? studyStart : busyStart;
                busyEnd = busyEnd.isAfter(studyEnd) ? studyEnd : busyEnd;

                if (currentStart.isBefore(busyStart)) {
                    availableTime.add(DateTimeRange.builder()
                            .start(currentStart)
                            .end(busyStart)
                            .build());
                }

                currentStart = busyEnd.isAfter(currentStart) ? busyEnd : currentStart;
            }

            if (currentStart.isBefore(studyEnd)) {
                availableTime.add(DateTimeRange.builder()
                        .start(currentStart)
                        .end(studyEnd)
                        .build());
            }

            currentDay = currentDay.plusDays(1);
        }

        return availableTime;
    }


    @Override
    public List<DateTimeRange> convertToDateTimeRanges(List<ScheduleStudy> scheduleStudies, List<Exam> exams) {
        List<DateTimeRange> busyTime = new ArrayList<>();
        List<DateTimeRange> scheduleDateTimeRanges = scheduleStudies.stream()
                .flatMap(scheduleStudy -> convertToDateTimeRanges(scheduleStudy).stream())
                .toList();

        List<DateTimeRange> examDateTimeRanges = exams.stream()
                .map(exam -> DateTimeRange.builder()
                        .start(exam.getStartTime())
                        .end(exam.getEndTime())
                        .build())
                .toList();

        busyTime.addAll(scheduleDateTimeRanges);
        busyTime.addAll(examDateTimeRanges);
        return busyTime;
    }

    private LocalTime getStartTimeStudy() {
        List<Setting> settings = settingRepository.findAll();
        SettingBag settingBag = new SettingBag(settings);
        String timeStartStudyString = settingBag.getValue(StudySettingType.TIME_START_STUDY.name());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DateTimeFormatters.TIME_FORMAT);
        return LocalTime.parse(timeStartStudyString, timeFormatter);
    }

    private LocalTime getEndTimeStudy() {
        List<Setting> settings = settingRepository.findAll();
        SettingBag settingBag = new SettingBag(settings);
        String endShiftOfDayString = settingBag.getValue(StudySettingType.END_SHIFT_OF_DAY.name());
        String shiftLengthMinutesString = settingBag.getValue(StudySettingType.SHIFT_LENGTH_MINUTES.name());

        Integer endShiftOfDay = Integer.parseInt(endShiftOfDayString);
        Integer shiftLengthMinutes = Integer.parseInt(shiftLengthMinutesString);
        LocalTime timeStartStudy = getStartTimeStudy();
        int minutesStudy = endShiftOfDay * shiftLengthMinutes;
        return timeStartStudy.plusMinutes(minutesStudy);
    }

    @Override
    public void validateTimeRangeIsNotBusy(List<DateTimeRange> busyTimeRanges,
                                            DateTimeRange toUse) {
        for (DateTimeRange busyTime : busyTimeRanges) {
            if (toUse.overlaps(busyTime)) {
                throw new AppException(ErrorCode.TIME_IN_USED);
            }
        }
    }
}
