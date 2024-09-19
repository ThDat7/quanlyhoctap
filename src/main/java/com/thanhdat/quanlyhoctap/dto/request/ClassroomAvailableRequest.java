package com.thanhdat.quanlyhoctap.dto.request;

import com.thanhdat.quanlyhoctap.entity.RoomType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassroomAvailableRequest {
    RoomType roomType;
    List<TimeToUseClassroomRequest> timeToUseClassroomRequests;
}
