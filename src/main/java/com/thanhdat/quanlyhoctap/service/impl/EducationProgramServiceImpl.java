package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.EducationProgramCourseRequest;
import com.thanhdat.quanlyhoctap.dto.request.EducationProgramCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.repository.*;
import com.thanhdat.quanlyhoctap.service.EducationProgramService;
import com.thanhdat.quanlyhoctap.util.PagingHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.thanhdat.quanlyhoctap.specification.EducationProgramSpecification.nameLike;
import static com.thanhdat.quanlyhoctap.specification.EducationProgramSpecification.schoolYearEqual;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EducationProgramServiceImpl implements EducationProgramService {
    EducationProgramRepository educationProgramRepository;
    PagingHelper pagingHelper;
    EducationProgramCourseRepository educationProgramCourseRepository;
    MajorRepository majorRepository;
    SemesterRepository semesterRepository;
    CourseRepository courseRepository;

    public DataWithCounterDto search(Map<String, String> params) {
        Specification<EducationProgram> specification = Specification.where(null);
        if (params.containsKey("kw"))
            specification = specification.and(nameLike(params.get("kw")));

        if (params.containsKey("year"))
            specification = specification.and(schoolYearEqual(Integer.parseInt(params.get("year"))));

        Pageable paging = pagingHelper.getPageable(params);

        Page<EducationProgram> pageEducationPrograms = educationProgramRepository.findAll(specification, paging);
        List<EducationProgramSearchDto> educationProgramsDto = educationProgram2Dto(pageEducationPrograms.getContent());
        long total = pageEducationPrograms.getTotalElements();
        return new DataWithCounterDto<>(educationProgramsDto, total);
    }

    @Override
    public EducationProgramViewDto getView(Long id) {
        Optional<EducationProgram> oEP =  educationProgramRepository.findById(id);
        if (oEP.isEmpty())
            throw new RuntimeException("Education program not found");

        EducationProgram ep = oEP.get();
        return educationProgram2ViewDto(ep);
    }

    @Override
    @Transactional
    public EducationProgramCloneBatchingResponse cloneBatching(int fromYear, int toYear) {
        validateCanCloneBatching(fromYear, toYear);
        Integer recordAdded = educationProgramRepository.cloneBatching(fromYear, toYear);
        createSemestersForNewEP(toYear);
        educationProgramCourseRepository.cloneBatching(fromYear, toYear);
        return EducationProgramCloneBatchingResponse.builder()
                .totalCloned(recordAdded)
                .build();
    }

    private void createSemestersForNewEP(int year) {
        Integer maxYearInEP = 5;
        Integer maxYear = year + maxYearInEP;
        List<Semester> semesters = semesterRepository.findByYearBetween(year, maxYear);
        List<Integer> newSemesters = List.of(1, 2, 3);
        List<Semester> createSemesters = new ArrayList<>();
        for (int i = year; i <= maxYear; i++) {
            for (Integer semester : newSemesters) {
                int fixYear = i;
                Boolean isExist = semesters.stream()
                        .anyMatch(s -> s.getSemester().equals(semester) && s.getYear().equals(fixYear));
                if (!isExist) {
                    Semester newSemester = Semester.builder()
                            .semester(semester)
                            .year(fixYear)
                            .build();
                    createSemesters.add(newSemester);
                }
            }
        }

        semesterRepository.saveAll(createSemesters);
    }

    @Override
    public DataWithCounterDto<EducationProgramCrudResponse> getAll(Map<String, String> params) {
        Pageable paging = pagingHelper.getPageable(params);
        Page<EducationProgram> page = educationProgramRepository.findAll(paging);
        List<EducationProgramCrudResponse> dto = page.getContent().stream()
                .map(this::mapToEducationProgramCrudResponse)
                .collect(Collectors.toList());
        long total = page.getTotalElements();
        return new DataWithCounterDto<>(dto, total);
    }

    @Override
    @Transactional
    public void create(EducationProgramCrudRequest createRequest) {
        Major major = majorRepository.findById(createRequest.getMajorId())
                .orElseThrow(() -> new RuntimeException("Major not found"));
        EducationProgram ep = EducationProgram.builder()
                .major(major)
                .schoolYear(createRequest.getSchoolYear())
                .build();
        if (createRequest.getEducationProgramCourses() != null) {
            List<EducationProgramCourse> epcs = createRequest.getEducationProgramCourses().stream()
                    .map(epcRequest -> mapToEducationProgramCourse(epcRequest, ep))
                    .collect(Collectors.toList());
            ep.setEducationProgramCourses(epcs);
        }
        educationProgramRepository.save(ep);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        educationProgramRepository.deleteById(id);
    }

    @Override
    public EducationProgramViewCrudResponse getById(Long id) {
        EducationProgram educationProgram = educationProgramRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Education program not found"));
        return mapToEducationProgramViewCrudResponse(educationProgram);
    }

    private EducationProgramCourse mapToEducationProgramCourse(EducationProgramCourseRequest epcRequest, EducationProgram ep) {
        Integer semesterInYear = calculateSemesterInYear(epcRequest.getSemester());
        Integer yearFromEP = calculateYearFromEP(epcRequest.getSemester());
        Integer realYear = ep.getSchoolYear() + yearFromEP;
        Semester semester = semesterRepository.findBySemesterAndYear(semesterInYear, realYear)
                .orElseThrow(() -> new RuntimeException("Semester not found"));
        Course course = courseRepository.findById(epcRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return EducationProgramCourse.builder()
                .semester(semester)
                .course(course)
                .educationProgram(ep)
                .build();
    }

    private EducationProgramViewCrudResponse mapToEducationProgramViewCrudResponse(EducationProgram ep) {
        List<EducationProgramCourseResponse> epcs = ep.getEducationProgramCourses().stream()
                .map(epc -> mapToEducationProgramCourseResponse(ep, epc))
                .collect(Collectors.toList());

        return EducationProgramViewCrudResponse.builder()
                .id(ep.getId())
                .majorId(ep.getMajor().getId())
                .schoolYear(ep.getSchoolYear())
                .educationProgramCourses(epcs)
                .build();
    }

    @Override
    @Transactional
    public void update(Long id, EducationProgramCrudRequest updateRequest) {
        EducationProgram ep = educationProgramRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Education program not found"));
        Boolean isMajorChanged = !ep.getMajor().getId().equals(updateRequest.getMajorId());
        if (isMajorChanged) {
            Major major = majorRepository.findById(updateRequest.getMajorId())
                    .orElseThrow(() -> new RuntimeException("Major not found"));
            ep.setMajor(major);
        }
        ep.setSchoolYear(updateRequest.getSchoolYear());
        updateEducationProgramCourses(ep, updateRequest.getEducationProgramCourses());
        educationProgramRepository.save(ep);
    }

    private void updateEducationProgramCourses(EducationProgram ep, List<EducationProgramCourseRequest> epcs) {
        List<Long> epcIds = epcs.stream().map(EducationProgramCourseRequest::getId).collect(Collectors.toList());
        List<EducationProgramCourse> epcsInDb = ep.getEducationProgramCourses();
        List<Long> epcIdsInDb = epcsInDb.stream().map(epc -> epc.getId()).collect(Collectors.toList());
        List<Long> epcIdsToDelete = epcIdsInDb.stream().filter(epcId -> !epcIds.contains(epcId)).collect(Collectors.toList());

        List<EducationProgramCourse> epcsToSave = epcs.stream().map(newEpc -> {
            EducationProgramCourse oldEpc = epcsInDb.stream()
                    .filter(epc -> epc.getId().equals(newEpc.getId()))
                    .findFirst()
                    .orElseGet(() -> EducationProgramCourse.builder().educationProgram(ep).build());

            Integer oldSemesterInEP = calculateSemesterInEP(ep, oldEpc.getSemester());
            Boolean isSetNewSemester = oldSemesterInEP == null
                    || !oldSemesterInEP.equals(newEpc.getSemester());
            if (isSetNewSemester) {
                Integer semesterInYear = calculateSemesterInYear(newEpc.getSemester());
                Integer yearFromEP = calculateYearFromEP(newEpc.getSemester());
                Integer realYear = ep.getSchoolYear() + yearFromEP;

                Semester semester = semesterRepository.findBySemesterAndYear(semesterInYear, realYear)
                        .orElseThrow(() -> new RuntimeException("Semester not found"));
                oldEpc.setSemester(semester);
            }
            Boolean isSetNewCourse = oldEpc.getCourseOutline() == null
                    || !oldEpc.getCourseOutline().getId().equals(newEpc.getCourseId());

            if (isSetNewCourse) {
                Course course = courseRepository.findById(newEpc.getCourseId())
                        .orElseThrow(() -> new RuntimeException("Course not found"));
                oldEpc.setCourse(course);
            }
            return oldEpc;
        }).collect(Collectors.toList());

        ep.getEducationProgramCourses().removeIf(epc -> epcIdsToDelete.contains(epc.getId()));
        ep.getEducationProgramCourses().addAll(epcsToSave);
    }


    private Integer calculateSemesterInYear(Integer semesterInEP) {
        Integer maxSemesterInYear = 3;
        Integer moduleSemester = semesterInEP % maxSemesterInYear;
        Boolean isLastSemester = moduleSemester == 0;

        if (isLastSemester)
            return maxSemesterInYear;
        return moduleSemester;
    }

    private Integer calculateYearFromEP(Integer semesterInEP) {
        Integer maxSemesterInYear = 3;
        Integer moduleSemester = semesterInEP % maxSemesterInYear;
        Boolean isLastSemester = moduleSemester == 0;

        if (isLastSemester)
            return semesterInEP / maxSemesterInYear - 1;
        return semesterInEP / maxSemesterInYear;
    }

    private void validateCanCloneBatching(int fromYear, int toYear) {
        Integer countToYear = educationProgramRepository.countBySchoolYear(toYear);
        Boolean isToYearEmpty = countToYear == 0;
        if (!isToYearEmpty)
            throw new RuntimeException("To year is not empty");
    }


    private EducationProgramCrudResponse mapToEducationProgramCrudResponse(EducationProgram educationProgram) {
        Integer numberOfCourses = educationProgram.getEducationProgramCourses().size();
        return EducationProgramCrudResponse.builder()
                .id(educationProgram.getId())
                .majorName(educationProgram.getMajor().getName())
                .schoolYear(educationProgram.getSchoolYear())
                .numberOfCourses(numberOfCourses)
                .build();
    }

    private EducationProgramCourseResponse mapToEducationProgramCourseResponse(EducationProgram educationProgram,
                                                                               EducationProgramCourse epc) {
        Course course = epc.getCourse();
        CourseOutline courseOutline = epc.getCourseOutline();
        Long courseOutlineId = courseOutline == null ? null : courseOutline.getId();
        String courseOutlineUrl = courseOutline == null ? null : courseOutline.getUrl();
        Integer semesterOnProgram = calculateSemesterInEP(educationProgram, epc.getSemester());

        return EducationProgramCourseResponse.builder()
                .id(epc.getId())
                .courseOutlineId(courseOutlineId)
                .courseId(course.getId())
                .semester(semesterOnProgram)
                .courseOutlineUrl(courseOutlineUrl)
                .build();
    }

    private Integer calculateSemesterInEP(EducationProgram ep, Semester semester) {
        if (semester == null)
            return null;

        Integer countSchoolYear = semester.getYear() - ep.getSchoolYear();
        return countSchoolYear * 3 + semester.getSemester();
    }

    private EducationProgramCourseDto mapToEducationProgramCourseDto(EducationProgram ep,
                                                                     EducationProgramCourse epc) {
        Integer semesterOnProgram = calculateSemesterInEP(ep, epc.getSemester());
        return EducationProgramCourseDto.builder()
                .courseOutlineId(epc.getCourseOutline().getId())
                .courseName(epc.getCourseOutline().getCourse().getName())
                .semester(semesterOnProgram)
                .courseOutlineUrl(epc.getCourseOutline().getUrl())
                .build();
    }

    private EducationProgramViewDto educationProgram2ViewDto(EducationProgram ep) {
        List<EducationProgramCourseDto> epcs = ep.getEducationProgramCourses().stream()
                .map(epc -> mapToEducationProgramCourseDto(ep, epc))
                .collect(Collectors.toList());

        return EducationProgramViewDto.builder()
                .id(ep.getId())
                .majorName(ep.getMajor().getName())
                .schoolYear(ep.getSchoolYear())
                .educationProgramCourses(epcs)
                .build();
    }

    private List<EducationProgramSearchDto> educationProgram2Dto(List<EducationProgram> eps) {
        return eps.stream().map(ep ->
                        EducationProgramSearchDto.builder()
                                .id(ep.getId())
                                .majorName(ep.getMajor().getName())
                                .schoolYear(ep.getSchoolYear())
                                .build())
                .collect(Collectors.toList());
    }

}
