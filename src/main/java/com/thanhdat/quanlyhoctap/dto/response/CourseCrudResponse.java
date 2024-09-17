package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourseCrudResponse {
    private Integer id;
    private String name;
    private String code;
    private Float credits;
    private String majors;
}
