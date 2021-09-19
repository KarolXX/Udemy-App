package com.projects.spring.udemy.course.dto;

import org.springframework.web.multipart.MultipartFile;

public class ImageModel {

    private MultipartFile file;

    public ImageModel() {

    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
