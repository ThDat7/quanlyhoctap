package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.InvoiceResponse;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.repository.InvoiceRepository;
import com.thanhdat.quanlyhoctap.repository.MajorRepository;
import com.thanhdat.quanlyhoctap.repository.StudyRepository;
import com.thanhdat.quanlyhoctap.service.SettingService;
import com.thanhdat.quanlyhoctap.service.StudentService;
import com.thanhdat.quanlyhoctap.service.InvoiceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final StudyRepository studyRepository;
    private final StudentService studentService;
    private final MajorRepository majorRepository;
    private final InvoiceRepository invoiceRepository;
    private final SettingService settingService;
    public List<InvoiceResponse> getByCurrentStudentAndSemester(Integer semesterId) {
        Integer semesterIdForRegister = settingService.getSemesterIdForRegister();
        // need more check for register is open to return temporary invoice
        Boolean isMatchSemesterRegister = semesterId.equals(semesterIdForRegister);
        if (isMatchSemesterRegister)
            return getTemporaryInvoice(semesterId);

        Integer currentStudentId = studentService.getCurrentStudent().getId();
        Invoice invoice = invoiceRepository
                .findByStudentIdAndSemesterId(currentStudentId, semesterId);
        Major major = majorRepository.getByStudentId(currentStudentId);
        Integer specializeTuition = major.getSpecializeTuition();
        Integer generalTuition = major.getGeneralTuition();
        List<Course> courses = invoice.getInvoiceDetails().stream()
                .map(InvoiceDetail::getCourseClass)
                .map(CourseClass::getCourse)
                .collect(Collectors.toList());
        return mapToInvoiceResponse(courses, specializeTuition, generalTuition);
    }

    private List<InvoiceResponse> getTemporaryInvoice(Integer semesterId) {
        Integer currentStudentId = studentService.getCurrentStudent().getId();
        List<Study> studies = studyRepository
                .findByStudentIdAndSemesterId(currentStudentId, semesterId);
        Major major = majorRepository.getByStudentId(currentStudentId);
        Integer specializeTuition = major.getSpecializeTuition();
        Integer generalTuition = major.getGeneralTuition();
        List<Course> courses = studies.stream()
                .map(Study::getCourseClass)
                .map(CourseClass::getCourse)
                .collect(Collectors.toList());
        return mapToInvoiceResponse(courses, specializeTuition, generalTuition);
    }

    private List<InvoiceResponse> mapToInvoiceResponse(List<Course> courses,
                                                       Integer specializeTuition,
                                                       Integer generalTuition) {
        return courses.stream()
                .map(course -> {
                    CourseType courseType = course.getType();
                    Integer tuitionPerCredit = courseType == CourseType.SPECIALIZE ? specializeTuition : generalTuition;
                    Integer tuition = Math.round(tuitionPerCredit * course.getCredits());
                    return InvoiceResponse.builder()
                            .courseName(course.getName())
                            .courseCode(course.getCode())
                            .courseCredits(course.getCredits())
                            .tuition(tuition)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
