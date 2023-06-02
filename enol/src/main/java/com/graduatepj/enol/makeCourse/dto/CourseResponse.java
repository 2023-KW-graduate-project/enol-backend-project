package com.graduatepj.enol.makeCourse.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {
    private List<PlaceDto> placeDto;
}
