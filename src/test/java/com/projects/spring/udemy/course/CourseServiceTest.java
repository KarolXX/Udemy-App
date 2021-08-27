package com.projects.spring.udemy.course;

import com.projects.spring.udemy.comment.Comment;
import com.projects.spring.udemy.comment.CommentRepository;
import com.projects.spring.udemy.relationship.CourseRating;
import com.projects.spring.udemy.relationship.CourseRatingKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
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
        var toTest = new CourseService(mockCourseRepo, null, null, null);

        // when
        var exception = catchThrowable(() -> toTest.getCourse(1));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No course with given id");
    }

    @Test
    @DisplayName("should give course with list of userIDs and the number of these users")
    void getCourse_courseAssociatedWithCommentsAndUsers_returnCourseAndUserIDsList() {
        // given
        Course course = mock(Course.class);
        // and
        Set<CourseRating> mockRatings = new HashSet<>(getMockCourseRatings(2, course));
        // and
        when(course.getTitle()).thenReturn("React");
        when(course.getRatings()).thenReturn(mockRatings);
        // and
        CourseRepository mockCourseRepo = mock(CourseRepository.class);
        when(mockCourseRepo.findById(anyInt())).thenReturn(Optional.ofNullable(course));
        // system under test
        var toTest = new CourseService(mockCourseRepo, null, null, null);

        // when
        var result = toTest.getCourse(1);
        // then
        assertThat(result.getCourse()).isEqualTo(course);
        assertThat(result.getUsersNumber()).isEqualTo(2);
    }

    private Set<CourseRating> getMockCourseRatings(int amount, Course course) {
        Set<CourseRating> ratings = new HashSet<>();
        for(int i = amount; i > 0; i--) {
            CourseRating mockCourseRating = mock(CourseRating.class);
            int userID= ThreadLocalRandom.current().nextInt(1, 6 + 1);
            var key = mock(CourseRatingKey.class);
            when(key.getUserId()).thenReturn(userID);
            when(mockCourseRating.getId())
                    .thenReturn(key);
            ratings.add(mockCourseRating);
        }
        return ratings;
    }
}