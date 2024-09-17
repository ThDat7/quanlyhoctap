package com.thanhdat.quanlyhoctap.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MajorCrudRequest {
    private String name;
    private String alias;
    private Integer facultyId;

    private Integer specializeTuition;
    private Integer generalTuition;
}
