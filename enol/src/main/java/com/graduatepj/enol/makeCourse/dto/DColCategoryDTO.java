package com.graduatepj.enol.makeCourse.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DColCategoryDTO { // D열이 어떤 목적을 가지는지 보임

    private String category; // D열 카테고리, SmallCategory에 속하는 것들이 이름으로 올 것

    /** 코스별 목적 */ // *****
    private String goal; // D열에 해당하는 카테고리가 각각 어떤 코스 목적을 갖는지 적을 것


    /** 피로도 */
    private int fatigability;

    /** 특이도 */
    private int specification;

    /** 활동성 */
    private int activity;

    /** 시간 */
    private int time;

    /** 별점 */
    private int rate;

    /** 실내외 */
    private int outdoor; // 실내면 0, 실내외면 50, 실외면 100 같은 식으로 구분



}
