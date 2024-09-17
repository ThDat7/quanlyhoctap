package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.SelectOptionResponse;
import com.thanhdat.quanlyhoctap.entity.Staff;
import com.thanhdat.quanlyhoctap.repository.StaffRepository;
import com.thanhdat.quanlyhoctap.service.StaffService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;

    @Override
    public List<SelectOptionResponse> getSelectOptions() {
        List<Staff> staffs = staffRepository.findAll();
        return staffs.stream()
                .map(staff -> mapToSelectOptionResponse(staff.getId(), staff.getFullName()))
                .toList();
    }

    private SelectOptionResponse mapToSelectOptionResponse(Object value, String label) {
        return SelectOptionResponse.builder()
                .value(value)
                .label(label)
                .build();
    }
}
