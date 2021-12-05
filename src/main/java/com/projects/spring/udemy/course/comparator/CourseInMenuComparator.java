package com.projects.spring.udemy.course.comparator;

import com.projects.spring.udemy.course.dto.CourseInMenu;

import java.util.Comparator;

public class CourseInMenuComparator implements Comparator<CourseInMenu> {
    @Override
    public int compare(CourseInMenu o1, CourseInMenu o2) {
        return Double.compare(o1.getOrder(), o2.getOrder());
    }
}
