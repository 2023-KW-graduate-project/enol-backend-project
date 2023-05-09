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
    private String wantedCategoryCode;
    private List<String> detailCategoryCodes;
    private Boolean mealCheck;
    private int startTime;
    private int endTime;

    public SecondCourse(CourseDto selectedCourse) {
        this.selectedCourse = selectedCourse;
    }
}
