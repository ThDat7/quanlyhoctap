package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseOutlineSearchDto {
    Long id;
    String courseName;
    String teacherName;
    Float courseCredits;
    List<Integer> years;
    String url;
}
