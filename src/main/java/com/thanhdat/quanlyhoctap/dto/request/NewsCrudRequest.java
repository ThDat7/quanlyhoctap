package com.thanhdat.quanlyhoctap.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsCrudRequest {
    @NotNull(message = "NEWS_TITLE_NOT_NULL")
    @Size(min = 3, max = 255, message = "NEWS_TITLE_SIZE")
    String title;
    @NotNull(message = "NEWS_CONTENT_NOT_NULL")
    @Size(min = 3, max = 255, message = "NEWS_CONTENT_SIZE")
    String content;
    @NotNull(message = "NEWS_IS_IMPORTANT_NOT_NULL")
    Boolean isImportant;
    @NotNull(message = "NEWS_AUTHOR_NOT_NULL")
    Long authorId;
}
