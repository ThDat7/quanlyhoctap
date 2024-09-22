package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.SelectOptionResponse;
import com.thanhdat.quanlyhoctap.entity.Staff;
import com.thanhdat.quanlyhoctap.mapper.UtilMapper;
import com.thanhdat.quanlyhoctap.repository.StaffRepository;
import com.thanhdat.quanlyhoctap.service.StaffService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffServiceImpl implements StaffService {
    StaffRepository staffRepository;

    UtilMapper utilMapper;

    @Override
    public List<SelectOptionResponse> getSelectOptions() {
        List<Staff> staffs = staffRepository.findAll();
        return staffs.stream()
                .map(staff -> utilMapper.toSelectOptionResponse(staff.getId(), staff.getFullName()))
                .toList();
    }
}
