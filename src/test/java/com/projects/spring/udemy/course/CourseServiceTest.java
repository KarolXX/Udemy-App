package com.projects.spring.udemy.course;

import com.projects.spring.udemy.InMemoryRepositoryConfiguration;
import com.projects.spring.udemy.author.Author;
import com.projects.spring.udemy.author.AuthorRepository;
import com.projects.spring.udemy.course.dto.CourseInMenu;
import com.projects.spring.udemy.course.event.CourseSequenceChangingEvent;
import com.projects.spring.udemy.relationship.BoughtCourse;
import com.projects.spring.udemy.relationship.BoughtCourseKey;
import com.projects.spring.udemy.relationship.BoughtCourseRepository;
import com.projects.spring.udemy.user.User;
import com.projects.spring.udemy.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest // to enable autowiring but it slows down unit tests, maybe there is another solution ?
class CourseServiceTest {

    @Autowired
    private InMemoryRepositoryConfiguration configuration;

    ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

    @Test
    @DisplayName("should throw IllegalArgumentException when no course with given id")
    void getCourse_noCourse_throwsIllegalArgumentException() {
        // given
        CourseRepository mockCourseRepo =  getCourseRepoWithFindByIdReturning(null);
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
        Set<BoughtCourse> ratings = getAssociationsBetweenUsersAndCourse(courseID, usersRate);
        // and
        final String title = "React";
        Set<Integer> willingUsersIDs = Set.of(); // no willing users
        Course course = returnCourseWith(courseID, title, ratings, willingUsersIDs, 0, null);
        // and
        CourseRepository mockCourseRepo =  getCourseRepoWithFindByIdReturning(course);
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
        Set<BoughtCourse> ratings = getAssociationsBetweenUsersAndCourse(courseID, usersRate);
        // and
        final String title = "React";
        Set<Integer> willingUsersIDs = Set.of(); // no willing users
        Course course = returnCourseWith(courseID, title, ratings, willingUsersIDs, 0, null);
        // and
        CourseRepository mockCourseRepo =  getCourseRepoWithFindByIdReturning(course);
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
        Set<BoughtCourse> ratings = getAssociationsBetweenUsersAndCourse(courseID, usersRate);
        // and
        final String title = "Spring";
        Set<Integer> willingUsersIDs = Set.of(7, loggedUserID, 4, 11);
        Course course = returnCourseWith(courseID, title, ratings, willingUsersIDs, 0, null);
        // and
        CourseRepository mockCourseRepo =  getCourseRepoWithFindByIdReturning(course);
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
        UserRepository mockUserRepo = getUserRepoWithFindByIdReturning(null);
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
        User user = new User();
        UserRepository mockUserRepo = getUserRepoWithFindByIdReturning(user);
        // and
        CourseRepository mockCourseRepo =  getCourseRepoWithFindByIdReturning(null);
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

    @Test
    @DisplayName("should throw NotEnoughMoneyAvailableException when user and course exist but user has no enough money and course has no promotion")
    void buyCourse_userAndCourseExist_and_userHasNoEnoughMoney_and_noPromotion_throwsNotEnoughMoneyAvailableException() {
        // given
        Integer budget = 100;
        Integer price = 150;
        Integer promotion = null;
        // and
        User user = returnUserWith(1, null, budget);
        UserRepository mockUserRepo = getUserRepoWithFindByIdReturning(user);
        // and
        Course course = returnCourseWith(1, "", null, Set.of(), price, promotion);
        CourseRepository mockCourseRepo =  getCourseRepoWithFindByIdReturning(course);
        // and
        BoughtCourseKey argument = new BoughtCourseKey(1, 1);
        // system under test
        var toTest = new CourseService(mockCourseRepo, mockUserRepo, null, null, null, null, null);

        // when
        var exception = catchThrowable(() -> toTest.buyCourse(argument));

        // then
        assertThat(exception)
                .isInstanceOf(NotEnoughMoneyAvailableException.class)
                .hasMessage("You don't have enough money on the account to purchase this course");
    }

    @Test
    @DisplayName("should throw NotEnoughMoneyAvailableException when user and course exist but user has no enough money and course has promotion")
    void buyCourse_userAndCourseExist_and_userHasNoEnoughMoney_and_isPromotion_throwsNotEnoughMoneyAvailableException() {
        // given
        Integer budget = 100;
        Integer price = 150;
        Integer promotion = 120;
        // and
        User user = returnUserWith(1, null, budget);
        UserRepository mockUserRepo = getUserRepoWithFindByIdReturning(user);
        // and
        Course course = returnCourseWith(1, "", null, Set.of(), price, promotion);
        CourseRepository mockCourseRepo =  getCourseRepoWithFindByIdReturning(course);
        // and
        BoughtCourseKey argument = new BoughtCourseKey(1, 1);
        // system under test
        var toTest = new CourseService(mockCourseRepo, mockUserRepo, null, null, null, null, null);

        // when
        var exception = catchThrowable(() -> toTest.buyCourse(argument));

        // then
        assertThat(exception)
                .isInstanceOf(NotEnoughMoneyAvailableException.class)
                .hasMessage("You don't have enough money on the account to purchase this course");
    }

    @Test
    @DisplayName("should create an association and publish the event when the user and course exist and no need to send money")
    void buyCourse_userAndCourseExist_and_courseForFree_createsAndSavesAssociation_and_publishCourseSequenceChangingEvent() {
        // given
        Integer budget = 0;
        Integer price = 0;
        // and
        User user = returnUserWith(1, null, budget);
        UserRepository mockUserRepo = getUserRepoWithFindByIdReturning(user);
        // and
        Course course = returnCourseWith(1, "", null, Set.of(), price, null);
        CourseRepository mockCourseRepo = getCourseRepoWithFindByIdReturning(course);
        // and
        var bcRepo = configuration.getInMemoryBoughtCourseRepository();
        // and
        BoughtCourseKey argument = new BoughtCourseKey(1, 1);

        // system under test
        var toTest = new CourseService(mockCourseRepo, mockUserRepo, bcRepo, null, null, null, eventPublisher);

        // when
        toTest.buyCourse(argument);

        // then
        assertThat(bcRepo.getSize()).isEqualTo(1); // check if association was saved
        verify(eventPublisher).publishEvent(any(CourseSequenceChangingEvent.class)); // check if event was published
    }

    @Test
    @DisplayName("should create an association, send money from user to author's budget  and publish the event when the course and user with enough amount of money exist")
    void buyCourse_userAndCourseExist_and_userHasEnoughMoney_createsAndSavesAssociation_and_publishCourseSequenceChangingEvent_and_sendsMoneyToAuthor() {
        // given
        Integer budget = 150;
        Integer price = 100;
        // and
        User user = returnUserWith(1, null, budget);
        UserRepository mockUserRepo = getUserRepoWithFindByIdReturning(user);
        // and
        Author author = new Author();
        int initialAuthorBudget = 0;
        author.setBudget(initialAuthorBudget);
        AuthorRepository authorRepo = getAuthorRepoWithFindByIdReturning(author);
        // and
        Course course = returnCourseWith(1, "", null, Set.of(), price, null);
        CourseRepository mockCourseRepo = getCourseRepoWithFindByIdReturning(course);
        // and
        var bcRepo = configuration.getInMemoryBoughtCourseRepository();
        // and
        BoughtCourseKey argument = new BoughtCourseKey(1, 1);

        // system under test
        var toTest = new CourseService(mockCourseRepo, mockUserRepo, bcRepo, authorRepo, null, null, eventPublisher);

        // when
        toTest.buyCourse(argument);

        // then
        assertThat(bcRepo.getSize()).isEqualTo(1); // check if association was saved
        assertThat(author.getBudget()).isEqualTo(initialAuthorBudget + price);
        verify(eventPublisher).publishEvent(any(CourseSequenceChangingEvent.class)); // check if event was published
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when association doesn't exist")
    void rateCourse_noAssociation_throwsIllegalArgumentException() {
        // given
        BoughtCourse association = new BoughtCourse();
        var bcRepo = configuration.getInMemoryBoughtCourseRepository();

        // system under test
        var toTest = new CourseService(null, null, bcRepo, null, null, null, null);

        // when
        var exception = catchThrowable(() -> toTest.rateCourse(association));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Buy course before rating");
    }

    @Test
    @DisplayName("should rate an BoughtCourse association (between User and Course) and publish the event when association exists")
    void rateCourse_associationExists_assignsRating_and_publishCourseSequenceChangingEvent() {
        // given
        Integer courseID = 1;
        Course course = returnCourseWith(courseID, "", Set.of(), Set.of(), 0, 0);
        // and
        BoughtCourse association = new BoughtCourse();
        BoughtCourseKey associationID = new BoughtCourseKey(1, courseID);
        association.setId(associationID);
        Double rating = 3.5;
        association.setRating(rating);
        // and
        CourseRepository courseRepo = configuration.getInMemoryCourseRepositoryWith(List.of(course));
        // and
        var bcRepo = configuration.getInMemoryBoughtCourseRepositoryWith(List.of(association));

        // system under test
        var toTest = new CourseService(courseRepo, null, bcRepo, null, null, null, eventPublisher);

        // when
        Double result = toTest.rateCourse(association);

        // then
        assertThat(result).isEqualTo(rating);
        verify(eventPublisher).publishEvent(any(CourseSequenceChangingEvent.class));
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when association doesn't exist")
    void updateCourseSequence_noCourse_throwsIllegalArgumentException() {
        // given
        CourseRepository courseRepo = getCourseRepoWithFindByIdReturning(null);
        CourseSequenceChangingEvent event = new CourseSequenceChangingEvent(1);
        // system under test
        var toTest = new CourseService(courseRepo, null, null, null, null, null, null);

        // when
        var exception = catchThrowable(() -> toTest.updateCourseSequence(event));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No course with given id");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when association doesn't exist")
    void updateCourseSequence_courseExists_updatesCourseSequence() {
        // given
        CourseRepository courseRepo = getCourseRepoWithFindByIdReturning(null);
        CourseSequenceChangingEvent event = new CourseSequenceChangingEvent(1);
        // system under test
        var toTest = new CourseService(courseRepo, null, null, null, null, null, null);

        // when
        var exception = catchThrowable(() -> toTest.updateCourseSequence(event));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No course with given id");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when association doesn't exist")
    void getOtherParticipantsCourses_noCourse_throwsIllegalArgumentException() {
        // given
        CourseRepository courseRepo = getCourseRepoWithFindByIdReturning(null);

        // system under test
        var toTest = new CourseService(courseRepo, null, null, null, null, null, null);

        // when
        var exception = catchThrowable(() -> toTest.getOtherParticipantsCourses(1, 1));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No course with given id");
    }

    @Test
    @DisplayName("should return List of CourseInMenu when target course with participants exists and those participants have other courses")
    void getOtherParticipantsCourses_courseExists_andOtherParticipantsCoursesExists_returnsOtherParticipantsCourses() {
        // given
        Integer loggedUserID = 1;
        Integer targetCourseID = 1;
        BoughtCourse bc0 = new BoughtCourse(new BoughtCourseKey(loggedUserID, targetCourseID));
        BoughtCourse bc1 = new BoughtCourse(new BoughtCourseKey(loggedUserID, 2));
        BoughtCourse bc2 = new BoughtCourse(new BoughtCourseKey(2, targetCourseID));
        BoughtCourse bc3 = new BoughtCourse(new BoughtCourseKey(3, targetCourseID));
        BoughtCourse bc4 = new BoughtCourse(new BoughtCourseKey(2, 2));
        BoughtCourse bc5 = new BoughtCourse(new BoughtCourseKey(3, 2));
        BoughtCourse bc6 = new BoughtCourse(new BoughtCourseKey(3, 3));
        var bcRepo = configuration.getInMemoryBoughtCourseRepositoryWith(List.of(bc0, bc2, bc3, bc4, bc5, bc6));
        // and
        Course targetCourse = returnCourseWith(1, "", Set.of(bc0, bc2, bc3), Set.of(), 0, 0);
        Course course2 = returnCourseWith(2, "", Set.of(bc1, bc4, bc5), Set.of(), 0, 0);
        Course course3 = returnCourseWith(3, "", Set.of(bc6), Set.of(), 0, 0);
        int otherCoursesNumber = 2;
        CourseRepository courseRepo = configuration.getInMemoryCourseRepositoryWith(List.of(targetCourse, course2, course3));

        // system under test
        var toTest = new CourseService(courseRepo, null, bcRepo, null, null, null, null);

        // when
        var result = toTest.getOtherParticipantsCourses(targetCourseID, loggedUserID);

        // then
        assertThat(result.size()).isEqualTo(otherCoursesNumber);
        List<Integer> otherCoursesIDs = result.stream()
                .map(CourseInMenu::getId)
                .collect(Collectors.toList());
        assertTrue(otherCoursesIDs.contains(2));
        assertTrue(otherCoursesIDs.contains(3));
        assertFalse(otherCoursesIDs.contains(1)); // this is target course and this method looks for other
    }

    private CourseRepository getCourseRepoWithFindByIdReturning(Course course) {
        CourseRepository mockCourseRepo = mock(CourseRepository.class);
        when(mockCourseRepo.findById(anyInt())).thenReturn(Optional.ofNullable(course));
        return mockCourseRepo;
    }

    private UserRepository getUserRepoWithFindByIdReturning(User user) {
        UserRepository mockUserRepo = mock(UserRepository.class);
        when(mockUserRepo.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        return mockUserRepo;
    }

    private AuthorRepository getAuthorRepoWithFindByIdReturning(Author author) {
        AuthorRepository mockAuthorRepo = mock(AuthorRepository.class);
        when(mockAuthorRepo.findAuthorCourseByCourseId(anyInt())).thenReturn(Optional.ofNullable(author));
        return mockAuthorRepo;
    }

    private Course returnCourseWith(Integer id, String title, Set<BoughtCourse> ratings, Set<Integer> willingUsersIDs, Integer price, Integer promotion) {
        Course course = new Course();
        course.setTitle(title);
        course.setPrice(price);
        course.setPromotion(promotion);
        course.setRatings(ratings);
        Set<User> willingUsers = willingUsersIDs.stream()
                .map(userID -> returnUserWith(userID, null, 0))
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

    private User returnUserWith(Integer id, Set<BoughtCourse> courses, Integer budget) {
        User user = new User();
        user.setCourses(courses);
        user.setBudget(budget);
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


    private Set<BoughtCourse> getAssociationsBetweenUsersAndCourse(Integer courseID, Map<Integer, Double> usersRate) {
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

}