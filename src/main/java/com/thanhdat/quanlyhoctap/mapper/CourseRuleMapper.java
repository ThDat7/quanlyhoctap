package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.CourseRuleResponse;
import com.thanhdat.quanlyhoctap.entity.CourseRule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseRuleMapper {
    CourseRuleResponse toCourseRuleResponse(CourseRule courseRule);
}
