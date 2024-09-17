package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MajorViewCrudResponse {
    private Integer id;
    private String name;
    private String alias;
    private Integer facultyId;

    private Integer specializeTuition;
    private Integer generalTuition;
}
