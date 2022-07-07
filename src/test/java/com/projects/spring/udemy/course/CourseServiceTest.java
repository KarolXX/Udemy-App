package com.projects.spring.udemy.course;

import com.projects.spring.udemy.InMemoryCourseRepository;
import com.projects.spring.udemy.relationship.BoughtCourse;
import com.projects.spring.udemy.relationship.BoughtCourseKey;
import com.projects.spring.udemy.user.User;
import com.projects.spring.udemy.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CourseServiceTest {

    @Test
    @DisplayName("should throw IllegalArgumentException when no course with given id")
    void getCourse_noCourse_throwsIllegalArgumentException() {
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
    @DisplayName("should return SingleCourseModel DTO with the null rate of logged in user and with likedCourse flag set to false")
    void getCourse_courseAssociatedWithUsers_and_loggedInUserNotAssociatedAndNotWilling_returnSingleCourseModelDTO() {
        // given
        int loggedInUserID = 1; // a user who has neither bought nor liked the course (willing user == user who liked course)
        int courseID = 1;
        // and
        Map<Integer, Double> usersRate = new HashMap<>();
        usersRate.put(3, null); // sample associations
        usersRate.put(4, 2.5); // sample associations
        Set<BoughtCourse> ratings = getAssociationsBetweenUserAndCourse(courseID, usersRate);
        // and
        final String title = "React";
        Set<Integer> willingUsersIDs = Set.of(); // no willing users
        Course course = returnCourseWith(courseID, title, ratings, willingUsersIDs);
        // and
        CourseRepository mockCourseRepo = mock(CourseRepository.class);
        when(mockCourseRepo.findById(courseID)).thenReturn(Optional.of(course));
        // system under test
        var toTest = new CourseService(mockCourseRepo, null, null, null, null, null,  null);

        // when
        var result = toTest.getCourse(courseID, loggedInUserID);

        // then
        assertThat(result.getCourse().getTitle()).isEqualTo(title);
        assertThat(result.getUserRate()).isEqualTo(null);
        assertThat(result.getUsersNumber()).isEqualTo(2);
        assertThat(result.isLikedCourse()).isEqualTo(false);
    }

    @Test
    @DisplayName("should return SingleCourseModel DTO with the rate of logged in user and with likedCourse flag set to false")
    void getCourse_courseAssociatedWithUsers_and_loggedInUserBoughtCourse_and_noWillingUsers_returnsSingleCourseModelDTO_and_loggedInUserRate() {
        // given
        int loggedUserID = 1; // a user who has bought but hasn't liked the course
        Double loggedUserRate = 3.5;
        int courseID = 1;
        // and
        Map<Integer, Double> usersRate = new HashMap<>();
        usersRate.put(loggedUserID, loggedUserRate);
        usersRate.put(4, 2.5); // another sample associations
        Set<BoughtCourse> ratings = getAssociationsBetweenUserAndCourse(courseID, usersRate);
        // and
        final String title = "React";
        Set<Integer> willingUsersIDs = Set.of(); // no willing users
        Course course = returnCourseWith(courseID, title, ratings, willingUsersIDs);
        // and
        CourseRepository mockCourseRepo = mock(CourseRepository.class);
        when(mockCourseRepo.findById(1)).thenReturn(Optional.of(course));
        // system under test
        var toTest = new CourseService(mockCourseRepo, null, null, null, null, null,  null);

        // when
        var result = toTest.getCourse(courseID, loggedUserID);

        // then
        assertThat(result.getCourse().getTitle()).isEqualTo(title);
        assertThat(result.getUserRate()).isEqualTo(Optional.of(loggedUserRate));
        assertThat(result.getUsersNumber()).isEqualTo(2);
        assertThat(result.isLikedCourse()).isEqualTo(false);
    }

    @Test
    @DisplayName("should return SingleCourseModel DTO with the rate of logged in user and with likedCourse flag set to true")
    void getCourse_courseAssociatedWithUsers_and_loggedInUserBoughtAndLikedCourse_and_willingUsers_returnsSingleCourseModelDTO_and_loggedInUserRate() {
        // given
        int loggedUserID = 1; // a user who has bought and liked the course
        Double loggedUserRate = 4.5;
        int courseID = 1;
        // and
        Map<Integer, Double> usersRate = new HashMap<>();
        usersRate.put(loggedUserID, loggedUserRate);
        usersRate.put(4, 2.5); // another sample associations
        Set<BoughtCourse> ratings = getAssociationsBetweenUserAndCourse(courseID, usersRate);
        // and
        final String title = "Spring";
        Set<Integer> willingUsersIDs = Set.of(7, loggedUserID, 4, 11);
        Course course = returnCourseWith(courseID, title, ratings, willingUsersIDs);
        // and
        CourseRepository mockCourseRepo = mock(CourseRepository.class);
        when(mockCourseRepo.findById(1)).thenReturn(Optional.of(course));
        // system under test
        var toTest = new CourseService(mockCourseRepo, null, null, null, null, null,  null);

        // when
        var result = toTest.getCourse(courseID, loggedUserID);

        // then
        assertThat(result.getCourse().getTitle()).isEqualTo(title);
        assertThat(result.getUserRate()).isEqualTo(Optional.of(loggedUserRate));
        assertThat(result.isLikedCourse()).isEqualTo(true);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when no user with given id")
    void buyCourse_noUser_throwsIllegalArgumentException() {
        // given
        UserRepository mockUserRepo = mock(UserRepository.class);
        when(mockUserRepo.findById(anyInt())).thenReturn(Optional.empty());
        // and
        BoughtCourseKey argument = new BoughtCourseKey(1, 1);
        // system under test
        var toTest = new CourseService(null, mockUserRepo, null, null, null, null, null);

        // when
        var exception = catchThrowable(() -> toTest.buyCourse(argument));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No user with given id");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when user exists and no course with given id")
    void buyCourse_userExists_and_noCourse_throwsIllegalArgumentException() {
        // given
        UserRepository mockUserRepo = mock(UserRepository.class);
        User user = new User();
        when(mockUserRepo.findById(anyInt())).thenReturn(Optional.of(user));
        // and
        CourseRepository mockCourseRepo = mock(CourseRepository.class);
        when(mockCourseRepo.findById(anyInt())).thenReturn(Optional.empty());
        // and
        BoughtCourseKey argument = new BoughtCourseKey(1, 1);
        // system under test
        var toTest = new CourseService(mockCourseRepo, mockUserRepo, null, null, null, null, null);

        // when
        var exception = catchThrowable(() -> toTest.buyCourse(argument));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No course with given id");
    }

    private Course returnCourseWith(Integer id, String title, Set<BoughtCourse> ratings, Set<Integer> willingUsersIDs) {
        Course course = new Course();
        course.setTitle(title);
        course.setRatings(ratings);
        Set<User> willingUsers = willingUsersIDs.stream()
                .map(this::returnUserWith)
                .collect(Collectors.toSet());
        course.setWillingUsers(willingUsers);
        // no public setter for Id so use reflection
        try {
            var field = Course.class.getDeclaredField("courseId");
            field.setAccessible(true);
            field.set(course, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return course;
    }

    private User returnUserWith(Integer id) {
        User user = new User();
        // no public setter for Id so use reflection
        try {
            var field = User.class.getDeclaredField("userId");
            field.setAccessible(true);
            field.set(user, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    private Set<BoughtCourse> getAssociationsBetweenUserAndCourse(Integer courseID, Map<Integer, Double> usersRate) {
        return usersRate.entrySet().stream()
                .map(e -> {
                    // take id and rate
                    Integer userID = e.getKey();
                    Double rate = e.getValue();
                    // create association key
                    BoughtCourseKey key = new BoughtCourseKey();
                    key.setUserId(userID);
                    key.setCourseId(courseID);
                    // create association
                    BoughtCourse bc = new BoughtCourse();
                    bc.setId(key);
                    bc.setRating(rate);
                    return bc;
                }).collect(Collectors.toSet());
    }

    private InMemoryCourseRepository getMockedCourseRepoWithGivenCourses(List<Course> courses) {
        return new InMemoryCourseRepository(courses);
    }
}