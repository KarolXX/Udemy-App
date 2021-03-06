package com.projects.spring.udemy;

import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.course.CourseRepository;
import com.projects.spring.udemy.course.dto.CourseInMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class InMemoryCourseRepository implements CourseRepository {
    // this map is sample db
    Map<Integer, Course> courses = new HashMap<>();
    int size = 0;

    InMemoryCourseRepository() {
    }

    // constructor that adds sample courses to db
    public InMemoryCourseRepository(List<Course> source) {
        source.forEach(course -> {
            courses.put(++size, course);
        });
    }

    @Override
    public List<CourseInMenu> getCourseMenuByIdIsIn(List<Integer> courseIDs) {
        List<CourseInMenu> result = new ArrayList<>();
        // Maybe for simplicity just use course.getCourseId() to create list of given IDs
        for (Map.Entry<Integer, Course> entry : courses.entrySet()) {
            if (courseIDs.contains(entry.getKey())) {
                Course c = entry.getValue();
                // this would be more readable with a dedicated constructor that takes the Course entity and gets its properties, but the production code doesn't need it, so it would be a bit silly to change the production code for testing
                CourseInMenu inMenu = new CourseInMenu(c.getId(), c.getTitle(), c.getAverageRating(), c.getUsersNumber(), c.getPrice(), c.getPromotion(), null, c.getAverageRating());
                result.add(inMenu);
            }
        }
        return result;
    }

    @Override
    public Optional<Course> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void updateCourseAverageRating(Integer courseId) {

    }

    @Override
    public List<CourseInMenu> getCourseMenu() {
        return null;
    }

    @Override
    public Page<CourseInMenu> getCourseMenu(Pageable pageable) {
        return null;
    }

    @Override
    public Course save(Course course) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }
}
