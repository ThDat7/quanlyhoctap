package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewsCrudResponse {
    private Integer id;
    private String title;
    private String authorName;
    private LocalDateTime createdAt;
    private Boolean isImportant;
}
