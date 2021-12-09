package com.projects.spring.udemy.course.event;

public class CourseOrderChangedEvent {
    private int courseId;

    public CourseOrderChangedEvent(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
