package com.projects.spring.udemy.file;

import com.projects.spring.udemy.ConfigurationProperties;
import com.projects.spring.udemy.author.AuthorRepository;
import com.projects.spring.udemy.comment.CommentRepository;
import com.projects.spring.udemy.course.CourseRepository;
import com.projects.spring.udemy.course.dto.UploadImage;
import net.bytebuddy.utility.RandomString;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class AppImageService {
    private AppImageRepository repository;
    private CommentRepository commentRepository;
    private CourseRepository courseRepository;
    private AuthorRepository authorRepository;
    private ConfigurationProperties configuration;

    public AppImageService(
            AppImageRepository repository,
            CommentRepository commentRepository,
            CourseRepository courseRepository,
            AuthorRepository authorRepository,
            ConfigurationProperties configuration
    ) {
        this.repository = repository;
        this.commentRepository = commentRepository;
        this.courseRepository = courseRepository;
        this.authorRepository = authorRepository;
        this.configuration = configuration;
    }

    @Transactional
    public ResponseEntity<?> saveImage(Integer id, UploadImage image, String entity) {
        ImageClass target = returnTarget(id, entity);
        if(target == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        String imagesPath = configuration.getImagesPath();
        File folder = new File(imagesPath);
        File file = null;

        if (!folder.exists()) {
            folder.mkdir();
        }

        try {
            file = new File(generateUniqueFilename());
            image.getFile().transferTo(file);
        } catch (IOException e) {

        }

        if (file == null) {

        } else {
            AppImage appImage = new AppImage(file.getAbsolutePath());
            AppImage savedAppImage = repository.save(appImage);
            target.setImage(savedAppImage);

            return getImage(id, entity);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public ResponseEntity<?> getImage(Integer id, String entity) {
        ImageClass target = returnTarget(id, entity);
        if(target == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        AppImage targetImage = repository.findById(target.getImage().getImageId())
                .orElseThrow(() -> new IllegalArgumentException("No image"));

        File file = new File(targetImage.getImage());
        if(!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        try{
            InputStreamResource isr = new InputStreamResource(
                    new FileInputStream(file)
            );
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=\"xxx.png\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(isr);
        } catch (FileNotFoundException e) {

        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private ImageClass returnTarget(Integer id, String entityName) {
        switch (entityName) {
            case "course":
                return courseRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("No such course"));
            case "comment":
                return commentRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("No such comment"));
            case "author":
                return authorRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("No such author"));
            default:
                return null;
        }
    }

    private String generateUniqueFilename() {
        RandomString random = new RandomString(64);
        String filename = null;

        while (filename == null || new File(configuration.getImagesPath() + File.separator + filename).exists()) {
            filename = configuration.getImagesPath() + File.separator + random.nextString();
        }

        return filename + ".png";
    }
}
