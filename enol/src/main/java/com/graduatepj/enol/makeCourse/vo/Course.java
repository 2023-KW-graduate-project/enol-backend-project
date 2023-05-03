package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "COURSE")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    private List<String> categories;

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


    // 추가
    /** 실내외 */
    private int outdoor; // 실내면 0, 실내외면 50, 실외면 100 같은 식으로 구분

    /** 코스별 목적 */
    private String goals; // 전체 계산해서 목적에 맞는 C열 카테고리가 1개 혹은 2개 이상 포함되면 해당 목적을 갖도록 해서 데이터베이스에 저장

//    /** 코스별 키워드 */
//    private String keywords; // 속성값 계산해서 정해진 값 N보다 크거나 작게 키워드를 설정할 경우 사용
}
