package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.MajorCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.dto.response.MajorCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.MajorViewCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.SelectOptionResponse;
import com.thanhdat.quanlyhoctap.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
public class MajorController {
    private final MajorService majorService;

    @GetMapping
    public ResponseEntity<DataWithCounterDto<MajorCrudResponse>> getAll(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(majorService.getAll(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MajorViewCrudResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(majorService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody MajorCrudRequest majorCrudRequest) {
        majorService.create(majorCrudRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody MajorCrudRequest majorCrudRequest) {
        majorService.update(id, majorCrudRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        majorService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/select-options")
    public ResponseEntity<List<SelectOptionResponse>> getSelectOptions() {
        return ResponseEntity.ok(majorService.getSelectOptions());
    }
}
