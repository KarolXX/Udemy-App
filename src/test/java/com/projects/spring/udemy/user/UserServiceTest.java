package com.projects.spring.udemy.user;

import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.course.CourseRepository;
import com.projects.spring.udemy.course.CourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Test
    @DisplayName("should throw IllegalArgumentException when no user with given id")
    void findUserById_noUser_throwsIllegalArgumentException() {
        // given
        UserRepository mockUserRepo = mock(UserRepository.class);
        when(mockUserRepo.findById(anyInt())).thenReturn(Optional.empty());
        // system under test
        var toTest = new UserService(mockUserRepo, null, null);

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
        UserRepository mockUserRepo = mock(UserRepository.class);
        when(mockUserRepo.findById(anyInt())).thenReturn(Optional.of(mockUser));
        // system under test
        var toTest = new UserService(mockUserRepo, null, null);

        // when
        var result = toTest.findUserById(1);
        // then
        assertThat(result.getName()).isEqualTo(userName);
        assertThat(result.getBudget()).isEqualTo(0);
    }
}
