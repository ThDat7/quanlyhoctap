package com.thanhdat.quanlyhoctap.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FacultyViewCrudResponse {
    Integer id;
    String name;
    String alias;
}
