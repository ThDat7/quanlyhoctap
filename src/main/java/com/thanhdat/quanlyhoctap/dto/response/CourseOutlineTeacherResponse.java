package com.thanhdat.quanlyhoctap.dto.response;

import com.thanhdat.quanlyhoctap.entity.OutlineStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseOutlineTeacherResponse {
    Integer id;
    String courseName;
    String courseCode;
    OutlineStatus status;
    LocalDateTime deadline;
}
