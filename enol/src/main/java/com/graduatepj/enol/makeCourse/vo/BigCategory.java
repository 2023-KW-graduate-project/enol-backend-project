package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BigCategory {
    // C열의 카테고리들을 가지는 클래스
    // 우선순위 순으로 배열에 저장
    // Main, sub를 따로 저장

    // main과 sub에 해당하는 카테고리들 이름
    private String[] mainCategory = {"play", "experience", "relation", "watch"};

    private String[] subCategory = {"play", "relation"};

    // main에 속하는 카테고리
    private String[] playMain = {"놀기-스포츠", "놀기-자연", "놀기-어트랙션"};

    private String[] experienceMain = {"체험-식물", "체험-뷰티", "체험-다도", "체험-미술", "체험-핸드메이드"};

    private String[] relationMain = {"관계-관람", "관계-관광명소", "관계-테마거리", "관계-풍경", "관계-휴식", "관계-쇼핑"};

    private String[] watchMain = {"관람-전시시설", "관람-공연시설"};

    // sub에 속하는 카테고리
    private String[] playSub = {"놀기-스포츠", "놀기-혼자도가능", "놀기-여러명"};

    private String[] relationSub = {"관계-테마카페", "관계-음주"};
}
