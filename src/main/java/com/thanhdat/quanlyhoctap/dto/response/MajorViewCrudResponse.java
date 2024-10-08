package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MajorViewCrudResponse {
    Long id;
    String name;
    String alias;
    Long facultyId;

    Integer specializeTuition;
    Integer generalTuition;
}
