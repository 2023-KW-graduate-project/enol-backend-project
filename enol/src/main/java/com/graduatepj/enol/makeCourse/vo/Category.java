package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_code")
    private String categoryCode;

    @Column(name = "name")
    private String name;

    @Column(name = "category_group_code")
    private String categoryGroupCode;

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
    @Column(name = "활동성")
    private int activity;


}
