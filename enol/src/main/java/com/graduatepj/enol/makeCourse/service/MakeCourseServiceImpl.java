package com.graduatepj.enol.makeCourse.service;

import com.graduatepj.enol.makeCourse.dao.CourseRepository;
import com.graduatepj.enol.makeCourse.dao.MemberRepository;
import com.graduatepj.enol.makeCourse.dao.StoreRepository;
import com.graduatepj.enol.makeCourse.dto.*;
import com.graduatepj.enol.makeCourse.vo.BigCourseOutline;
import com.graduatepj.enol.makeCourse.vo.Course;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MakeCourseServiceImpl implements MakeCourseService {

    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    // 실제로 해야할 것
    @Override
    public List<CourseResponse> MakeCourse(CourseRequest courseRequest) {
        FirstCourse firstCourse = firstCourseFiltering(courseRequest);
        SecondCourse secondCourse = secondCourseFiltering(firstCourse);
        FinalCourse finalCourse = finalCourseFiltering(secondCourse);
        return null;
    }


    // 코스를 고르는거잖아 -> 필터링 + 코스 유사도 평가까지 끝내야겠네
    @Override
    public FirstCourse firstCourseFiltering(CourseRequest courseRequest) {
        // C열 카테고리로 이루어진 코스 생성
        log.info("firstCourseFiltering Start!");


        // 입력받은 정보들을 모두 firstCourse에 넣는 과정
//        FirstCourse course;

        // firstCourse에 내용 확인
//        log.info("firstCourse.getMemberList = {}", course.getMemberList());
//        log.info("firstCourse.getStartTime = {}", course.getStartTime());
//        log.info("firstCourse.getFinishTime = {}", course.getFinishTime());
//        log.info("firstCourse.getCourseKeyword = {}", course.getCourseKeyword());
//        log.info("firstCourse.getGoal = {}", course.getGoal());
//        log.info("firstCourse.getWantedCategory = {}", course.getWantedCategory());
//        log.info("firstCourse.isMealCheck = {}", course.isMealCheck());

        // 시간 제한으로 가능한 모든 코스 받아오기(시간은 24시간으로 받아야함)
        // 시간에 따라 코스에 속할 카테고리 개수 정하기 - 최대 main 4개나 sub 4개
        int time = courseRequest.getFinishTime() - courseRequest.getStartTime();
        List<Course> timeCourse = courseRepository.findAllByTime(time);
        List<Course> filteredCourse;
        for (Course courseInfo : timeCourse) { // 코스틀 전체에서 코스 하나하나 가져오기
            List<String> course = courseInfo.getCategories(); // 코스 가져오기
            if (courseRequest.getWantedCategory() != null) {
                if (!course.contains(courseRequest.getWantedCategory())) { // 필수장소(카테고리) 필터링
                    continue;
                }
            }
            if (!firstCourseGoalFiltering()) // 대충 여기다 쓰려고 만든것같은데?
                continue;
            if (!firstCourseKeywordFiltering()) // 대충 여기다 쓰려고 만든것같은데?
                continue;
            filteredCourse.add(courseInfo);
        }

        // 랜덤으로 하든 유사도평가를 하든 어쩄든 여기 지나면 코스는 하나만 남거든.


        // wantedCategory가 null인 경우 우선순위에 따른 주장소 선택(main)


        return null;
    }

    // 세부 카테고리를 결정
    @Override
    public SecondCourse secondCourseFiltering(FirstCourse firstCourse) {
        return null;
    }

    // 실제 가게들 선택 및 최적화 알고리즘 적용
    @Override
    public FinalCourse finalCourseFiltering(SecondCourse secondCourse) {
        return null;
    }


    /**
     * 코스 목적에 따른 필터링 메서드 - FirstCourse에서 사용
     *
     * @param firstCourse
     * @param bigCourse
     * @return
     */
    private List<BigCourseOutline> firstCourseGoalFiltering(FirstCourse firstCourse, List<BigCourseOutline> bigCourse) {
        List<Integer> removeIdx = new ArrayList<>(); // 여기에 제거할거 인덱스만 모아두기
        for (int i = 0; i < 5; i++) { // 신첵, 음주, 체험, 동물, 힐링
            if (firstCourse.getGoal(i)) { // 목적 하나씩 가져와서 있는 경우만 필터링 진행
                switch (i) {
                    case 0: // 산책
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
                            if (!bigCourse.get(j).getFeature().equals("산책")) { // 산책을 가지지 않는다면
                                removeIdx.add(j);
                            }
                        }

                        while (removeIdx.size() == 0) {
                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
                        }
                        break;

                    case 1: // 음주
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
                            if (!bigCourse.get(j).getFeature().equals("음주")) { // 음주를 가지지 않는다면
                                removeIdx.add(j);
                            }
                        }

                        while (removeIdx.size() == 0) {
                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
                        }
                        break;

                    case 2: // 체험
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
                            if (!bigCourse.get(j).getFeature().equals("체험")) { // 체험을 가지지 않는다면
                                removeIdx.add(j);
                            }
                        }

                        while (removeIdx.size() == 0) {
                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
                        }
                        break;

                    case 3: // 동물
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
                            if (!bigCourse.get(j).getFeature().equals("동물")) { // 동물을 가지지 않는다면
                                removeIdx.add(j);
                            }
                        }

                        while (removeIdx.size() == 0) {
                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
                        }
                        break;

                    case 4: // 힐링
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
                            if (!bigCourse.get(j).getFeature().equals("힐링")) { // 힐링을 가지지 않는다면
                                removeIdx.add(j);
                            }
                        }

                        while (removeIdx.size() == 0) {
                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
                        }
                        break;
                }
            }
        }
        return bigCourse;
    }

    /**
     * 코스 키워드 필터링
     *
     * @param firstCourse
     * @param bigCourse
     * @param N
     * @return
     */
    private Boolean firstCourseKeywordFiltering(Course course, List<BigCourseOutline> bigCourse, int N) {
        // N보다 높거나 낮은것을 따지므로
        List<Integer> keywordRemoveIdx = new ArrayList<>(); // 여기에 제거할거 인덱스만 모아두기
        for (int i = 0; i < 8; i++) { // 신첵, 음주, 체험, 동물, 힐링
            if (firstCourse.getCourseKeyword(i)) { // 목적 하나씩 가져와서 있는 경우만 필터링 진행
                switch (i) {
                    case 0: // 이색
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
                            if (!(bigCourse.get(j).getSpecification() > N)) { // N보다 특이도가 높다면 코스 키워드는 이색
                                keywordRemoveIdx.add(j); //키워드가 이색인 경우인데 코스의 특이도가 N보다 크지 않으면 제거
                            }
                        }

                        while (keywordRemoveIdx.size() == 0) {
                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
                        }
                        break;

                    case 1: // 일상
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
                            if (!(bigCourse.get(j).getSpecification() <= N)) { // N보다 특이도가 낮거나 같다면 코스 키워드는 일상
                                keywordRemoveIdx.add(j); //키워드가 일상인 경우인데 코스의 특이도가 N보다 낮거나 같지 않으면 제거
                            }
                        }

                        while (keywordRemoveIdx.size() == 0) {
                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
                        }
                        break;

                    case 2: // 체력높음
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
                            if (!(bigCourse.get(j).getFatigability() > N)) { // N보다 피로도가 높다면 코스 키워드는 체력높음
                                keywordRemoveIdx.add(j); //키워드가 체력높음인 경우인데 코스의 특이도가 N보다 크지 않으면 제거
                            }
                        }

                        while (keywordRemoveIdx.size() == 0) {
                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
                        }
                        break;

                    case 3: // 체력낮음
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
                            if (!(bigCourse.get(j).getFatigability() <= N)) { // N보다 피로도가 낮거나 같다면 코스 키워드는 체력낮음
                                keywordRemoveIdx.add(j); //키워드가 체력낮음인 경우인데 코스의 특이도가 N보다 낮거나 같지 않으면 제거
                            }
                        }

                        while (keywordRemoveIdx.size() == 0) {
                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
                        }
                        break;

                    case 4: // 액티비티
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
                            if (!(bigCourse.get(j).getActivity() > N)) { // N보다 활동성이 높다면 코스 키워드는 액티비티
                                keywordRemoveIdx.add(j); //키워드가 액티비티인 경우인데 코스의 활동성이 N보다 높지 않으면 제거
                            }
                        }

                        while (keywordRemoveIdx.size() == 0) {
                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
                        }
                        break;

                    case 5: // 앉아서 놀기
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
                            if (!(bigCourse.get(j).getActivity() <= N)) { // N보다 활동성이 높다면 코스 키워드는 앉아서놀기
                                keywordRemoveIdx.add(j); //키워드가 앉아서놀기인 경우인데 코스의 활동성이 N보다 낮거나 같지 않으면 제거
                            }
                        }

                        while (keywordRemoveIdx.size() == 0) {
                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
                        }
                        break;
                }
            }
        }
        return bigCourse;
    }
}
