package com.graduatepj.enol.makeCourse.dto;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SmallCategory {
    // D열의 카테고리들을 가지는 클래스
    // BigCategory에 속하는 세부 카테고리가 뭔지를 배열로 저장
    // 싱글턴 적용
    private static SmallCategory instance = new SmallCategory();

    public static SmallCategory getInstance() {
        return instance;
    }

    private Map<String, List<String>> categories;

    private SmallCategory() {
        categories = new HashMap<>();
        categories.put("놀기 Main 스포츠", Arrays.asList("수상스포츠", "클라이밍", "수영장", "스킨스쿠버", "스케이트장"));
        categories.put("놀기 Main 자연", Arrays.asList("산", "계곡"));
        categories.put("놀기 Main 어트랙션", Arrays.asList("서바이벌게임", "테마파크(중대형)", "눈썰매장", "테마파크(소형)"));
        categories.put("놀기 Sub 스포츠", Arrays.asList("탁구", "볼링", "사격,궁도", "스크린야구", "스크린골프"));
        categories.put("놀기 Sub 혼자도 가능", Arrays.asList("오락실", "실내낚시", "만화카페"));
        categories.put("놀기 Sub 여러명", Arrays.asList("VR게임", "방탈출카페", "보드게임카페", "마작카페"));
        categories.put("체험 Main 요리&베이킹", Arrays.asList("요리&배이킹"));
        categories.put("체험 Main 식물", Arrays.asList("플라워*가드닝", "테라리움"));
        categories.put("체험 Main 뷰티", Arrays.asList("뷰티"));
        categories.put("체험 Main 다도", Arrays.asList("다도"));
        categories.put("체험 Main 미술", Arrays.asList("드로잉카페"));
        categories.put("체험 Main 핸드메이드", Arrays.asList("핸드메이드"));
        categories.put("관계 Main 관람", Arrays.asList("야외동물원", "실내동물원", "수목원,식물원"));
        categories.put("관계 Main 관광명소", Arrays.asList("고궁,궁", "전망대", "관광,명소"));
        categories.put("관계 Main 테마거리", Arrays.asList("테마거리", "카페거리"));
        categories.put("관계 Main 풍경", Arrays.asList("공원", "숲", "호수"));
        categories.put("관계 Main 휴식", Arrays.asList("룸카페&멀티방", "파티룸", "스파"));
        categories.put("관계 Main 쇼핑", Arrays.asList("백화점"));
        categories.put("관계 Sub 테마카페", Arrays.asList("플라워카페", "갤러리카페", "슬라임카페", "고양이카페", "상담카페", "카페", "이색카페", "북카페"));
        categories.put("관계 Sub 음주", Arrays.asList("실내포장마차", "와인바", "일본식주점", "칵테일바", "호프,요리주점"));
        categories.put("관람 Main 전시시설", Arrays.asList("아쿠아리움", "미술관", "전시관"));
        categories.put("관람 Main 공연시설", Arrays.asList("공연장,연극극장", "영화관"));
    }

}
