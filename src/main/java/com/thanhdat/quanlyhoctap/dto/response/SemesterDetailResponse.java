package com.thanhdat.quanlyhoctap.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SemesterDetailResponse {
    Integer id;
    String year;
    Integer semester;
    LocalDate startDate;
    Integer durationWeeks;
}
