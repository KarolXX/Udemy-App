package com.projects.spring.udemy.course.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Data
public class FileModel {

    private MultipartFile file;

}
