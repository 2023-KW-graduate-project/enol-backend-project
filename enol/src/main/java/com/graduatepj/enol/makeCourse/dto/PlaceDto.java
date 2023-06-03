package com.graduatepj.enol.makeCourse.dto;

import com.graduatepj.enol.makeCourse.vo.Place;
import lombok.*;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlaceDto {
    private Long id;
    private String categoryName;
    private String placeName;
    private double rating;
    private int ratingNum;
    private int reviewNum;
    private String addressName;
    private double x;
    private double y;
    private String phoneNumber;
    private String intro;
    private String time;
    private String menu;
    private String imgUrl;
    private String tag;

    public static PlaceDto fromEntity(Place place) {
        if (place == null) {
            return null;
        }

        return PlaceDto.builder()
                .id(place.getId())
                .placeName(place.getPlaceName())
                .categoryName(place.getCategoryName())
                .addressName(place.getAddressName())
                .rating(place.getRating())
                .ratingNum(place.getRatingNum())
                .reviewNum(place.getReviewNum())
                .x(place.getX())
                .y(place.getY())
                .imgUrl(place.getImgUrl())
                .phoneNumber(place.getPhoneNumber())
                .intro(place.getIntro())
                .time(place.getTime())
                .menu(place.getMenu())
                .tag(place.getTag())
                .build();
    }

    public static List<PlaceDto> fromEntityList(List<Place> places) {
        List<PlaceDto> placeDtos=new ArrayList<>();

        if (places == null) {
            return null;
        }

        for(Place place : places){
            placeDtos.add(PlaceDto.fromEntity(place));
        }

        return placeDtos;
    }
}