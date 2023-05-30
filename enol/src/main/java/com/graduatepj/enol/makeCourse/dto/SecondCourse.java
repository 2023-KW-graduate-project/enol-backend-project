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
    private String wantedCategoryName;
    private List<String> detailCategoryNames;
    private List<String> CategoryGroupCodes;
    private Boolean mealCheck;
    private int startTime;
    private int endTime;

    // 추가
    private String wantedCategoryGroupCode;
    private Boolean dawnDrink;

//    public SecondCourse(CourseDto selectedCourse) {
//        this.selectedCourse = selectedCourse;
//    }

    public SecondCourse(CourseDto selectedCourse, List<String> course) {
        this.selectedCourse = selectedCourse;
        this.wantedCategoryGroupCode = selectedCourse.getWantedCategoryGroup();
        this.wantedCategoryName = selectedCourse.getWantedCategory();

        this.mealCheck = selectedCourse.isMealCheck();
        this.dawnDrink = selectedCourse.isDawnDrink();

        // CategoryGroupCodes 채우기
        this.CategoryGroupCodes = course;

        this.startTime = selectedCourse.getStartTime();
        this.endTime = selectedCourse.getFinishTime();
    }
}
