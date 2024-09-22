package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.SelectOptionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UtilMapper {
    SelectOptionResponse toSelectOptionResponse(Object value, String label);
}
