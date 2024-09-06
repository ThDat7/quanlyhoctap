package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.config.PaginationProperties;
import com.thanhdat.quanlyhoctap.dto.response.CourseOutlineSearchDto;
import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.entity.CourseOutline;
import com.thanhdat.quanlyhoctap.repository.CourseOutlineRepository;
import com.thanhdat.quanlyhoctap.service.CourseOutlineService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.thanhdat.quanlyhoctap.specification.CourseOutlineSpecification.*;

@Service
@AllArgsConstructor
public class CourseOutlineServiceImpl implements CourseOutlineService {
    private final CourseOutlineRepository courseOutlineRepository;
    private final PaginationProperties paginationProperties;

    @Override
    public DataWithCounterDto<CourseOutlineSearchDto> search(Map<String, String> params) {
        Specification<CourseOutline> specification = Specification.where(haveUrl());
        specification = specification.and(isPublished());

        if (params.containsKey("kw")) {
            specification = specification.and(courseNameLike(params.get("kw")));
            specification = specification.or(teacherNameLike(params.get("kw")));
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
