package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.CourseCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CourseCrudRequest createRequest) {
        courseService.create(createRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        courseService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseViewCrudResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody CourseCrudRequest updateRequest) {
        courseService.update(id, updateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<DataWithCounterDto<CourseCrudResponse>> getAll(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(courseService.getAll(params));
    }

    @GetMapping("/types")
    public ResponseEntity<List<SelectOptionResponse>> getTypes() {
        return ResponseEntity.ok(courseService.getTypes());
    }
}
