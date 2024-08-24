package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.ClassroomAvailableRequest;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.repository.ClassroomRepository;
import com.thanhdat.quanlyhoctap.repository.FinalExamRepository;
import com.thanhdat.quanlyhoctap.repository.ScheduleStudyRepository;
import com.thanhdat.quanlyhoctap.repository.SemesterRepository;
import com.thanhdat.quanlyhoctap.service.ClassroomService;
import com.thanhdat.quanlyhoctap.service.ScheduleStudyService;
import com.thanhdat.quanlyhoctap.util.DateTimeRange;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {
    private ClassroomRepository classroomRepository;
    private ScheduleStudyRepository scheduleStudyRepository;
    private FinalExamRepository finalExamRepository;
    private final ScheduleStudyService scheduleStudyService;
    private final SemesterRepository semesterRepository;

    public List<Classroom> getUnUsedClassrooms(ClassroomAvailableRequest classroomAvailableRequest) {
//        need to check all dates in one semester
        LocalDate sampleStartDate = classroomAvailableRequest.getTimeToUseClassroomRequests().get(0).getStartTime().toLocalDate();
        LocalDate sampleEndDate = classroomAvailableRequest.getTimeToUseClassroomRequests().get(0).getEndTime().toLocalDate();
        Semester semester = semesterRepository.findByDateRange(sampleStartDate, sampleEndDate);

        List<ScheduleStudy> schedules = scheduleStudyRepository.findBySemesterId(semester.getId());
        List<FinalExam> finalExams = finalExamRepository.findBySemesterId(semester.getId());
        RoomType roomType = classroomAvailableRequest.getRoomType();
        Boolean isAvailable = true;

        Set<Integer> usedClassroomIdsForSchedule = new HashSet<>();
        Set<Integer> usedClassroomIdsForExam= new HashSet<>();

        classroomAvailableRequest.getTimeToUseClassroomRequests().stream()
                .forEach(timeToUseClassroomRequest -> {
                    DateTimeRange toUseDateTimeRange = DateTimeRange.builder()
                            .start(timeToUseClassroomRequest.getStartTime())
                            .end(timeToUseClassroomRequest.getEndTime())
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
                            .map(FinalExam::getClassroom)
                            .map(Classroom::getId)
                            .collect(Collectors.toList()));
                });

        Set<Integer> usedClassroomIds = new HashSet<>();
        usedClassroomIds.addAll(usedClassroomIdsForSchedule);
        usedClassroomIds.addAll(usedClassroomIdsForExam);

        return classroomRepository.findByRoomTypeAndIdNotIn(roomType, usedClassroomIds);
    }

    private Boolean isScheduleTimeConflict(DateTimeRange toUseDateTimeRange, ScheduleStudy schedule) {
        List<DateTimeRange> scheduleDateTimeRanges = scheduleStudyService.convertToDateTimeRanges(schedule);
        return scheduleDateTimeRanges.stream()
                .anyMatch(toUseDateTimeRange::overlaps);
    }

    private Boolean isTimeConflict(DateTimeRange dateTimeRange,
                                   LocalDateTime startTime, LocalDateTime endTime) {
        Boolean isStartTimeConflict = dateTimeRange.isWithinRange(startTime);
        Boolean isEndTimeConflict = dateTimeRange.isWithinRange(endTime);
        return isStartTimeConflict || isEndTimeConflict;
    }
}
