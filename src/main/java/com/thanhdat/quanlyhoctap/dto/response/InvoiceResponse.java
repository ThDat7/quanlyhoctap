package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {
    private String courseName;
    private String courseCode;
    private Float courseCredits;
    private Integer tuition;
}
