package com.graduatepj.enol.makeCourse.controller;

import com.graduatepj.enol.makeCourse.dto.CourseRequest;
import com.graduatepj.enol.makeCourse.dto.CourseResponse;
import com.graduatepj.enol.makeCourse.dto.PlaceDto;
import com.graduatepj.enol.makeCourse.service.MakeCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/makeCourse")
public class MakeCourseController {
    // 맞춤 코스 제작 페이지
    // 맞춤 코스 제작 코스 - 입력: 인원수, 약속시간(시작, 끝), 코스 키워드(1개 이상), 모임의 목적
    // 가고싶은 카테고리 입력(선택) - 디폴트가 없음, D열의 카테고리를 받으면 코스의 주 장소로 설정, 입력 안받으면 정해진 C열의 우선순위에 따라 코스에 속한 C열 분류 중 하나를 주 장소로 설정
    // 최종 출력 - 장소들로 이루어진 코스 한 개
    private final MakeCourseService makeCourseService;

    public MakeCourseController(MakeCourseService makeCourseService) {
        this.makeCourseService = makeCourseService;
    }

    @PostMapping
    public ResponseEntity<CourseResponse> createCustomCourse(@RequestBody CourseRequest requestData) {
        return ResponseEntity.ok(makeCourseService.MakeCourse(requestData));
    }

    // 프론트 테스트용
    @PostMapping("/test")
    public ResponseEntity<List<PlaceDto>> createTestCourse() {
        return ResponseEntity.ok(makeCourseService.testCourse());
    }

}
