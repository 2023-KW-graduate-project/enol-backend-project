package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "place")
public class Place {
    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

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


//    /** 어느 카테고리에 소속되어있는지 카테고리 id */
//    private String category;
//
//    /** 카카오에서 기본적으로 제공하는 것들 */
//    private String placeName;
//    private String categoryGroupCode;
//    private String categoryGroupName;
//    private String categoryName;
//    private String phone;
//    private String addressName;
//    private String roadAddressName;
//    private String placeUrl;
//    private String distance;
//    private Double x;
//    private Double y;
//    /** 별점 */
//    private double rate;
//    /** 개점/폐점 시간 */
//    private int startTime;
//    private int finishTime;
//    /** 기타 특징? */
//    private String attribute;

}