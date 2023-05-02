package com.graduatepj.enol.makeCourse.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecondCourse {
    private CourseDto selectedCourse;
    private List<String> detailCategories;

    public SecondCourse(CourseDto selectedCourse) {
        this.selectedCourse = selectedCourse;
    }
}
