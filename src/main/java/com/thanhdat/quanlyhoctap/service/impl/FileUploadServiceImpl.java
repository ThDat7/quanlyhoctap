package com.thanhdat.quanlyhoctap.service.impl;

import com.cloudinary.Cloudinary;
import com.thanhdat.quanlyhoctap.service.FileUploadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    private Cloudinary cloudinary;

    public String upload(MultipartFile file, String folder) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of("folder", folder));
        return uploadResult.get("url").toString();
    }
}
