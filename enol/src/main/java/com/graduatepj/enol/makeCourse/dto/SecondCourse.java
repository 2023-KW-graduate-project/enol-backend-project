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
    private String courseId;
    private double rating;
    private String userCode;

    // 추가
    private String wantedCategoryGroupCode;
    private Boolean dawnDrink;

//    public SecondCourse(CourseDto selectedCourse) {
//        this.selectedCourse = selectedCourse;
//    }
    
    // history를 위한 course_id, rating 추가해야 함

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

        this.courseId = selectedCourse.getCourseId();
        this.userCode = selectedCourse.getUserCode();

        this.rating = selectedCourse.getRate();
    }
}
