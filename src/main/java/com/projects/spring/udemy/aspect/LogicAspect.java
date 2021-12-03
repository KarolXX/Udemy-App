package com.projects.spring.udemy.aspect;

import com.projects.spring.udemy.relationship.CourseRating;
import com.projects.spring.udemy.relationship.CourseRatingKey;
import com.projects.spring.udemy.relationship.CourseRatingRepository;
import com.sun.jdi.request.InvalidRequestStateException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;

@Aspect
@Component
@Configurable
class LogicAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogicAspect.class);
    private final CourseRatingRepository ratingRepository;

    public LogicAspect(final CourseRatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    // FIXME: this logic is here only for education purposes. It should be in normal code bcs it's not cross cutting concern
    @Around("execution(* com.projects.spring.udemy.course.CourseService.buyCourse(..))")
    Object aroundBuyCourseMethod(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        CourseRatingKey key = (CourseRatingKey) Arrays.stream(args).findFirst().get();
        Boolean isAlreadyBought = ratingRepository.existsById_CourseIdAndId_UserId(
                key.getCourseId(),
                key.getUserId()
        );
        if(!isAlreadyBought) {
            return jp.proceed();
        }
        else {
            throw new InvalidRequestStateException("User already has this course");
        }
    }
}
