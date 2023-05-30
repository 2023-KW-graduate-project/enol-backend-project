package com.graduatepj.enol.makeCourse.dto;

import com.graduatepj.enol.makeCourse.vo.CourseV2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CourseDto {

    //    private List<String> categories; // c열로 이루어진 리스트
    private boolean mealCheck;

    private String wantedCategoryGroup; // 필수 카테고리가 속하는 categoryGroup
    // 필수 장소(미선택시 C열 기준 우선순위 선정)
    private String wantedCategory; // 필수 카테고리
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
    private double rate; // rate에서 rating으로 이름 Entity와 맞춤, int형에서 double형으로 테이블에 맞춤
    /**
     * 유사도
     */
    private double similarity;

    /**
     * 애도 Course 안쓸거니까 CourseV2써서 다시 만들어야 함
     */
//    public static CourseDto fromEntity(Course course) {
//        return CourseDto.builder()
//                .categories(course.getCategories())
//                .fatigability(course.getFatigability())
//                .specification(course.getSpecification())
//                .activity(course.getActivity())
//                .time(course.getTime())
//                .rate(course.getRate())
//                .build();
//    }


    // 추가
    public static CourseDto fromEntity(CourseV2 course) {
        return CourseDto.builder()
                .categoryGroupCode1(course.getCategoryGroupCode1())
                .categoryGroupCode2(course.getCategoryGroupCode2())
                .categoryGroupCode3(course.getCategoryGroupCode3())
                .categoryGroupCode4(course.getCategoryGroupCode4())
                .fatigability(course.getFatigability())
                .specification(course.getSpecification())
                .activity(course.getActivity())
                .time(course.getTime())
                .rate(course.getRate())
                .build();
    }

    /**
     * 어느 목적이 어떤 카테고리에 속하는지 저장하는 map
     */
    private Map<String, List<Integer>> goalMatch; // 첫번째 string이 C열 카테고리 두번째 string이 속하는 목적

    // DB에 4개 각각 열로 있으므로 하나씩 저장 - findBy로 가져올 것
    private String categoryGroupCode1;
    private String categoryGroupCode2;
    private String categoryGroupCode3;
    private String categoryGroupCode4;

    private boolean dawnDrink;

    private int startTime;
    private int FinishTime;

    public CourseDto() {
    } // 기본 생성자

    // Entity에서 가져오기 위해 만드는 생성자 - CourseV2에서 가져오려고 변수 맞추는 부분
    public CourseDto(String categoryGroupCode1, String categoryGroupCode2, String categoryGroupCode3, String categoryGroupCode4, int fatigability, int specification, int activity) {
        this.categoryGroupCode1 = categoryGroupCode1;
        this.categoryGroupCode2 = categoryGroupCode2;
        this.categoryGroupCode3 = categoryGroupCode3;
        this.categoryGroupCode4 = categoryGroupCode4;
        this.fatigability = fatigability;
        this.specification = specification;
        this.activity = activity;
    }


}
