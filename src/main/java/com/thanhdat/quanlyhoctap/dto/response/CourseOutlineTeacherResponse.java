package com.thanhdat.quanlyhoctap.dto.response;

import com.thanhdat.quanlyhoctap.entity.OutlineStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseOutlineTeacherResponse {
    private Integer id;
    private String courseName;
    private String courseCode;
    private OutlineStatus status;
    private LocalDateTime deadline;
}
