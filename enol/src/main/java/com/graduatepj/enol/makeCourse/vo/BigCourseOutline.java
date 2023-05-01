package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BigCourseOutline {
    // 데이터베이스에 저장될 큰 코스 틀의 정보를 가져올 것
    // ex) 놀이Main+관계Main+관계sub

    /** 코스 틀 */
    private String courseOutline;

    /** 피로도 */
    private int fatigability;

    /** 특이도 */
    private int specification;

    /** 활동성 */
    private int activity;

    /** 별점 */
    private int rate;

    /** 기타 특징 */
    private String feature;
}
