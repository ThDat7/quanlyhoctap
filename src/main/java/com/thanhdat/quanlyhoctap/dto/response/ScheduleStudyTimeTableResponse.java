package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleStudyTimeTableResponse {
    private LocalDate startDate;
    private Integer weekLength;
    private Integer shiftStart;
    private Integer shiftLength;
    private String roomType;
    private String roomName;
}
