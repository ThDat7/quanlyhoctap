package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.FacultyCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.FacultyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/faculties")
@AllArgsConstructor
public class FacultyController {
    private final FacultyService faculTyService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody FacultyCrudRequest createRequest) {
        faculTyService.create(createRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        faculTyService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<DataWithCounterDto<FacultyCrudResponse>> getAll(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(faculTyService.getAll(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyViewCrudResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(faculTyService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody FacultyCrudRequest updateRequest) {
        faculTyService.update(id, updateRequest);
        return ResponseEntity.ok().build();
    }
}
