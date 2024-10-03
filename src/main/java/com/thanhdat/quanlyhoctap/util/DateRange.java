package com.thanhdat.quanlyhoctap.util;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DateRange {
    LocalDate start;
    LocalDate end;
}
