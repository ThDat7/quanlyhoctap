package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamScheduleResponse {
    private String courseCode;
    private String courseName;
    private String studentClassName;
    private Integer quantityStudent;
    private LocalDateTime startTime;
    private String roomName;
}
