package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.dto.CourseDto;
import com.graduatepj.enol.makeCourse.vo.Course3;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Course3Repository extends JpaRepository<Course3, Long> {

    List<CourseDto> findAllByTime(Integer time);

}
