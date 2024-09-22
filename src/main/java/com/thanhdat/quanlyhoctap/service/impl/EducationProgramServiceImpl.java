package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.EducationProgramCourseRequest;
import com.thanhdat.quanlyhoctap.dto.request.EducationProgramCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.mapper.EducationProgramMapper;
import com.thanhdat.quanlyhoctap.repository.*;
import com.thanhdat.quanlyhoctap.service.EducationProgramService;
import com.thanhdat.quanlyhoctap.util.PagingHelper;
import com.thanhdat.quanlyhoctap.util.SemesterCalculator;
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
    EducationProgramCourseRepository educationProgramCourseRepository;
    MajorRepository majorRepository;
    SemesterRepository semesterRepository;
    CourseRepository courseRepository;

    PagingHelper pagingHelper;

    EducationProgramMapper educationProgramMapper;

    public DataWithCounterDto search(Map<String, String> params) {
        Specification<EducationProgram> specification = Specification.where(null);
        if (params.containsKey("kw"))
            specification = specification.and(nameLike(params.get("kw")));

        if (params.containsKey("year"))
            specification = specification.and(schoolYearEqual(Integer.parseInt(params.get("year"))));

        Pageable paging = pagingHelper.getPageable(params);

        Page<EducationProgram> pageEducationPrograms = educationProgramRepository.findAll(specification, paging);
        List<EducationProgramSearchDto> educationProgramsDto = pageEducationPrograms.getContent().stream()
                .map(educationProgramMapper::toEducationProgramSearchDto)
                .collect(Collectors.toList());
        long total = pageEducationPrograms.getTotalElements();
        return new DataWithCounterDto<>(educationProgramsDto, total);
    }

    @Override
    public EducationProgramViewDto getView(Long id) {
        Optional<EducationProgram> oEP =  educationProgramRepository.findById(id);
        if (oEP.isEmpty())
            throw new RuntimeException("Education program not found");

        EducationProgram ep = oEP.get();
        return educationProgramMapper.toEducationProgramViewDto(ep);
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
                .map(educationProgramMapper::toEducationProgramCrudResponse)
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
                    .map(epcRequest -> buildEducationProgramCourse(epcRequest, ep))
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
        return educationProgramMapper.toEducationProgramViewCrudResponse(educationProgram);
    }

    private EducationProgramCourse buildEducationProgramCourse(EducationProgramCourseRequest epcRequest, EducationProgram ep) {
        Integer semesterInYear = SemesterCalculator.calculateSemesterInYear(epcRequest.getSemester());
        Integer yearFromEP = SemesterCalculator.calculateYearFromEP(epcRequest.getSemester());
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

            Integer oldSemesterInEP = SemesterCalculator.calculateSemesterInEP(ep, oldEpc.getSemester());
            Boolean isSetNewSemester = oldSemesterInEP == null
                    || !oldSemesterInEP.equals(newEpc.getSemester());
            if (isSetNewSemester) {
                Integer semesterInYear = SemesterCalculator.calculateSemesterInYear(newEpc.getSemester());
                Integer yearFromEP = SemesterCalculator.calculateYearFromEP(newEpc.getSemester());
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

    private void validateCanCloneBatching(int fromYear, int toYear) {
        Integer countToYear = educationProgramRepository.countBySchoolYear(toYear);
        Boolean isToYearEmpty = countToYear == 0;
        if (!isToYearEmpty)
            throw new RuntimeException("To year is not empty");
    }
}
