package com.thanhdat.quanlyhoctap.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MajorCrudRequest {
    String name;
    String alias;
    Integer facultyId;

    Integer specializeTuition;
    Integer generalTuition;
}
