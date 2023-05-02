package com.graduatepj.enol.makeCourse.service;

import com.graduatepj.enol.makeCourse.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("makeCourseService")
public interface MakeCourseService {
    // 맞춤 코스 제작 코스 - 입력: 인원수, 약속시간(시작, 끝), 코스 키워드(1개 이상), 모임의 목적, 가고 싶은 카테고리, 식사 유무 - 이건 모든 Course 클래스가 가지도록 함
    public abstract List<CourseResponse> MakeCourse(CourseRequest courseRequest);
    // 첫 번째 코스 생성 - C열로 이루어진 코스 생성
    public abstract FirstCourse firstCourseFiltering(CourseRequest courseRequest);

    // 두 번째 코스 생성 - D열로 이루어진 코스 생성
    public abstract SecondCourse secondCourseFiltering(FirstCourse firstCourse);

    // 세 번째 코스 생성 - 가게들로 이루어진 최종 코스 생성
    public abstract FinalCourse finalCourseFiltering(SecondCourse secondCourse);
}
