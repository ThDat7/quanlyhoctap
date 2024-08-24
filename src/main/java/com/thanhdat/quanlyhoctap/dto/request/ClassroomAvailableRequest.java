package com.thanhdat.quanlyhoctap.dto.request;

import com.thanhdat.quanlyhoctap.entity.RoomType;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomAvailableRequest {
    private RoomType roomType;
    private List<TimeToUseClassroomRequest> timeToUseClassroomRequests;
}
