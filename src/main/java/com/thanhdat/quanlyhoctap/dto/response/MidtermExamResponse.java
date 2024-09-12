package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MidtermExamResponse {
    private Integer courseClassId;
    private String courseName;
    private String courseCode;
    private LocalDateTime startTime;
    private String roomName;
}
