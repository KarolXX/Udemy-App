package com.projects.spring.udemy.course;

import com.projects.spring.udemy.ConfigurationProperties;
import com.projects.spring.udemy.author.Author;
import com.projects.spring.udemy.author.AuthorRepository;
import com.projects.spring.udemy.course.dto.CourseInMenu;
import com.projects.spring.udemy.course.dto.SingleCourseModel;
import com.projects.spring.udemy.course.dto.UpdatedCourse;
import com.projects.spring.udemy.course.event.CourseSequenceChangingEvent;
import com.projects.spring.udemy.relationship.BoughtCourse;
import com.projects.spring.udemy.relationship.BoughtCourseKey;
import com.projects.spring.udemy.relationship.BoughtCourseRepository;
import com.projects.spring.udemy.user.User;
import com.projects.spring.udemy.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.pow;

@Service
public class CourseService {
    private final CourseRepository repository;
    private final UserRepository userRepository;
    private final BoughtCourseRepository boughtCourseRepository;
    private final AuthorRepository authorRepository;
    private final ConfigurationProperties configuration;

    private ModelMapper modelMapper;
    private ApplicationEventPublisher eventPublisher;

    public CourseService(
            CourseRepository repository,
            UserRepository userRepository,
            BoughtCourseRepository boughtCourseRepository,
            AuthorRepository authorRepository,
            ConfigurationProperties configuration,
            ModelMapper modelMapper,
            ApplicationEventPublisher eventPublisher
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.boughtCourseRepository = boughtCourseRepository;
        this.authorRepository = authorRepository;
        this.configuration = configuration;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
    }

    SingleCourseModel getCourse(Integer courseId, Integer userId) {
        Course target = repository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));

        Integer usersNumber = target.getRatings().size();

        boolean boughtCourse;
        Optional<Double> userRate;

        Optional<BoughtCourse> association = target.getRatings()
                .stream()
                .filter(rating -> rating.getId().getUserId() == userId)
                .findFirst();

        if (association.isEmpty()) {
            boughtCourse = false;
            userRate = null;
        } else {
            boughtCourse = true;
            userRate = Optional.ofNullable(association.get().getRating());
        }

        boolean isCourseLiked = target.getWillingUsers()
                .stream().anyMatch(user -> user.getUserId() == userId);

        return new SingleCourseModel(target, boughtCourse, userRate, isCourseLiked, usersNumber);
    }

    @Transactional
    public ResponseEntity<?> buyCourse(BoughtCourseKey key) {
        // when user buy course then association between him and bought course is created
        BoughtCourse association = new BoughtCourse(key);

        User user = userRepository.findById(key.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No user with given id"));
        Course course = repository.findById(key.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));

        //check if user has enough money
        int price = 0;
        if (course.getPromotion() != null)
            price = course.getPromotion();
        else
            price = course.getPrice();
        if(user.getBudget() < price)
            throw new NotEnoughMoneyAvailableException("You don't have enough money on the account to purchase this course");

        association.setCourse(course);
        association.setUser(user);

        // send money for author's budget
        if (price != 0) {
            Optional<Author> author = authorRepository.findAuthorCourseByCourseId(course.getId());
            if(author.isPresent()) {
                author.get().setBudget(author.get().getBudget() + price);
                user.setBudget(user.getBudget() - price);
            }
        }

        boughtCourseRepository.save(association);

        // update the number of course users
        // (there is no need to update the course' average rating because the user cannot rate and buy a course at the same time)
        course.setUsersNumber(course.getUsersNumber() + 1);

        eventPublisher.publishEvent(
                new CourseSequenceChangingEvent(course.getId())
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional
    public Double rateCourse(BoughtCourse source) {
        BoughtCourse association = boughtCourseRepository.findById(source.getId())
                .orElseThrow(() -> new IllegalArgumentException("Buy course before commenting"));
        association.setRating(source.getRating());
        BoughtCourse updatedSource = boughtCourseRepository.save(association);

        // update course's average rating
        int targetCourseId = source.getId().getCourseId();
        repository.updateCourseAverageRating(targetCourseId);

        eventPublisher.publishEvent(
                new CourseSequenceChangingEvent(source.getId().getCourseId())
        );

        return updatedSource.getRating();
    }

    Course updateCourse(UpdatedCourse updatedCourse, Integer courseId) {
        TypeMap<UpdatedCourse, Course> propertyMapper = modelMapper.getTypeMap(UpdatedCourse.class, Course.class);
        if(propertyMapper == null)
                propertyMapper = modelMapper.createTypeMap(UpdatedCourse.class, Course.class);

        // provide course that should be modified to modelMapper
        Provider<Course> targetProvider = p -> repository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));
        propertyMapper.setProvider(targetProvider);

        // skip mapping if property value is null
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        // reflect changes in db and return updated entity
        return repository.save(
                modelMapper.map(updatedCourse, Course.class)
        );
    }

    @Transactional
    @EventListener
    public void updateCourseSequence(CourseSequenceChangingEvent event) {
        Course target = repository.findById(event.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));

        Integer usersNumber = target.getUsersNumber();
        Double averageRating = target.getAverageRating();
        Optional<Integer> promotion = Optional.ofNullable(target.getPromotion());
        Integer price = target.getPrice();

        // algorithm for setting course sequence
        double promotionRatio = (promotion.map(integer -> (integer == 0 ? 5 : integer)).orElseGet(() -> (price == 0 ? 20 : price)));
        double sequence = ( averageRating > 4.4 ? pow(averageRating + 1, 2) : pow(averageRating, 2)) * averageRating * usersNumber / promotionRatio;
        target.setSequence(sequence);
    }

    List<CourseInMenu> getOtherParticipantsCourses(Integer targetCourseId, Integer userId) {
        Course targetCourse = repository.findById(targetCourseId)
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));

        List<Integer> otherParticipantIDs = targetCourse.getRatings()
                .stream()
                .filter(rate -> rate.getId().getUserId() != userId) // only users other than the currently logged in are taken into account
                .map(rate -> rate.getId().getUserId())
                .collect(Collectors.toList());

        // find all boughtCourses related with this course' participants to take their other courses in further part of this method
        List<BoughtCourse> source = boughtCourseRepository.findBoughtCoursesById_UserIdIsIn(otherParticipantIDs);

        List<Integer> courseIDs = new ArrayList<>();
        source.stream().map(boughtCourse -> {
            Integer courseId = boughtCourse.getId().getCourseId();
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

}