package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "Category_group")
public class CategoryGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_group_code")
    private String categoryGroupCode;

    @Column(name = "function")
    private String function;

    @Column(name = "priority")
    private String priority;

    @Column(name = "sub_function")
    private String subFunction;

    @Column(name="time")
    private String time;

    /**
     * 피로도
     */
    @Column(name = "avg_피로도")
    private int avgFatigability;

    /**
     * 특이도
     */
    @Column(name = "avg_특이도")
    private int avgSpecification;

    /**
     * 활동성
     */
    @Column(name = "avg_활동성")
    private int avgActivity;

}
