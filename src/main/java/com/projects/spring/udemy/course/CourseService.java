package com.projects.spring.udemy.course;

import com.projects.spring.udemy.ConfigurationProperties;
import com.projects.spring.udemy.author.Author;
import com.projects.spring.udemy.author.AuthorRepository;
import com.projects.spring.udemy.course.dto.CourseInMenu;
import com.projects.spring.udemy.course.dto.SingleCourseModel;
import com.projects.spring.udemy.relationship.CourseRating;
import com.projects.spring.udemy.relationship.CourseRatingKey;
import com.projects.spring.udemy.relationship.CourseRatingRepository;
import com.projects.spring.udemy.user.User;
import com.projects.spring.udemy.user.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private CourseRepository repository;
    private UserRepository userRepository;
    private CourseRatingRepository ratingRepository;
    private AuthorRepository authorRepository;
    private ConfigurationProperties configuration;

    public CourseService(CourseRepository repository, UserRepository userRepository, CourseRatingRepository ratingRepository, AuthorRepository authorRepository, ConfigurationProperties configuration) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.authorRepository = authorRepository;
        this.configuration = configuration;
    }

    public SingleCourseModel getCourse(Integer courseId, Integer userId) {
        Course target = repository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));

        Integer usersNumber = target.getRatings().size();

        boolean boughtCourse;
        Optional<Double> userRate;
        Optional<CourseRating> userRateExists = target.getRatings()
                .stream()
                .filter(rating -> rating.getId().getUserId() == userId)
                .findFirst();
        if (userRateExists.isEmpty()) {
            boughtCourse = false;
            userRate = null;
        } else {
            boughtCourse = true;
            userRate = userRateExists.stream()
                    .map(CourseRating::getRating)
                    .filter(rate -> rate != null)
                    .findFirst();
        }

        Optional<User> willingUser = target.getWillingUsers()
                .stream().filter(user -> user.getUserId() == userId)
                .findFirst();
        boolean isCourseLiked = willingUser.isPresent();

        return new SingleCourseModel(target, boughtCourse, userRate, isCourseLiked, usersNumber);
    }

    @Transactional
    public ResponseEntity<?> buyCourse(CourseRatingKey key) {
        CourseRating association = new CourseRating(key);

        User user = userRepository.findById(key.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No user with given id"));
        Course course = repository.findById(key.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));

        //check if user has enough money
        int sum = 0;
        if (course.getPromotion() != null)
            sum = course.getPromotion();
        else
            sum = course.getPrice();
        if(user.getBudget() < sum)
            return ResponseEntity.ok("Not enough funds on the account");

        //FIXME: add methods responsible for keeping in-sync both sides of association as it is in Course class: addComment(), removeComment()
        //EDIT: IDK if this is necessary bcs teacher told when you associate A and B wih join table C then just set A and B in C (it is already done)
        association.setCourse(course);
        association.setUser(user);

        if (course.getPrice() != 0) {
            Optional<Author> author = authorRepository.findCourseAuthorByCourseId(course.getId());
            if(author.isPresent()) {
                author.get().setBudget(author.get().getBudget() + sum);
                user.setBudget(user.getBudget() - sum);
            }
        }

        ratingRepository.save(association);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional
    public double rateCourse(CourseRating source) {
        CourseRating association = ratingRepository.findById(source.getId())
                .orElseThrow(() -> new IllegalArgumentException("This course or user is not available"));
        association.setRating(source.getRating());
        return association.getRating();
    }

    public List<CourseInMenu> getOtherParticipantsCourses(Integer targetCourseId, Integer userId) {
        Course targetCourse = repository.findById(targetCourseId)
                .orElseThrow(() -> new IllegalArgumentException("No ciurse with given id"));
        List<Integer> userIDs = targetCourse.getRatings()
                .stream().map(rate -> rate.getId().getUserId())
                .collect(Collectors.toList());
        List<CourseRating> source = ratingRepository.findCourseRatingsById_UserIdIsIn(userIDs);

        List<Integer> courseIDs = new ArrayList<>();
        source.stream().map(courseRating -> {
            Integer courseId = courseRating.getId().getCourseId();
            if (courseIDs.contains(courseId) || courseId.equals(targetCourseId))
                return null;
            else
                return courseId;
        }).forEach(courseId -> {
            if (courseId != null)
                courseIDs.add(courseId);
        });

        return repository.getCourseMenuByIdIsIn(courseIDs);
    }

    @Transactional
    public void deleteFile(Integer id) {
        Course course = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));
        course.setImage(null);
    }

    private String generateUniqueFilename() {
        RandomString random = new RandomString(64);
        String filename = null;

        while (filename == null || new File(configuration.getImagesPath() + File.separator + filename).exists()) {
            filename = configuration.getImagesPath() + File.separator + random.nextString();
        }

        filename = filename + ".png";

        return filename;
    }
}
