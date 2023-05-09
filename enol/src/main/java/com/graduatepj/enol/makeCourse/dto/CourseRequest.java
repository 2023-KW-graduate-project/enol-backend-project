package com.graduatepj.enol.makeCourse.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseRequest {
    // 혹시 쓸지 몰라서 냅둠.
    private int numPeople;

    // ArrayList<Member> memberList 이건 vo에 있는 Member를 쓴거같은데 이거 쓰면 안됨.
    // vo는 DB랑만 소통하는 전용임. 코스를 만드는데 member의 모든 요소가 필요한것도 아니거니와 가져와서도 안됨.
    // 일단 id로 냅두겠음.
    private List<Long> memberIdList;

    private int startTime;

    private int finishTime;

    private boolean mealCheck;

    /**
     * 추가 - 필수 장소 C열 카테고리
     */
    private String wantedCategoryGroup;

    // 필수 장소(미선택시 C열 기준 우선순위 선정) - D열 카테고리
    private String wantedCategory;

    // 필터링 사용 목적(수치 필터링)
    private List<Boolean> courseKeywords; // 8개

    // 필터링 사용 목적(컷)
    private List<Boolean> goals; // 10개

}
