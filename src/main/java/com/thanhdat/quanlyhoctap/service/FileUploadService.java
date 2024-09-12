package com.thanhdat.quanlyhoctap.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    String upload(MultipartFile file, String folder) throws IOException;
}
