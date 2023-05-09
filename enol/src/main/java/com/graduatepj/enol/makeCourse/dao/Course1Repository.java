package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.dto.CourseDto;
import com.graduatepj.enol.makeCourse.vo.Course1;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Course1Repository extends JpaRepository<Course1, Long> {
    List<CourseDto> findAllByTime(Integer time);
}
