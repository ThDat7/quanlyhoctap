package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.config.PaginationProperties;
import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.dto.response.EducationProgramCourseDto;
import com.thanhdat.quanlyhoctap.dto.response.EducationProgramSearchDto;
import com.thanhdat.quanlyhoctap.dto.response.EducationProgramViewDto;
import com.thanhdat.quanlyhoctap.entity.EducationProgram;
import com.thanhdat.quanlyhoctap.repository.EducationProgramRepository;
import com.thanhdat.quanlyhoctap.service.EducationProgramService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.thanhdat.quanlyhoctap.specification.EducationProgramSpecification.*;

@Service
@AllArgsConstructor
public class EducationProgramServiceImpl implements EducationProgramService {
    private final EducationProgramRepository educationProgramRepository;
    private final PaginationProperties paginationProperties;

    public DataWithCounterDto search(Map<String, String> params) {
        Specification<EducationProgram> specification = Specification.where(null);
        if (params.containsKey("kw"))
            specification = specification.and(nameLike(params.get("kw")));

        if (params.containsKey("year"))
            specification = specification.and(schoolYearEqual(Integer.parseInt(params.get("year"))));

        Integer page = 0;
        Integer pageSize = paginationProperties.getPageSize();

        if (params.containsKey("page"))
            page = Integer.parseInt(params.get("page")) - 1;

        Pageable paging = PageRequest.of(page, pageSize);

        Page<EducationProgram> pageEducationPrograms = educationProgramRepository.findAll(specification, paging);
        List<EducationProgramSearchDto> educationProgramsDto = educationProgram2Dto(pageEducationPrograms.getContent());
        Integer total = (int) pageEducationPrograms.getTotalElements();
        return new DataWithCounterDto<>(educationProgramsDto, total);
    }

    @Override
    public EducationProgramViewDto getView(int id) {
        Optional<EducationProgram> oEP =  educationProgramRepository.findById(id);
        if (oEP.isEmpty())
            throw new RuntimeException("Education program not found");

        EducationProgram ep = oEP.get();
        return educationProgram2ViewDto(ep);
    }

    private EducationProgramViewDto educationProgram2ViewDto(EducationProgram ep) {
        List<EducationProgramCourseDto> epcs = ep.getEducationProgramCourses().stream()
                .map(epc -> {
                    Integer countSchoolYear = epc.getSemester().getYear() - ep.getSchoolYear();
                    Integer semesterOnProgram = countSchoolYear * 3 + epc.getSemester().getSemester();

                    return EducationProgramCourseDto.builder()
                            .courseOutlineId(epc.getCourseOutline().getId())
                            .courseName(epc.getCourseOutline().getCourse().getName())
                            .semester(semesterOnProgram)
                            .courseOutlineUrl(epc.getCourseOutline().getUrl())
                            .build();
                })
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
