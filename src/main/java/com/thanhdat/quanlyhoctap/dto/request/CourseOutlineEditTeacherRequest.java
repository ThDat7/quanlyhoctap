package com.thanhdat.quanlyhoctap.dto.request;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseOutlineEditTeacherRequest {
    private CourseRuleRequest courseRule;
}
