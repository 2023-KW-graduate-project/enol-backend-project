package com.graduatepj.enol.makeCourse.dto;

import com.graduatepj.enol.makeCourse.vo.Course;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {

    private List<String> categories; // c열로 이루어진 리스트
    private boolean mealCheck;
    // 필수 장소(미선택시 C열 기준 우선순위 선정)
    private String wantedCategory;
    /**
     * 피로도
     */
    private int fatigability;

    /**
     * 특이도
     */
    private int specification;

    /**
     * 활동성
     */
    private int activity;

    /**
     * 시간
     */
    private int time;

    /**
     * 별점
     */
    private int rate;
    /**
     * 유사도
     */
    private double similarity;

    public static CourseDto fromEntity(Course course) {
        return CourseDto.builder()
                .categories(course.getCategories())
                .fatigability(course.getFatigability())
                .specification(course.getSpecification())
                .activity(course.getActivity())
                .time(course.getTime())
                .rate(course.getRate())
                .build();
    }

    
    // 추가
    /**
     * 어느 목적이 어떤 카테고리에 속하는지 저장하는 map
     */
    private Map<String, String> goalMatch; // 첫번째 string이 C열 카테고리 두번째 string이 속하는 목적
}
