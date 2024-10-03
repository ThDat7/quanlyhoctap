package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseClassViewCrudResponse {
    Long courseId;
    CourseRuleResponse courseRule;
    Integer durationFinalExam;
    Long semesterId;
    Long teacherId;
    Long studentClassId;
    Integer capacity;
}
