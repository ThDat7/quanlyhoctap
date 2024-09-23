package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.InvoiceResponse;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.exception.code.ErrorCode;
import com.thanhdat.quanlyhoctap.exception.type.AppException;
import com.thanhdat.quanlyhoctap.mapper.InvoiceMapper;
import com.thanhdat.quanlyhoctap.repository.InvoiceRepository;
import com.thanhdat.quanlyhoctap.repository.MajorRepository;
import com.thanhdat.quanlyhoctap.repository.StudyRepository;
import com.thanhdat.quanlyhoctap.service.SettingService;
import com.thanhdat.quanlyhoctap.service.StudentService;
import com.thanhdat.quanlyhoctap.service.InvoiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceServiceImpl implements InvoiceService {
    StudyRepository studyRepository;
    MajorRepository majorRepository;
    InvoiceRepository invoiceRepository;

    StudentService studentService;
    SettingService settingService;

    private final InvoiceMapper invoiceMapper;

    public List<InvoiceResponse> getByCurrentStudentAndSemester(Long semesterId) {
        Long semesterIdForRegister = settingService.getSemesterIdForRegister();

        Boolean isMatchSemesterRegister = semesterId.equals(semesterIdForRegister);
        if (isMatchSemesterRegister)
            return getTemporaryInvoice(semesterId);

        Long currentStudentId = studentService.getCurrentStudent().getId();
        Optional<Invoice> opInvoice = invoiceRepository
                .findByStudentIdAndSemesterId(currentStudentId, semesterId);

        if (opInvoice.isEmpty())
            return List.of();

        Major major = majorRepository.getByStudentId(currentStudentId)
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));

        Integer specializeTuition = major.getSpecializeTuition();
        Integer generalTuition = major.getGeneralTuition();
        List<Course> courses = opInvoice.get().getInvoiceDetails().stream()
                .map(InvoiceDetail::getCourseClass)
                .map(CourseClass::getCourse)
                .collect(Collectors.toList());
        return tuitionCalculator(courses, specializeTuition, generalTuition);
    }

    private List<InvoiceResponse> getTemporaryInvoice(Long semesterId) {
        Long currentStudentId = studentService.getCurrentStudent().getId();
        List<Study> studies = studyRepository
                .findByStudentIdAndSemesterId(currentStudentId, semesterId);
        Major major = majorRepository.getByStudentId(currentStudentId)
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));

        Integer specializeTuition = major.getSpecializeTuition();
        Integer generalTuition = major.getGeneralTuition();
        List<Course> courses = studies.stream()
                .map(Study::getCourseClass)
                .map(CourseClass::getCourse)
                .collect(Collectors.toList());
        return tuitionCalculator(courses, specializeTuition, generalTuition);
    }

    private List<InvoiceResponse> tuitionCalculator(List<Course> courses,
                                                    Integer specializeTuition,
                                                    Integer generalTuition) {
        return courses.stream()
                .map(course -> {
                    CourseType courseType = course.getType();
                    Integer tuitionPerCredit = courseType == CourseType.SPECIALIZE ? specializeTuition : generalTuition;
                    Integer tuition = Math.round(tuitionPerCredit * course.getCredits());
                    return invoiceMapper.toInvoiceResponse(course, tuition);
                })
                .collect(Collectors.toList());
    }
}
