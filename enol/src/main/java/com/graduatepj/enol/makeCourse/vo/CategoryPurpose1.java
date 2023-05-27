package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity(name = "category_purpose1")
public class CategoryPurpose1 {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_code")
    private String categoryCode;

    @Column(name = "category_group_code")
    private String categoryGroupCode;

    @Column(name = "walk")
    private int walk;

    @Column(name = "drink")
    private int drink;

    @Column(name = "healing")
    private int healing;

    @Column(name = "socializing")
    private int socializing;

    @Column(name = "mood")
    private int mood;

    @Column(name = "novelty")
    private int novelty;

    @Column(name = "daily")
    private int daily;

    @Column(name = "quick")
    private int quick;

    @Column(name = "summer")
    private int summer;

    @Column(name = "cultural")
    private int cultural;
}
