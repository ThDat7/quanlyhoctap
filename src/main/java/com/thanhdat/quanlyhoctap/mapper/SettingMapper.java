package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.SettingCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.SettingViewCrudResponse;
import com.thanhdat.quanlyhoctap.entity.Setting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SettingMapper {
    @Mapping(target = "id", source = "key")
    SettingCrudResponse toSettingCrudResponse(Setting setting);

    SettingViewCrudResponse toSettingViewCrudResponse(Setting setting);
}
