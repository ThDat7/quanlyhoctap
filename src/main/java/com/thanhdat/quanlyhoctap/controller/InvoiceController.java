package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.ApiResponse;
import com.thanhdat.quanlyhoctap.dto.response.InvoiceResponse;
import com.thanhdat.quanlyhoctap.service.InvoiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceController {
    InvoiceService invoiceService;

    @GetMapping("/semester/{semesterId}/current-student")
    public ApiResponse<List<InvoiceResponse>> getByCurrentStudentAndSemester(@PathVariable Long semesterId) {
        return ApiResponse.ok(invoiceService.getByCurrentStudentAndSemester(semesterId));
    }
}
