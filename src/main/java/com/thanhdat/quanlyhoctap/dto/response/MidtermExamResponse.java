package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MidtermExamResponse {
    Integer courseClassId;
    String courseName;
    String courseCode;
    LocalDateTime startTime;
    String roomName;
}
