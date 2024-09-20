package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.EducationProgramCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.CourseOutlineService;
import com.thanhdat.quanlyhoctap.service.EducationProgramService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/education-programs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EducationProgramController {
    EducationProgramService educationProgramService;
    CourseOutlineService courseOutlineService;

    @GetMapping("/search")
    public ApiResponse<List<DataWithCounterDto>> search(@RequestParam Map<String, String> params) {
        DataWithCounterDto dwcCourseOutlines = courseOutlineService.search(params);
        DataWithCounterDto dwcEducationPrograms = educationProgramService.search(params);
        return ApiResponse.ok(List.of(dwcCourseOutlines, dwcEducationPrograms));
    }

    @GetMapping("/view/{id}")
    public ApiResponse<EducationProgramViewDto> getView(@PathVariable Long id) {
        return ApiResponse.ok(educationProgramService.getView(id));
    }

    @GetMapping("/clone-batching/fromYear/{fromYear}/toYear/{toYear}")
    public ApiResponse<EducationProgramCloneBatchingResponse> cloneBatching(@PathVariable int fromYear, @PathVariable int toYear) {
        return ApiResponse.ok(educationProgramService.cloneBatching(fromYear, toYear));
    }

    @GetMapping
    public ApiResponse<DataWithCounterDto<EducationProgramCrudResponse>> getAll(@RequestParam Map<String, String> params) {
        return ApiResponse.ok(educationProgramService.getAll(params));
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody @Valid EducationProgramCrudRequest createRequest) {
        educationProgramService.create(createRequest);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        educationProgramService.delete(id);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<EducationProgramViewCrudResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(educationProgramService.getById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody @Valid EducationProgramCrudRequest updateRequest) {
        educationProgramService.update(id, updateRequest);
        return ApiResponse.ok();
    }
}
