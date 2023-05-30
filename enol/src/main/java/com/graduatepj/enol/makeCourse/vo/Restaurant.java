package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "restaurant")
public class Restaurant {
    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private int id;

    @Column(name="category_name")
    private String categoryName;
    @Column(name="place_name")
    private String placeName;
    @Column(name="avg_rating")
    private double rating;
    @Column(name="rating_num")
    private int ratingNum;
    @Column(name="review_num")
    private int reviewNum;
    @Column(name="address_name")
    private String addressName;
    @Column(name="x")
    private double x;
    @Column(name="y")
    private double y;
    @Column(name="phone")
    private String phoneNumber;
    @Column(name="introduction")
    private String intro;
    @Column(name="open_operation")
    private String time;
    @Column(name="menu")
    private String menu;
    @Column(name="img_url")
    private String imgUrl;
    @Column(name="tag")
    private String tag;
}
