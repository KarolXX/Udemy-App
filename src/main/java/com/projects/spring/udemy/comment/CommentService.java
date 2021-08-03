package com.projects.spring.udemy.comment;

import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.course.CourseRepository;
import com.projects.spring.udemy.course.dto.CommentWithUserID;
import com.projects.spring.udemy.relationship.CourseRatingKey;
import com.projects.spring.udemy.user.User;
import com.projects.spring.udemy.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class CommentService {
    private CommentRepository repository;
    private CourseRepository courseRepository;
    private UserRepository userRepository;

    public CommentService(CommentRepository repository, CourseRepository courseRepository, UserRepository userRepository) {
        this.repository = repository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void editComment(Integer commentId, Comment source) {
        Comment target = repository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("No comment with given id"));
        target.setText(source.getText());
    }

    public void deleteComment(Integer commentId) {
        Comment target = repository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("No Comment with given id"));

        Course targetCourse = target.getCourse();
        targetCourse.setComments(null);

        User targetUser = target.getUser();
        targetUser.setComments(null);

        target.setCourse(null);
        target.setUser(null);

        repository.deleteById(commentId);
    }

    public Comment createComment(Integer courseId, CommentWithUserID commentWithUserID) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("You can not add comment to non-existing course"));
        User user = userRepository.findById(commentWithUserID.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));
        // create new set of comments ( just adding a new comment to existing ones )
        Set<Comment> updatedComments = new HashSet<>(course.getComments());
        updatedComments.add(commentWithUserID.getComment());
        // keeping both sides of the association between course and comments in-sync
        commentWithUserID.getComment().setCourse(course);
        course.setComments(updatedComments);
        // keeping both sides of the association between user and comments in-sync
        commentWithUserID.getComment().setUser(user);
        user.setComments(updatedComments);
        // saving changes
        return repository.save(commentWithUserID.getComment());
    }

}
