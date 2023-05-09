package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.test.CourseTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CourseTest, String> {
}
