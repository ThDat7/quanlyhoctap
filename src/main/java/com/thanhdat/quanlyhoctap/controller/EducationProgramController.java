package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.EducationProgramCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.CourseOutlineService;
import com.thanhdat.quanlyhoctap.service.EducationProgramService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/education-programs")
@AllArgsConstructor
public class EducationProgramController {
    private final EducationProgramService educationProgramService;
    private final CourseOutlineService courseOutlineService;

    @GetMapping("/search")
    public ResponseEntity<List<DataWithCounterDto>> search(@RequestParam Map<String, String> params) {
        DataWithCounterDto dwcCourseOutlines = courseOutlineService.search(params);
        DataWithCounterDto dwcEducationPrograms = educationProgramService.search(params);
        return ResponseEntity.ok(List.of(dwcCourseOutlines, dwcEducationPrograms));
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<EducationProgramViewDto> getView(@PathVariable int id) {
        return ResponseEntity.ok(educationProgramService.getView(id));
    }

    @GetMapping("/clone-batching/fromYear/{fromYear}/toYear/{toYear}")
    public ResponseEntity<EducationProgramCloneBatchingResponse> cloneBatching(@PathVariable int fromYear, @PathVariable int toYear) {
        return ResponseEntity.ok(educationProgramService.cloneBatching(fromYear, toYear));
    }

    @GetMapping
    public ResponseEntity<DataWithCounterDto<EducationProgramCrudResponse>> getAll(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(educationProgramService.getAll(params));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody EducationProgramCrudRequest createRequest) {
        educationProgramService.create(createRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        educationProgramService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Integer id) {
        return ResponseEntity.ok(educationProgramService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody EducationProgramCrudRequest updateRequest) {
        educationProgramService.update(id, updateRequest);
        return ResponseEntity.ok().build();
    }
}
