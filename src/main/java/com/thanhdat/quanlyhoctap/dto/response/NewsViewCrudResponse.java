package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewsViewCrudResponse {
    private Integer id;
    private String title;
    private Integer authorId;
    private String content;
    private Boolean isImportant;
    private LocalDateTime createdAt;
}
