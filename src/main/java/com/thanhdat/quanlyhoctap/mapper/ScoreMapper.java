package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.ScoreResponse;
import com.thanhdat.quanlyhoctap.entity.Score;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScoreMapper {
    @Mapping(target = "studyId", source = "study.id")
    @Mapping(target = "studentFirstName", source = "study.student.user.firstName")
    @Mapping(target = "studentLastName", source = "study.student.user.lastName")
    @Mapping(target = "studentCode", source = "study.student.studentCode")
    ScoreResponse toScoreResponse(Score score);
}
