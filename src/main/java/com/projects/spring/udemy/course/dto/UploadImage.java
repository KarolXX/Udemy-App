package com.projects.spring.udemy.course.dto;

import org.springframework.web.multipart.MultipartFile;

public class UploadImage {

    private MultipartFile file;

    public UploadImage() {

    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
