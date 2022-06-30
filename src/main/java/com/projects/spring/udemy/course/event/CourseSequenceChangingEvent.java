package com.projects.spring.udemy.course.event;

/**
 * Event that occurs when somebody bought or rated course - then sequence changes
 */
public class CourseSequenceChangingEvent {
    private int courseId;

    public CourseSequenceChangingEvent(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
