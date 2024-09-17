package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.SettingCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.dto.response.SettingCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.SettingViewCrudResponse;
import com.thanhdat.quanlyhoctap.entity.Setting;
import com.thanhdat.quanlyhoctap.helper.settingbag.RegisterCourseSettingType;
import com.thanhdat.quanlyhoctap.repository.SettingRepository;
import com.thanhdat.quanlyhoctap.service.SettingService;
import com.thanhdat.quanlyhoctap.util.PagingHelper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SettingServiceImpl implements SettingService {
    private final SettingRepository settingRepository;
    private final PagingHelper pagingHelper;

    public Integer getSemesterIdForRegister() {
        Optional<Setting> setting = settingRepository.findById(RegisterCourseSettingType
                .SEMESTER_ID_FOR_REGISTER.name());
        return Integer.parseInt(setting
                .orElseThrow(() -> new RuntimeException("Setting not found"))
                .getValue());
    }

    @Override
    public SettingViewCrudResponse getById(String key) {
        Setting setting = settingRepository.findById(key)
                .orElseThrow(() -> new RuntimeException("Setting not found"));
        return mapToSettingViewCrudResponse(setting);
    }

    private SettingViewCrudResponse mapToSettingViewCrudResponse(Setting setting) {
        return SettingViewCrudResponse.builder()
                .key(setting.getKey())
                .value(setting.getValue())
                .build();
    }

    @Override
    public void update(String key, SettingCrudRequest updateRequest) {
        Setting oldSetting = settingRepository.findById(key)
                .orElseThrow(() -> new RuntimeException("Setting not found"));
//        validate
        oldSetting.setValue(updateRequest.getValue());
        settingRepository.save(oldSetting);
    }

    @Override
    public DataWithCounterDto<SettingCrudResponse> getAll(Map<String, String> params) {
        Pageable paging = pagingHelper.getPageable(params);
        Page<Setting> page = settingRepository.findAll(paging);
        List<SettingCrudResponse> dto = page.getContent().stream()
                .map(this::mapToSettingCrudResponse)
                .collect(Collectors.toList());
        Integer total = (int) page.getTotalElements();
        return new DataWithCounterDto<>(dto, total);
    }

    private SettingCrudResponse mapToSettingCrudResponse(Setting setting) {
        return SettingCrudResponse.builder()
                .key(setting.getKey())
                .value(setting.getValue())
                .build();
    }
}
