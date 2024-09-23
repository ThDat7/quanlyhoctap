package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.SettingCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.dto.response.SettingCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.SettingViewCrudResponse;
import com.thanhdat.quanlyhoctap.entity.Setting;
import com.thanhdat.quanlyhoctap.exception.code.ErrorCode;
import com.thanhdat.quanlyhoctap.exception.type.AppException;
import com.thanhdat.quanlyhoctap.helper.settingbag.RegisterCourseSettingType;
import com.thanhdat.quanlyhoctap.mapper.SettingMapper;
import com.thanhdat.quanlyhoctap.repository.SettingRepository;
import com.thanhdat.quanlyhoctap.service.SettingService;
import com.thanhdat.quanlyhoctap.util.PagingHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SettingServiceImpl implements SettingService {
    SettingRepository settingRepository;

    PagingHelper pagingHelper;

    SettingMapper settingMapper;

    public Long getSemesterIdForRegister() {
        Setting setting = settingRepository.findById(RegisterCourseSettingType
                .SEMESTER_ID_FOR_REGISTER.name())
                .orElseThrow(() -> new RuntimeException("Setting not found"));
        return Long.parseLong(setting.getValue());
    }

    @Override
    public SettingViewCrudResponse getById(String key) {
        Setting setting = settingRepository.findById(key)
                .orElseThrow(() -> new AppException(ErrorCode.SETTING_NOT_FOUND));
        return settingMapper.toSettingViewCrudResponse(setting);
    }

    @Override
    public void update(String key, SettingCrudRequest updateRequest) {
//        CHECK IS SETTING VALID
        Setting oldSetting = settingRepository.findById(key)
                .orElseThrow(() -> new AppException(ErrorCode.SETTING_NOT_FOUND));
//        validate
        oldSetting.setValue(updateRequest.getValue());
        settingRepository.save(oldSetting);
    }

    @Override
    public DataWithCounterDto<SettingCrudResponse> getAll(Map<String, String> params) {
        Pageable paging = pagingHelper.getPageable(params);
        Page<Setting> page = settingRepository.findAll(paging);
        List<SettingCrudResponse> dto = page.getContent().stream()
                .map(settingMapper::toSettingCrudResponse)
                .collect(Collectors.toList());
        long total = page.getTotalElements();
        return new DataWithCounterDto<>(dto, total);
    }
}
