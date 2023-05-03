package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity(name = "STORE")
public class Store {
    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** 어느 카테고리에 소속되어있는지 카테고리 id */
    private String category;

    /** 카카오에서 기본적으로 제공하는 것들 */
    private String placeName;
    private String categoryGroupCode;
    private String categoryGroupName;
    private String categoryName;
    private String phone;
    private String addressName;
    private String roadAddressName;
    private String placeUrl;
    private String distance;
    private Double x;
    private Double y;
    /** 별점 */
    private double rate;
    /** 개점/폐점 시간 */
    private int startTime;
    private int finishTime;
    /** 기타 특징? */
    private String attribute;

}