package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.entity.Setting;
import com.thanhdat.quanlyhoctap.helper.settingbag.RegisterCourseSettingType;
import com.thanhdat.quanlyhoctap.repository.SettingRepository;
import com.thanhdat.quanlyhoctap.service.SettingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SettingServiceImpl implements SettingService {
    private final SettingRepository settingRepository;

    public Integer getSemesterIdForRegister() {
        Optional<Setting> setting = settingRepository.findById(RegisterCourseSettingType
                .SEMESTER_ID_FOR_REGISTER.name());
        return Integer.parseInt(setting
                .orElseThrow(() -> new RuntimeException("Setting not found"))
                .getValue());
    }
}
