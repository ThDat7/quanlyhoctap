package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.FacultyCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.FacultyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/faculties")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FacultyController {
    FacultyService faculTyService;

    @GetMapping("/select-options")
    public ApiResponse<List<SelectOptionResponse>> getSelectOptions(){
        return ApiResponse.ok(faculTyService.getAllForSelect());
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody FacultyCrudRequest createRequest) {
        faculTyService.create(createRequest);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        faculTyService.delete(id);
        return ApiResponse.ok();
    }

    @GetMapping
    public ApiResponse<DataWithCounterDto<FacultyCrudResponse>> getAll(@RequestParam Map<String, String> params) {
        return ApiResponse.ok(faculTyService.getAll(params));
    }

    @GetMapping("/{id}")
    public ApiResponse<FacultyViewCrudResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(faculTyService.getById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody FacultyCrudRequest updateRequest) {
        faculTyService.update(id, updateRequest);
        return ApiResponse.ok();
    }
}
