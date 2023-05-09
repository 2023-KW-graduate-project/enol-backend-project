package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.dto.CourseDto;
import com.graduatepj.enol.makeCourse.vo.Course2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Course2Repository extends JpaRepository<Course2, Long> {
    List<CourseDto> findAllByTime(Integer time);
}
