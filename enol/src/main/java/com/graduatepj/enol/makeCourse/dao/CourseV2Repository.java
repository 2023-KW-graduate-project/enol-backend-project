package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.dto.CourseDto;
import com.graduatepj.enol.makeCourse.vo.CourseV2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseV2Repository extends JpaRepository<CourseV2, String> {
    List<CourseDto> findAllByTime(Integer time);
}
