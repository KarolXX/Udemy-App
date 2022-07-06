package com.projects.spring.udemy.user;

import com.projects.spring.udemy.InMemoryCourseRepository;
import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.course.dto.CourseInMenu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        User mockUser = mock(User.class);
        when(mockUser.getName()).thenReturn(userName);
        // and
        UserRepository mockUserRepo = userRepositoryReturning(mockUser);
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
        // two mocked courses and only one of them is the liked one
        Course mockC1 = mock(Course.class);
        when(mockC1.getId()).thenReturn(1);
        Course mockLikedC2 = mock(Course.class); // note that this is the only liked course
        int likedC2Id = 2;
        when(mockLikedC2.getId()).thenReturn(likedC2Id);
        List<Course> sampleCourses = List.of(mockC1, mockLikedC2);
        Set<Course> likedCourses = Set.of(mockLikedC2);
        // and
        User user = mock(User.class);
        when(user.getUserId()).thenReturn(1);
        when(user.getLikedCourses()).thenReturn(likedCourses);
        // and
        var userRepoMock = userRepositoryReturning(user);
        // and
        var courseRepoMock = getMockedCourseRepoWithGivenCourses(sampleCourses);

        // system under test
        var underTest = new UserService(userRepoMock, courseRepoMock);

        // when
        List<CourseInMenu> result = underTest.getUserFavouriteCourses(1);

        // then
        assertThat(result.size()).isEqualTo(1); // only one liked course
        assertThat(result.get(0).getId()).isEqualTo(likedC2Id);
    }

    private InMemoryCourseRepository getMockedCourseRepoWithGivenCourses(List<Course> courses) {
        return new InMemoryCourseRepository(courses);
    }

    private UserRepository userRepositoryReturning(User user) {
        var userRepoMock = mock(UserRepository.class);
        when(userRepoMock.findById(anyInt()))
                .thenReturn(Optional.ofNullable(user));
        return userRepoMock;
    }

}
