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
    private int id;

    @Column(name="name")
    private String name;
    @Column(name="category_code")
    private String categoryCode;
    @Column(name="address_name")
    private String addressName;
    @Column(name="x")
    private double x;
    @Column(name="y")
    private double y;
    @Column(name="cost")
    private String cost;
    @Column(name="img_url")
    private String imgUrl;
    @Column(name="phone")
    private String phoneNumber;

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