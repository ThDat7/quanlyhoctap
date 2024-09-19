package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.FacultyCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.FacultyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<SelectOptionResponse>> getSelectOptions(){
        return ResponseEntity.ok(faculTyService.getAllForSelect());
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody FacultyCrudRequest createRequest) {
        faculTyService.create(createRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        faculTyService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<DataWithCounterDto<FacultyCrudResponse>> getAll(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(faculTyService.getAll(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyViewCrudResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(faculTyService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody FacultyCrudRequest updateRequest) {
        faculTyService.update(id, updateRequest);
        return ResponseEntity.ok().build();
    }
}
