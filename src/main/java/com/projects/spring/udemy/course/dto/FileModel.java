package com.projects.spring.udemy.course.dto;

import org.springframework.web.multipart.MultipartFile;

public class FileModel {

    private MultipartFile file;

    public FileModel() {

    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
