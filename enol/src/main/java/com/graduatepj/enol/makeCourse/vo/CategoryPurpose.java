package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "category_purpose")
public class CategoryPurpose {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_code")
    private String categoryCode;

    @Column(name = "category_group_code")
    private String categoryGroupCode;

    @Column(name = "산책")
    private int walk;

    @Column(name = "음주")
    private int drink;

    @Column(name = "체험")
    private int experience;

    @Column(name = "힐링")
    private int healing;

    @Column(name = "관람")
    private int watch;

    @Column(name = "지적활동")
    private int intellectual;

    @Column(name = "경치")
    private int view;

    @Column(name = "일반")
    private int normal;

    @Column(name = "스포츠")
    private int sports;

    @Column(name = "솔로")
    private int solo;
}
