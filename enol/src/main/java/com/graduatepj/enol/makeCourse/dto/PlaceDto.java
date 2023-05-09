package com.graduatepj.enol.makeCourse.dto;

import com.graduatepj.enol.makeCourse.vo.Place;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDto {
    private int id;
    private String name;
    private String categoryCode;
    private String addressName;
    private double x;
    private double y;
    private String cost;
    private String imgUrl;
    private String phoneNumber;

    public static PlaceDto fromEntity(Place place) {
        if (place == null) {
            return null;
        }

        return PlaceDto.builder()
                .id(place.getId())
                .name(place.getName())
                .categoryCode(place.getCategoryCode())
                .addressName(place.getAddressName())
                .x(place.getX())
                .y(place.getY())
                .cost(place.getCost())
                .imgUrl(place.getImgUrl())
                .phoneNumber(place.getPhoneNumber())
                .build();
    }
}