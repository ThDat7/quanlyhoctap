package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.CourseOutlineEditTeacherRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.CourseOutlineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/course-outlines")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseOutlineController {
    CourseOutlineService courseOutlineService;

    @GetMapping("/current-teacher")
    public ApiResponse<DataWithCounterDto<CourseOutlineTeacherResponse>> getAllByCurrentTeacher(@RequestParam Map<String, String> params) {
        return ApiResponse.ok(courseOutlineService.getAllByCurrentTeacher(params));
    }

    @GetMapping("/{id}/current-teacher")
    public ApiResponse<CourseOutlineViewTeacherResponse> teacherView(@PathVariable Long id) {
        return ApiResponse.ok(courseOutlineService.getViewByCurrentTeacher(id));
    }

    @PostMapping(value = "/{id}/current-teacher", consumes = {"multipart/form-data"})
    public ApiResponse<Void> teacherUpdate(@PathVariable Long id,
                                        @RequestPart(value = "file") MultipartFile file,
                                        @RequestPart(value = "data") CourseOutlineEditTeacherRequest request) {
        courseOutlineService.updateByCurrentTeacher(id, file, request);
        return ApiResponse.ok();
    }
}
