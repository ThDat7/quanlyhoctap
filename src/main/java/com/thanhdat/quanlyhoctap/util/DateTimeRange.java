package com.thanhdat.quanlyhoctap.util;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DateTimeRange {
    LocalDateTime start;
    LocalDateTime end;

    public boolean isWithinRange(LocalDateTime dateTime) {
        return (dateTime.isEqual(start) || dateTime.isAfter(start)) &&
                (dateTime.isEqual(end) || dateTime.isBefore(end));
    }

    public boolean overlaps(DateTimeRange other) {
        return !this.end.isBefore(other.start) && !this.start.isAfter(other.end);
    }
}
