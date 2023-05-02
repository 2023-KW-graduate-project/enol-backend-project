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
}
