package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.response.InvoiceResponse;

import java.util.List;

public interface InvoiceService {

    List<InvoiceResponse> getByCurrentStudentAndSemester(Long semesterId);
}
