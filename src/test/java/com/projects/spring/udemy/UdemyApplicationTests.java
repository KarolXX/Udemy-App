package com.projects.spring.udemy;

import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.course.CourseRepository;
import net.sf.ehcache.CacheManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;

@SpringBootTest
class UdemyApplicationTests {
	@Autowired
	private CourseRepository repository;

	@Test
	void contextLoads() {
	}

}
