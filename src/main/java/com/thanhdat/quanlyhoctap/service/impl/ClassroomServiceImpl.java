package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.ClassroomAvailableRequest;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.repository.ClassroomRepository;
import com.thanhdat.quanlyhoctap.repository.ExamRepository;
import com.thanhdat.quanlyhoctap.repository.ScheduleStudyRepository;
import com.thanhdat.quanlyhoctap.repository.SemesterRepository;
import com.thanhdat.quanlyhoctap.service.ClassroomService;
import com.thanhdat.quanlyhoctap.service.ScheduleStudyService;
import com.thanhdat.quanlyhoctap.util.DateTimeRange;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassroomServiceImpl implements ClassroomService {
    ClassroomRepository classroomRepository;
    ScheduleStudyRepository scheduleStudyRepository;
    ExamRepository examRepository;
    ScheduleStudyService scheduleStudyService;
    SemesterRepository semesterRepository;

    @Override
    public List<Classroom> getUnUsedClassrooms(List<DateTimeRange> timeToUse, RoomType roomType) {
        Set<Long> usedClassroomIds = getUnUsedClassroomsIds(timeToUse);
        return classroomRepository.findByRoomTypeAndIdNotIn(roomType, usedClassroomIds);
    }

    private Set<Long> getUnUsedClassroomsIds(List<DateTimeRange> timeToUseClassrooms) {
        LocalDate sampleStartDate = timeToUseClassrooms.get(0).getStart().toLocalDate();
        LocalDate sampleEndDate = timeToUseClassrooms.get(0).getEnd().toLocalDate();
        Semester semester = semesterRepository.findByDateRange(sampleStartDate, sampleEndDate)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Semester not found for date range: " + "start: %s, end: %s",
                                sampleStartDate, sampleEndDate)));

        List<ScheduleStudy> schedules = scheduleStudyRepository.findBySemesterId(semester.getId());
        List<Exam> finalExams = examRepository.findBySemesterIdAndType(semester.getId(), ExamType.FINAL);

        Set<Long> usedClassroomIdsForSchedule = new HashSet<>();
        Set<Long> usedClassroomIdsForExam= new HashSet<>();

        timeToUseClassrooms.stream()
                .forEach(timeToUse -> {
                    DateTimeRange toUseDateTimeRange = DateTimeRange.builder()
                            .start(timeToUse.getStart())
                            .end(timeToUse.getEnd())
                            .build();

                    usedClassroomIdsForSchedule.addAll(
                            schedules.stream()
                                    .filter(scheduleStudy ->
                                            isScheduleTimeConflict(toUseDateTimeRange, scheduleStudy))
                                    .map(ScheduleStudy::getClassroom)
                                    .map(Classroom::getId)
                                    .collect(Collectors.toList()));

                    usedClassroomIdsForExam.addAll(
                            finalExams.stream()
                                    .filter(finalExam -> {
                                        DateTimeRange examDateTimeRange = DateTimeRange.builder()
                                                .start(finalExam.getStartTime())
                                                .end(finalExam.getEndTime())
                                                .build();
                                        return examDateTimeRange.overlaps(toUseDateTimeRange);
                                    })
                                    .map(Exam::getClassroom)
                                    .map(Classroom::getId)
                                    .collect(Collectors.toList()));
                });

        Set<Long> usedClassroomIds = new HashSet<>();
        usedClassroomIds.addAll(usedClassroomIdsForSchedule);
        usedClassroomIds.addAll(usedClassroomIdsForExam);
        return usedClassroomIds;
    }

    public List<Classroom> getUnUsedClassrooms(List<DateTimeRange> timeToUseClassrooms) {
        Set<Long> usedClassroomIds = getUnUsedClassroomsIds(timeToUseClassrooms);
        return classroomRepository.findAllByIdNotIn(usedClassroomIds);
    }

    private Boolean isScheduleTimeConflict(DateTimeRange toUseDateTimeRange, ScheduleStudy schedule) {
        List<DateTimeRange> scheduleDateTimeRanges = scheduleStudyService.convertToDateTimeRanges(schedule);
        return scheduleDateTimeRanges.stream()
//                should move overlaps, isWithinRange to logic, DateTimeRange only for data
                .anyMatch(toUseDateTimeRange::overlaps);
    }

}
