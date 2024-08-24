package com.thanhdat.quanlyhoctap.util;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateTimeRange {
    private LocalDateTime start;
    private LocalDateTime end;

    public boolean isWithinRange(LocalDateTime dateTime) {
        return (dateTime.isEqual(start) || dateTime.isAfter(start)) &&
                (dateTime.isEqual(end) || dateTime.isBefore(end));
    }

    public boolean overlaps(DateTimeRange other) {
        return !this.end.isBefore(other.start) && !this.start.isAfter(other.end);
    }
}
