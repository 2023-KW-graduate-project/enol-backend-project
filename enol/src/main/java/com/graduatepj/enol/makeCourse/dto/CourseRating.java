package com.graduatepj.enol.makeCourse.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRating {
    private String userCode;
    private double rating;
    private String order;
}
