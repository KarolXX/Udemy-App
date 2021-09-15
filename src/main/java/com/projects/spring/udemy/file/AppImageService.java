package com.projects.spring.udemy.file;

import com.projects.spring.udemy.ConfigurationProperties;
import com.projects.spring.udemy.comment.Comment;
import com.projects.spring.udemy.comment.CommentRepository;
import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.course.CourseRepository;
import com.projects.spring.udemy.course.dto.UploadImage;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

@Service
public class AppImageService {
    private AppImageRepository repository;
    private CommentRepository commentRepository;
    private CourseRepository courseRepository;
    private ConfigurationProperties configuration;

    public AppImageService(
            AppImageRepository repository,
            CommentRepository commentRepository,
            CourseRepository courseRepository,
            ConfigurationProperties configuration
    ) {
        this.repository = repository;
        this.commentRepository = commentRepository;
        this.courseRepository = courseRepository;
        this.configuration = configuration;
    }

    @Transactional
    public void saveImage(Integer id, UploadImage image, String entity) {
        Comment comment = null;
       // Course course = null;
        if (entity.equals("comment"))
            comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No such comment"));
//        else if (entity.equals("course"))
//            course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No course with given id"));

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
            repository.save(appImage);
            if (comment != null)
                comment.setImage(appImage);
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
