package com.thanhdat.quanlyhoctap.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewsCrudRequest {
    private String title;
    private String content;
    private Boolean isImportant;
    private Integer authorId;
}
