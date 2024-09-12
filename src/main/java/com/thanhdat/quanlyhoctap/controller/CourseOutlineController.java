package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.CourseOutlineEditTeacherRequest;
import com.thanhdat.quanlyhoctap.dto.response.CourseOutlineTeacherResponse;
import com.thanhdat.quanlyhoctap.dto.response.CourseOutlineViewTeacherResponse;
import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.service.CourseOutlineService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/course-outlines")
@AllArgsConstructor
public class CourseOutlineController {
    private final CourseOutlineService courseOutlineService;

    @GetMapping("/current-teacher")
    public ResponseEntity<DataWithCounterDto<CourseOutlineTeacherResponse>> getAllByCurrentTeacher(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(courseOutlineService.getAllByCurrentTeacher(params));
    }

    @GetMapping("/{id}/current-teacher")
    public ResponseEntity<CourseOutlineViewTeacherResponse> teacherView(@PathVariable Integer id) {
        return ResponseEntity.ok(courseOutlineService.getViewByCurrentTeacher(id));
    }

    @PostMapping(value = "/{id}/current-teacher", consumes = {"multipart/form-data"})
    public ResponseEntity teacherUpdate(@PathVariable Integer id,
                                        @RequestPart(value = "file") MultipartFile file,
                                        @RequestPart(value = "data") CourseOutlineEditTeacherRequest request) {
        courseOutlineService.updateByCurrentTeacher(id, file, request);
        return ResponseEntity.ok().build();
    }
}
