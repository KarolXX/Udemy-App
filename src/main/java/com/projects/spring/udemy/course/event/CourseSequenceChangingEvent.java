package com.projects.spring.udemy.course.event;

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
