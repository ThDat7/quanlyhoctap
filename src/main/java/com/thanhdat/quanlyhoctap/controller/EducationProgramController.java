package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.dto.response.EducationProgramViewDto;
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
}
