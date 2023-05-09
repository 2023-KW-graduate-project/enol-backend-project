package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "course_1")
public class Course1 {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private String id; // Course_id

    /** C열 카테고리 1개 */
    @Column(name = "category_group_code_1")
    private String categoryGroupCode1;

    /** 피로도 */
    @Column(name = "total_피로도")
    private int fatigability;

    /** 특이도 */
    @Column(name = "total_특이도")
    private int specification;

    /** 활동성 */
    @Column(name = "total_활동성")
    private int activity;

    /** 시간 */
    @Column(name = "total_time")
    private int time;


}
