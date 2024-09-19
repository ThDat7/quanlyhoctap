package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.config.PaginationProperties;
import com.thanhdat.quanlyhoctap.dto.request.CourseOutlineEditTeacherRequest;
import com.thanhdat.quanlyhoctap.dto.request.CourseRuleRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.CourseOutline;
import com.thanhdat.quanlyhoctap.entity.CourseRule;
import com.thanhdat.quanlyhoctap.entity.OutlineStatus;
import com.thanhdat.quanlyhoctap.repository.CourseOutlineRepository;
import com.thanhdat.quanlyhoctap.service.CourseOutlineService;
import com.thanhdat.quanlyhoctap.service.FileUploadService;
import com.thanhdat.quanlyhoctap.service.TeacherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.thanhdat.quanlyhoctap.specification.CourseOutlineSpecification.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseOutlineServiceImpl implements CourseOutlineService {
    CourseOutlineRepository courseOutlineRepository;
    PaginationProperties paginationProperties;
    TeacherService teacherService;
    FileUploadService fileUploadService;

    @Override
    public DataWithCounterDto<CourseOutlineSearchDto> search(Map<String, String> params) {
        Specification<CourseOutline> specification = Specification.where(haveUrl());
        specification = specification.and(isPublished());

        if (params.containsKey("kw")) {
            Specification nameLike = courseNameLike(params.get("kw"));
            nameLike = nameLike.or(teacherNameLike(params.get("kw")));
            specification = specification.and(nameLike);
        }

        if (params.containsKey("credits"))
            specification = specification.and(courseCreditsEqual(Float.parseFloat(params.get("credits"))));

        if (params.containsKey("year"))
            specification = specification.and(belongsToEducationProgramBySchoolYear(Integer.parseInt(params.get("year"))));

        Integer page = 0;
        Integer pageSize = paginationProperties.getPageSize();

        if (params.containsKey("page"))
            page = Integer.parseInt(params.get("page")) - 1;

        Pageable paging = PageRequest.of(page, pageSize);

        Page<CourseOutline> pageCourseOutlines = courseOutlineRepository.findAll(specification, paging);
        List<CourseOutlineSearchDto> courseOutlinesDto = courseOutline2Dto(pageCourseOutlines.getContent());
        Integer total = (int) pageCourseOutlines.getTotalElements();
        return new DataWithCounterDto(courseOutlinesDto, total);
    }

    @Override
    public DataWithCounterDto<CourseOutlineTeacherResponse> getAllByCurrentTeacher(Map<String, String> params) {
        Integer currentTeacherId = teacherService.getCurrentTeacherId();

        Specification<CourseOutline> specification = Specification.where(belongsToTeacherId(currentTeacherId));

        Specification statusEqual = statusEqual(OutlineStatus.DOING);
        statusEqual = statusEqual.or(statusEqual(OutlineStatus.COMPLETED));
        specification = specification.and(statusEqual);

        Integer page = 0;
        Integer pageSize = paginationProperties.getPageSize();

        if (params.containsKey("page"))
            page = Integer.parseInt(params.get("page")) - 1;

        Pageable paging = PageRequest.of(page, pageSize);

        Page<CourseOutline> pageCourseOutlines = courseOutlineRepository.findAll(specification, paging);
        List<CourseOutlineTeacherResponse> courseOutlinesDto = mapToCourseOutlineTeacherResponse(pageCourseOutlines.getContent());
        Integer total = (int) pageCourseOutlines.getTotalElements();
        return new DataWithCounterDto(courseOutlinesDto, total);
    }

    @Override
    public CourseOutlineViewTeacherResponse getViewByCurrentTeacher(Integer id) {
        Integer currentTeacherId = teacherService.getCurrentTeacherId();
        Specification<CourseOutline> specification = Specification.where(belongsToTeacherId(currentTeacherId));

        Specification statusEqual = statusEqual(OutlineStatus.DOING);
        statusEqual = statusEqual.or(statusEqual(OutlineStatus.COMPLETED));
        specification = specification.and(statusEqual);

        specification = specification.and(idEqual(id));

        CourseOutline courseOutline = courseOutlineRepository.findOne(specification)
                .orElseThrow(() -> new RuntimeException("Course Outline not found"));

        return mapToCourseOutlineViewTeacherResponse(courseOutline);
    }

    @Override
    public void updateByCurrentTeacher(Integer id, MultipartFile file, CourseOutlineEditTeacherRequest request) {
        CourseRuleRequest courseRuleRequest = request.getCourseRule();
        validateTeacherCanUpDate(id, courseRuleRequest);

        CourseOutline courseOutline = courseOutlineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course Outline not found"));
        String folder = String.format("course-outline/%s/%s", courseOutline.getCourse().getCode(), courseOutline.getId());
        String url;

        try {
            url = fileUploadService.upload(file, folder);
        } catch (Exception e) {
            throw new RuntimeException("Course Outline upload failed");
        }

        CourseRule courseRule = courseOutline.getCourseRule();
        if (courseRule == null) {
            courseRule = new CourseRule();
            courseOutline.setCourseRule(courseRule);
        }

        courseRule.setMidTermFactor(courseRuleRequest.getMidTermFactor());
        courseRule.setFinalTermFactor(courseRuleRequest.getFinalTermFactor());
        courseRule.setPassScore(courseRuleRequest.getPassScore());

        courseOutline.setStatus(OutlineStatus.COMPLETED);
        courseOutline.setUrl(url);

        courseOutlineRepository.save(courseOutline);
    }

    private void validateTeacherCanUpDate(Integer id, CourseRuleRequest courseRuleRequest) {
        Integer currentTeacherId = teacherService.getCurrentTeacherId();
        Specification<CourseOutline> specification = Specification.where(belongsToTeacherId(currentTeacherId));

        Specification statusEqual = statusEqual(OutlineStatus.DOING);
        statusEqual = statusEqual.or(statusEqual(OutlineStatus.COMPLETED));
        specification = specification.and(statusEqual);

        specification = specification.and(idEqual(id));

        Boolean isCourseExist = courseOutlineRepository.exists(specification);

        if (!isCourseExist)
            throw new RuntimeException("You can't update this course outline");
        Float midtermExamFactor = courseRuleRequest.getMidTermFactor();
        Float finalExamFactor = courseRuleRequest.getFinalTermFactor();
        Float passScore = courseRuleRequest.getPassScore();

        Boolean isMidtermFactorValid = midtermExamFactor >= 0 && midtermExamFactor <= 0.5;
        Boolean isFinalTermFactorValid = finalExamFactor >= 0.5 && finalExamFactor <= 1;
        Boolean isTotalFactorValid = midtermExamFactor + finalExamFactor == 1;
        Boolean isPassScoreValid = passScore >= 4.0 && passScore <= 5.0;

        Boolean isCourseRuleValid = isMidtermFactorValid && isFinalTermFactorValid && isTotalFactorValid && isPassScoreValid;
        if (!isCourseRuleValid)
            throw new RuntimeException("Course rule is invalid");
    }

    private CourseOutlineViewTeacherResponse mapToCourseOutlineViewTeacherResponse(CourseOutline courseOutline) {
        CourseRuleResponse courseRuleResponse = mapToCourseRuleResponse(courseOutline.getCourseRule());
        return CourseOutlineViewTeacherResponse.builder()
                .id(courseOutline.getId())
                .courseName(courseOutline.getCourse().getName())
                .courseCode(courseOutline.getCourse().getCode())
                .url(courseOutline.getUrl())
                .courseRule(courseRuleResponse)
                .build();
    }

    private CourseRuleResponse mapToCourseRuleResponse(CourseRule courseRule) {
        return CourseRuleResponse.builder()
                .midTermFactor(courseRule.getMidTermFactor())
                .finalTermFactor(courseRule.getFinalTermFactor())
                .passScore(courseRule.getPassScore())
                .build();
    }

    private List<CourseOutlineTeacherResponse> mapToCourseOutlineTeacherResponse(List<CourseOutline> courseOutlines) {
        return courseOutlines.stream().map(co ->
                CourseOutlineTeacherResponse.builder()
                        .id(co.getId())
                        .courseName(co.getCourse().getName())
                        .courseCode(co.getCourse().getCode())
                        .deadline(co.getDeadline())
                        .status(co.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    private List<CourseOutlineSearchDto> courseOutline2Dto(List<CourseOutline> cos) {
        return cos.stream().map(co ->
                        CourseOutlineSearchDto.builder()
                                .id(co.getId())
                                .courseName(co.getCourse().getName())
                                .teacherName(String.format("%s %s",
                                        co.getTeacher().getUser().getLastName(),
                                        co.getTeacher().getUser().getFirstName()))
                                .courseCredits(co.getCourse().getCredits())
                                .years(co.getEducationProgramCourses().stream()
                                        .map(epc -> epc.getEducationProgram().getSchoolYear())
                                        .collect(Collectors.toList()))
                                .url(co.getUrl())
                                .build())
                .collect(Collectors.toList());
    }
}
