package com.projects.spring.udemy.user;

import com.projects.spring.udemy.InMemoryCourseRepository;
import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.course.dto.CourseInMenu;
import com.projects.spring.udemy.user.dto.TransferMoney;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Test
    @DisplayName("should throw IllegalArgumentException when no user with given id")
    void findUserById_noUser_throwsIllegalArgumentException() {
        // given
        UserRepository mockUserRepo = userRepositoryReturning(null);
        // system under test
        var toTest = new UserService(mockUserRepo, null);

        // when
        var exception = catchThrowable(() -> toTest.findUserById(1));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No user with given id");
    }

    @Test
    @DisplayName("should return user")
    void findUserById_userExists_throwsIllegalArgumentException() {
        // given
        String userName = "Name";
        User user = new User();
        user.setName(userName);
        // and
        UserRepository mockUserRepo = userRepositoryReturning(user);
        // system under test
        var toTest = new UserService(mockUserRepo, null);

        // when
        var result = toTest.findUserById(1);
        // then
        assertThat(result.getName()).isEqualTo(userName);
        assertThat(result.getBudget()).isEqualTo(0);
    }

    @Test
    @DisplayName("should return list of CourseInMenu DTO")
    void getUserFavouriteCourses_userAndCoursesExist_returnsListOfCourseInMenuDTO() {
        // given
        // two courses and only one of them is the liked one
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(1);
        Course likeCourse = mock(Course.class); // note that this is the only liked course
        int likedC2Id = 2;
        when(likeCourse.getId()).thenReturn(likedC2Id);
        List<Course> sampleCourses = List.of(course, likeCourse);
        Set<Course> likedCourses = Set.of(likeCourse);
        // and
        User mockUser = mock(User.class);
        when(mockUser.getUserId()).thenReturn(1);
        when(mockUser.getLikedCourses()).thenReturn(likedCourses);
        // and
        var userRepoMock = userRepositoryReturning(mockUser);
        // and
        var courseRepoMock = getMockedCourseRepoWithGivenCourses(sampleCourses);

        // system under test
        var toTest = new UserService(userRepoMock, courseRepoMock);

        // when
        List<CourseInMenu> result = toTest.getUserFavouriteCourses(1);

        // then
        assertThat(result.size()).isEqualTo(1); // only one liked course
        assertThat(result.get(0).getId()).isEqualTo(likedC2Id);
    }

    @Test
    @DisplayName("should add given a given amount of money to that user account")
    void addMoneyToUserBudget_userExists_addsMoneyToUserBudget() {
        // given
        int budgetBefore = 100;
        int amount = 100;
        TransferMoney transferMoney = new TransferMoney(amount);
        // and
        final User user = new User();
        user.setUserId(1);
        user.setBudget(budgetBefore);
        // and
        var userRepoMock = userRepositoryReturning(user);
        // system under test
        var toTest = new UserService(userRepoMock, null);

        // when
        toTest.addMoneyToUserBudget(1, transferMoney);

        // then
        assertThat(user.getBudget()).isEqualTo(budgetBefore + amount);
    }

    private InMemoryCourseRepository getMockedCourseRepoWithGivenCourses(List<Course> courses) {
        return new InMemoryCourseRepository(courses);
    }

    // mocked userRepository that serve findById method
    private UserRepository userRepositoryReturning(User user) {
        var userRepoMock = mock(UserRepository.class);
        when(userRepoMock.findById(anyInt()))
                .thenReturn(Optional.ofNullable(user));
        return userRepoMock;
    }

}
