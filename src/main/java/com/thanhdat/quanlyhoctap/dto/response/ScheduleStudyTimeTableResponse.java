package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleStudyTimeTableResponse {
    LocalDate startDate;
    Integer weekLength;
    Integer shiftStart;
    Integer shiftLength;
    String roomType;
    String roomName;
}
