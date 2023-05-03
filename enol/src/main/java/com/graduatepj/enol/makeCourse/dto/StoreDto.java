package com.graduatepj.enol.makeCourse.dto;

import com.graduatepj.enol.makeCourse.vo.Store;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDto {
    /**
     * id
     */
    private Long id;
    /**
     * 어느 카테고리에 소속되어있는지 카테고리 id
     */
    private String category;

    /**
     * 카카오에서 기본적으로 제공하는 것들
     */
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
    /**
     * 별점
     */
    private double rate;
    /**
     * 개점/폐점 시간
     */
    private int startTime;
    private int finishTime;
    /**
     * 기타 특징?
     */
    private String attribute;

    public static StoreDto fromEntity(Store store) {
        return StoreDto.builder()
                .id(store.getId())
                .category(store.getCategory())
                .placeName(store.getPlaceName())
                .categoryGroupCode(store.getCategoryGroupCode())
                .categoryGroupName(store.getCategoryGroupName())
                .categoryName(store.getCategoryName())
                .phone(store.getPhone())
                .addressName(store.getAddressName())
                .roadAddressName(store.getRoadAddressName())
                .placeUrl(store.getPlaceUrl())
                .distance(store.getDistance())
                .x(store.getX())
                .y(store.getY())
                .startTime(store.getStartTime())
                .finishTime(store.getFinishTime())
                .attribute(store.getAttribute())
                .build();
    }
}