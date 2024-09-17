package com.thanhdat.quanlyhoctap.dto.request;

import com.thanhdat.quanlyhoctap.dto.response.EducationProgramCourseDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EducationProgramCrudRequest {
    private int schoolYear;
    private Integer majorId;
    private List<EducationProgramCourseRequest> educationProgramCourses;
}
