package com.thanhdat.quanlyhoctap.dto.response;

import com.thanhdat.quanlyhoctap.entity.ExamType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamScheduleResponse {
    String courseCode;
    String courseName;
    String studentClassName;
    Integer quantityStudent;
    ExamType type;
    LocalDateTime startTime;
    String roomName;
}
