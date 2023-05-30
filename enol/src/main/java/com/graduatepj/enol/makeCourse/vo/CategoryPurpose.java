package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity(name = "category_purpose")
public class CategoryPurpose {

    @Id
    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_group_code")
    private String categoryGroupCode;

    @Column(name = "walk")
    private int walk;

    @Column(name = "socializing")
    private int socializing;

    @Column(name = "NICE_ATMOSPHERE")
    private int niceAtmosphere;

    @Column(name = "healing")
    private int healing;

    @Column(name = "drinking")
    private int drinking;

    @Column(name = "unusual")
    private int unusual;

    @Column(name = "active")
    private int active;

    @Column(name = "daily")
    private int daily;

    @Column(name = "summer")
    private int summer;

    @Column(name = "cultural_life")
    private int culturalLife;
}
