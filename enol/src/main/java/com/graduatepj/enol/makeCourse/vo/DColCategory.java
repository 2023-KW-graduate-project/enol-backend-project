package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "DColCategory")
public class DColCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    private String category;

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


    /** 코스별 목적 */
    private String goal; // D열에 해당하는 카테고리가 각각 어떤 코스 목적을 갖는지 적을 것

}
