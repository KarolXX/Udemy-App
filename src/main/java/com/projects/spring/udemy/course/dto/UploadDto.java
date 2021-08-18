package com.projects.spring.udemy.course.dto;

import org.springframework.web.multipart.MultipartFile;

public class UploadDto {

    private MultipartFile file;

    public UploadDto() {

    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
