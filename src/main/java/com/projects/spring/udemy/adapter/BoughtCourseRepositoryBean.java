package com.projects.spring.udemy.adapter;

import com.projects.spring.udemy.relationship.BoughtCourse;
import com.projects.spring.udemy.relationship.BoughtCourseKey;
import com.projects.spring.udemy.relationship.BoughtCourseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BoughtCourseRepositoryBean extends BoughtCourseRepository, JpaRepository<BoughtCourse, BoughtCourseKey> {
}
