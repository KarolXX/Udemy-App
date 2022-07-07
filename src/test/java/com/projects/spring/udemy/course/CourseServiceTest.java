package com.projects.spring.udemy.course;

import com.projects.spring.udemy.InMemoryCourseRepository;
import com.projects.spring.udemy.relationship.BoughtCourse;
import com.projects.spring.udemy.relationship.BoughtCourseKey;
import com.projects.spring.udemy.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CourseServiceTest {

    @Test
    @DisplayName("should throw IllegalArgumentException when no course with given id")
    void getCourse_noCourse_throwIllegalArgumentException() {
        // given
        CourseRepository mockCourseRepo = mock(CourseRepository.class);
        when(mockCourseRepo.findById(anyInt())).thenReturn(Optional.empty());
        // system under test
        var toTest = new CourseService(mockCourseRepo, null, null, null, null, null, null);

        // when
        var exception = catchThrowable(() -> toTest.getCourse(1, 1));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No course with given id");
    }

    @Test
    @DisplayName("should give SingleCourseModel DTO")
    void getCourse_courseAssociatedWithUsers_and_loggedInUserNotAssociatedAndNotWilling_returnCourseAndUserIDsList() {
        // given
        int loggedInUserID = 1; // a user who has neither bought nor liked the course (willing user == user who liked course)
        List<Integer> associatedUserIDs = List.of(4, 5);
        Set<BoughtCourse> boughtCourses = getBoughtCourses(associatedUserIDs);
        // and
        final String title = "React";
        Course course = returnCourseWith(title, boughtCourses, Set.of());
        // and
        CourseRepository mockCourseRepo = mock(CourseRepository.class);
        when(mockCourseRepo.findById(anyInt())).thenReturn(Optional.of(course));
        // system under test
        var toTest = new CourseService(mockCourseRepo, null, null, null, null, null,  null);

        // when
        var result = toTest.getCourse(1, loggedInUserID);
        // then
        assertThat(result.getCourse().getTitle()).isEqualTo(title);
        assertThat(result.getUsersNumber()).isEqualTo(2);
    }

    private Course returnCourseWith(String title, Set<BoughtCourse> ratings, Set<User> willingUsers) {
        Course course = new Course();
        course.setTitle(title);
        course.setRatings(ratings);
        course.setWillingUsers(willingUsers);
        return course;
    }

    private Set<BoughtCourse> getBoughtCourses(List<Integer> userIDs) {
        return userIDs.stream().map(id -> {
            BoughtCourseKey key = new BoughtCourseKey();
            key.setUserId(id);
            BoughtCourse bc = new BoughtCourse();
            bc.setId(key);
            return bc;
        }).collect(Collectors.toSet());
    }

    private InMemoryCourseRepository getMockedCourseRepoWithGivenCourses(List<Course> courses) {
        return new InMemoryCourseRepository(courses);
    }
}