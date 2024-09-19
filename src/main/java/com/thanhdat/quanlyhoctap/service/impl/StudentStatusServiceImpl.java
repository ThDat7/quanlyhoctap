package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.SemesterDetailResponse;
import com.thanhdat.quanlyhoctap.dto.response.StudentStatusResponse;
import com.thanhdat.quanlyhoctap.entity.StudentStatus;
import com.thanhdat.quanlyhoctap.repository.StudentStatusRepository;
import com.thanhdat.quanlyhoctap.service.StudentService;
import com.thanhdat.quanlyhoctap.service.StudentStatusService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentStatusServiceImpl implements StudentStatusService {
    StudentStatusRepository studentStatusRepository;
    StudentService studentService;

    public List<StudentStatusResponse> getByCurrentStudent() {
        Integer currentStudentId = studentService.getCurrentStudent().getId();
        List<StudentStatus> studentStatuses =  studentStatusRepository.findByStudentId(currentStudentId);
        return mapToStudentStatusResponse(studentStatuses);
    }

    private List<StudentStatusResponse> mapToStudentStatusResponse(List<StudentStatus> studentStatuses){
        return studentStatuses.stream()
                        .map(studentStatus -> {
                            SemesterDetailResponse semester = SemesterDetailResponse.builder()
                                    .id(studentStatus.getSemester().getId())
                                    .year(String.format("%s-%s",
                                            studentStatus.getSemester().getYear(),
                                            studentStatus.getSemester().getYear() + 1))
                                    .semester(studentStatus.getSemester().getSemester())
                                    .startDate(studentStatus.getSemester().getStartDate())
                                    .durationWeeks(studentStatus.getSemester().getDurationWeeks())
                                    .build();

                            return StudentStatusResponse.builder()
                                            .semester(semester)
                                            .isLock(studentStatus.getIsLock())
                                            .build();
                        })
                .collect(Collectors.toList());
    }
}
