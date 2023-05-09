package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.dto.CourseDto;
import com.graduatepj.enol.makeCourse.vo.Course4;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Course4Repository extends JpaRepository<Course4, Long> {
    List<CourseDto> findAllByTime(Integer time);
}
