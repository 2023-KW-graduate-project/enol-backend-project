package com.graduatepj.enol.makeCourse.test;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity(name = "Category") // category -> Category로 수정
@Getter
@ToString
public class CourseTest {
    @Id
    private String category_code;
    private String name;
    private String category_group_code;
    /**
     * 피로도
     */
    @Column(name = "피로도")
    private int fatigability;

    /**
     * 특이도
     */
    @Column(name = "특이도")
    private int specification;

    /**
     * 활동성
     */
    @Column(name = "활동도")
    private int activity;

}
