package com.projects.spring.udemy.file;

import com.projects.spring.udemy.ConfigurationProperties;
import com.projects.spring.udemy.author.AuthorRepository;
import com.projects.spring.udemy.comment.CommentRepository;
import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.course.CourseRepository;
import com.projects.spring.udemy.course.dto.FileModel;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FilenameUtils;
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
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class AppFileService {
    private AppFileRepository repository;
    private CommentRepository commentRepository;
    private CourseRepository courseRepository;
    private AuthorRepository authorRepository;
    private ConfigurationProperties configuration;

    public AppFileService(
            AppFileRepository repository,
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
    public ResponseEntity<?> saveFile(Integer id, FileModel image, String entity) {
        // FIXME: replace manually comparing extensions with mimeType
        //String mimeType = image.getFile().getContentType();
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
            String extension = FilenameUtils.getExtension(
                    image.getFile().getOriginalFilename()
            );
            AppFile appFile = new AppFile(file.getAbsolutePath());
            appFile.setExtension(extension);
            AppFile savedAppFile = repository.save(appFile);

            boolean isVideo = false;
            if(entity.equals("course")) {
                List<String> videoExtensions = configuration.getVideoExtensions();
                isVideo = videoExtensions.stream()
                        .anyMatch(videoExt -> videoExt.equals(extension));
            }

            if(isVideo)
                ((Course) target).setVideo(savedAppFile);
            else
                target.setImage(savedAppFile);

            return ResponseEntity.created(URI.create("/" + savedAppFile.getFileId())).build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public ResponseEntity<?> getFile(Integer id, String entity, boolean playVideo) {
        ImageClass target = returnTarget(id, entity);
        if(target == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        AppFile targetFile;
        if(playVideo) {
            targetFile = repository.findById(((Course) target).getVideo().getFileId())
                    .orElseThrow(() -> new IllegalArgumentException("No video"));
        } else {
            Optional<AppFile> image = Optional.of(target.getImage());
            if(!image.isPresent())
                return null;
            targetFile = repository.findById(target.getImage().getFileId())
                    .orElseThrow(() -> new IllegalArgumentException("No image"));
        }

        File file = new File(targetFile.getFilePath());
        if(!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Optional<String> extension = Optional.ofNullable(targetFile.getExtension());
        if(!extension.isPresent())
            extension = Optional.of(".png");

        try{
            InputStreamResource isr = new InputStreamResource(
                    new FileInputStream(file)
            );
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=\"appFile." + extension.get() + "\"");

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

        return filename;
    }
}
