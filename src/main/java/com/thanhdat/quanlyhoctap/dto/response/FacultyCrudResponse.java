package com.thanhdat.quanlyhoctap.dto.response;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FacultyCrudResponse {
    private Integer id;
    private String name;
    private String alias;
}
