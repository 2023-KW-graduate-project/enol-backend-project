package com.graduatepj.enol.makeCourse.dto;

import com.graduatepj.enol.makeCourse.vo.BigCourseOutline;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FirstCourse {
    // 맞춤 코스 제작 코스 - 입력: 인원수, 약속시간(시작, 끝), 코스 키워드(1개 이상), 모임의 목적, 가고 싶은 카테고리, 식사 유무 - 이건 모든 Course 클래스가 가지도록 함

    // int peopleNum, int startTime, int finishTime, String courseKeyword, String goal, String wantedCategory, boolean mealCheck
    // 밥시간 - 8~10시 사이 아침, 11~1시 사이 점심, 5~7시 사이 저녁 ***

//    private int peopleNumber; // 인원수

    private List<SelectedMemberDto> memberList = new ArrayList<>(); // 회원들 리스트

    private int startTime; // 약속 시작 시간

    private int finishTime; // 약속 끝 시간

    private boolean[] courseKeyword = new boolean[6]; // 코스 키워드, 순서대로 이색, 일상/체력높음,체력낮음/액티비티, 앉아서놀기/

    private boolean[] goal = new boolean[5]; // 모임의 목적, 순서대로 신첵, 음주, 체험, 동물, 힐링

    private String wantedCategory; // 가고 싶은 카테고리 입력한 것 - 없음이면 nothing

    private boolean mealCheck; // 밥 먹을거면 true, 아니면 false

    private String mainBigCategory; // 코스의 주 장소가 될 카테고리

    private List<BigCourseOutline> bigCategoryCourses = new ArrayList<>(); // bigCategory로 이루어진 코스들을 동적 배열로 가짐

    public FirstCourse(BigCourseOutline bigCourseOutline) { bigCategoryCourses.add(bigCourseOutline);}

    public boolean getGoal(int idx) {
        return goal[idx];
    }

    public boolean getCourseKeyword(int idx) {
        return courseKeyword[idx];
    }

    public BigCourseOutline getBigCategoryCourses(int idx) { // 코스 틀 중 해당 인덱스의 것 하나만 추출
        return bigCategoryCourses.get(idx);
    }
}
