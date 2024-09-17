package com.thanhdat.quanlyhoctap.dto.response;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentStatusResponse {
    private SemesterDetailResponse semester;
    private Boolean isLock;
}
