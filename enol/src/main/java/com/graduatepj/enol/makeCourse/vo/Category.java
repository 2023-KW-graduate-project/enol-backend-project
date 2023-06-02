package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "category")
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
    @Column(name = "fatigue")
    private int fatigability;

    /**
     * 특이도
     */
    @Column(name = "unique")
    private int specification;

    /**
     * 활동성
     */
    @Column(name = "activity")
    private int activity;


}
