package com.graduatepj.enol.makeCourse.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryPriority {
    SPORTS_MAIN("놀기 Main 스포츠", 1),
    NATURE_MAIN("놀기 Main 자연", 2),
    ATTRACTION_MAIN("놀기 Main 어트랙션", 3),
    SPORTS_SUB("놀기 Sub 스포츠", 4),
    ALONE_POSSIBLE_SUB("놀기 Sub 혼자도 가능", 5),
    MULTI_MEMBER_SUB("놀기 Sub 여러명", 6),
    COOKING_MAIN("체험 Main 요리&베이킹", 7),
    PLANTS_MAIN("체험 Main 식물", 8),
    BEAUTY_MAIN("체험 Main 뷰티", 9),
    MARTIAL_ARTS_MAIN("체험 Main 다도", 10),
    ART_MAIN("체험 Main 미술", 11),
    HANDMADE_MAIN("체험 Main 핸드메이드", 12),
    SIGHTSEEING_MAIN("관계 Main 관람", 13),
    LANDMARK_MAIN("관계 Main 관광명소", 14),
    THEME_STREET_MAIN("관계 Main 테마거리", 15),
    SCENERY_MAIN("관계 Main 풍경", 16),
    REST_MAIN("관계 Main 휴식", 17),
    SHOPPING_MAIN("관계 Main 쇼핑", 18),
    THEME_CAFE_SUB("관계 Sub 테마카페", 19),
    DRINK_SUB("관계 Sub 음주", 20),
    EXHIBITION_MAIN("관람 Main 전시시설", 21),
    PERFORMANCE_MAIN("관람 Main 공연시설", 22);

    private String name;
    private int value;
}
