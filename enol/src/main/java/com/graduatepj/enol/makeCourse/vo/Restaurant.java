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

    @Column(name="user_category_name")
    private String categoryName;
    @Column(name="place_name")
    private String placeName;
    @Column(name="category_name")
    private String categoryCode;
    @Column(name="avg_rating")
    private double rating;
    @Column(name="address_name")
    private String addressName;
    @Column(name="x")
    private double x;
    @Column(name="y")
    private double y;
}
