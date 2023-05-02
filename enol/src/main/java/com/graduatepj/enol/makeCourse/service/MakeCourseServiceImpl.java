package com.graduatepj.enol.makeCourse.service;

import com.graduatepj.enol.makeCourse.dao.CourseRepository;
import com.graduatepj.enol.makeCourse.dao.MemberRepository;
import com.graduatepj.enol.makeCourse.dao.StoreRepository;
import com.graduatepj.enol.makeCourse.dto.*;
import com.graduatepj.enol.makeCourse.type.CategoryPriority;
import com.graduatepj.enol.makeCourse.vo.BigCourseOutline;
import com.graduatepj.enol.makeCourse.vo.Course;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MakeCourseServiceImpl implements MakeCourseService {

    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private static final int CATEGORY_NUM = 62;

    // 실제로 해야할 것
    @Override
    public List<CourseResponse> MakeCourse(CourseRequest courseRequest) {
        CourseDto firstCourse = firstCourseFiltering(courseRequest);
        SecondCourse secondCourse = secondCourseFiltering(firstCourse);
        FinalCourse finalCourse = finalCourseFiltering(secondCourse);
        return null;
    }


    // 코스를 고르는거잖아 -> 필터링 + 코스 유사도 평가까지 끝내야겠네
    @Override
    public CourseDto firstCourseFiltering(CourseRequest courseRequest) {
        // C열 카테고리로 이루어진 코스 생성
        log.info("firstCourseFiltering Start!");

        // 멤버 데이터 가져오기
        List<MemberDto> memberList = memberRepository
                .findAllByIdIn(courseRequest.getMemberIdList())
                .stream()
                .map(member -> (MemberDto.fromEntity(member)))
                .collect(Collectors.toList());

        // 멤버들의 특성치 평균 구하기
        MemberDto avgMember = new MemberDto();
        if (!memberList.isEmpty()) {
            avgMember.setFatigability(memberList.stream().mapToInt(MemberDto::getFatigability).sum() / memberList.size());
            avgMember.setSpecification(memberList.stream().mapToInt(MemberDto::getSpecification).sum() / memberList.size());
            avgMember.setActivity(memberList.stream().mapToInt(MemberDto::getActivity).sum() / memberList.size());
        }

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
        List<CourseDto> filteredCourse = new ArrayList<>();
        for (Course courseInfo : timeCourse) { // 코스틀 전체에서 코스 하나하나 가져오기
            List<String> course = courseInfo.getCategories(); // 코스 가져오기
            if (courseRequest.getWantedCategory() != null) {
                if (!course.contains(courseRequest.getWantedCategory())) { // 필수장소(카테고리) 필터링
                    continue;
                }
            }
//            if (!firstCourseGoalFiltering()) // 대충 여기다 쓰려고 만든것같은데?
//                continue;
//            if (!firstCourseKeywordFiltering()) // 대충 여기다 쓰려고 만든것같은데?
//                continue;
            filteredCourse.add(CourseDto.fromEntity(courseInfo));
        }

        // 코사인 유사도 평가로 뽑는 방법(1)
        log.info("computeSimilarity Start!");
        List<CourseDto> courseAfterSimilarity = computeSimilarity(filteredCourse, avgMember);
        // 랜덤으로 뽑는 방법(2)
        // 랜덤 객체 생성
        //Random rand = new Random();
        // 리스트에서 무작위로 하나의 요소 선택
        // CourseDto randomCourse = filteredCourse.get(rand.nextInt(filteredCourse.size()));
        CourseDto selectedCourse = courseAfterSimilarity.get(0);
        selectedCourse.setMealCheck(courseRequest.isMealCheck());
        selectedCourse.setWantedCategory(courseRequest.getWantedCategory());

        // wantedCategory가 null인 경우 우선순위에 따른 주장소 선택(main)
        log.info("firstCourseFiltering Start!");
        if (selectedCourse.getWantedCategory() == null) {
            // 우선순위에 따른 주장소 선택
            for (CategoryPriority p : CategoryPriority.values()) {
                for (String category : selectedCourse.getCategories()) {
                    if (category.contains(p.getName())) {
                        selectedCourse.setWantedCategory(p.getName());
                        break;
                    }
                }
                if (selectedCourse.getWantedCategory() != null) {
                    break;
                }
            }
        }
        return selectedCourse;
    }


    // 세부 카테고리를 결정
    // 일단 죄다 랜덤으로 뽑긴 했는데 세부적으로 뭐 기타특징 같은거 고려해야함.
    @Override
    public SecondCourse secondCourseFiltering(CourseDto firstCourse) {
        SecondCourse secondCourse = new SecondCourse(firstCourse);
//         랜덤 객체 생성
        Random rand = new Random();
        List<String> detailCategories = new ArrayList<>();
//         리스트에서 무작위로 하나의 요소 선택
        for (String s : firstCourse.getCategories()) {
            List<String> smallCategories = SmallCategory.getInstance().getCategories().get(s);
            detailCategories.add(smallCategories.get(rand.nextInt(smallCategories.size())));
        }
        secondCourse.setDetailCategories(detailCategories);
        return secondCourse;
    }

    // 실제 가게들 선택 및 최적화 알고리즘 적용
    @Override
    public FinalCourse finalCourseFiltering(SecondCourse secondCourse) {
        return null;
    }


    /**
     * 코스 목적에 따른 필터링 메서드 - FirstCourse에서 사용
     *
     * @param course
     * @param bigCourse
     * @return
     */
    private Boolean firstCourseGoalFiltering(Course course, List<Course> bigCourse) {
//        List<Integer> removeIdx = new ArrayList<>(); // 여기에 제거할거 인덱스만 모아두기
//        for (int i = 0; i < 5; i++) { // 신첵, 음주, 체험, 동물, 힐링
//            if (course.getGoals(i)) { // 목적 하나씩 가져와서 있는 경우만 필터링 진행
//                switch (i) {
//                    case 0: // 산책
//                        for (int j = 0; j < bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
//                            if (!bigCourse.get(j).getFeature().equals("산책")) { // 산책을 가지지 않는다면
//                                removeIdx.add(j);
//                            }
//                        }
//
//                        while (removeIdx.size() == 0) {
//                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
//                        }
//                        break;
//
//                    case 1: // 음주
//                        for (int j = 0; j < bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
//                            if (!bigCourse.get(j).getFeature().equals("음주")) { // 음주를 가지지 않는다면
//                                removeIdx.add(j);
//                            }
//                        }
//
//                        while (removeIdx.size() == 0) {
//                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
//                        }
//                        break;
//
//                    case 2: // 체험
//                        for (int j = 0; j < bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
//                            if (!bigCourse.get(j).getFeature().equals("체험")) { // 체험을 가지지 않는다면
//                                removeIdx.add(j);
//                            }
//                        }
//
//                        while (removeIdx.size() == 0) {
//                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
//                        }
//                        break;
//
//                    case 3: // 동물
//                        for (int j = 0; j < bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
//                            if (!bigCourse.get(j).getFeature().equals("동물")) { // 동물을 가지지 않는다면
//                                removeIdx.add(j);
//                            }
//                        }
//
//                        while (removeIdx.size() == 0) {
//                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
//                        }
//                        break;
//
//                    case 4: // 힐링
//                        for (int j = 0; j < bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
//                            if (!bigCourse.get(j).getFeature().equals("힐링")) { // 힐링을 가지지 않는다면
//                                removeIdx.add(j);
//                            }
//                        }
//
//                        while (removeIdx.size() == 0) {
//                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
//                        }
//                        break;
//                }
//            }
//        }
        return true;
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
//        List<Integer> keywordRemoveIdx = new ArrayList<>(); // 여기에 제거할거 인덱스만 모아두기
//        for (int i = 0; i < 8; i++) { // 신첵, 음주, 체험, 동물, 힐링
//            if (firstCourse.getCourseKeyword(i)) { // 목적 하나씩 가져와서 있는 경우만 필터링 진행
//                switch (i) {
//                    case 0: // 이색
//                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
//                            if (!(bigCourse.get(j).getSpecification() > N)) { // N보다 특이도가 높다면 코스 키워드는 이색
//                                keywordRemoveIdx.add(j); //키워드가 이색인 경우인데 코스의 특이도가 N보다 크지 않으면 제거
//                            }
//                        }
//
//                        while (keywordRemoveIdx.size() == 0) {
//                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
//                        }
//                        break;
//
//                    case 1: // 일상
//                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
//                            if (!(bigCourse.get(j).getSpecification() <= N)) { // N보다 특이도가 낮거나 같다면 코스 키워드는 일상
//                                keywordRemoveIdx.add(j); //키워드가 일상인 경우인데 코스의 특이도가 N보다 낮거나 같지 않으면 제거
//                            }
//                        }
//
//                        while (keywordRemoveIdx.size() == 0) {
//                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
//                        }
//                        break;
//
//                    case 2: // 체력높음
//                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
//                            if (!(bigCourse.get(j).getFatigability() > N)) { // N보다 피로도가 높다면 코스 키워드는 체력높음
//                                keywordRemoveIdx.add(j); //키워드가 체력높음인 경우인데 코스의 특이도가 N보다 크지 않으면 제거
//                            }
//                        }
//
//                        while (keywordRemoveIdx.size() == 0) {
//                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
//                        }
//                        break;
//
//                    case 3: // 체력낮음
//                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
//                            if (!(bigCourse.get(j).getFatigability() <= N)) { // N보다 피로도가 낮거나 같다면 코스 키워드는 체력낮음
//                                keywordRemoveIdx.add(j); //키워드가 체력낮음인 경우인데 코스의 특이도가 N보다 낮거나 같지 않으면 제거
//                            }
//                        }
//
//                        while (keywordRemoveIdx.size() == 0) {
//                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
//                        }
//                        break;
//
//                    case 4: // 액티비티
//                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
//                            if (!(bigCourse.get(j).getActivity() > N)) { // N보다 활동성이 높다면 코스 키워드는 액티비티
//                                keywordRemoveIdx.add(j); //키워드가 액티비티인 경우인데 코스의 활동성이 N보다 높지 않으면 제거
//                            }
//                        }
//
//                        while (keywordRemoveIdx.size() == 0) {
//                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
//                        }
//                        break;
//
//                    case 5: // 앉아서 놀기
//                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
//                            if (!(bigCourse.get(j).getActivity() <= N)) { // N보다 활동성이 높다면 코스 키워드는 앉아서놀기
//                                keywordRemoveIdx.add(j); //키워드가 앉아서놀기인 경우인데 코스의 활동성이 N보다 낮거나 같지 않으면 제거
//                            }
//                        }
//
//                        while (keywordRemoveIdx.size() == 0) {
//                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
//                        }
//                        break;
//                }
//            }
//        }
        return true;
    }

    public List<CourseDto> computeSimilarity(List<CourseDto> filteredCourse, MemberDto avgMember) {
        List<CourseDto> similarCourses = new ArrayList<>();

        // 평균 값을 이용해 RealVector 생성
        RealVector avgVector = new ArrayRealVector(new double[]{
                avgMember.getFatigability(),
                avgMember.getSpecification(),
                avgMember.getActivity()
        });

        // 코사인 유사도 계산
        for (CourseDto course : filteredCourse) {
            RealVector vector = new ArrayRealVector(new double[]{
                    course.getFatigability(),
                    course.getSpecification(),
                    course.getActivity()
            });

            double cosineSimilarity = (vector.dotProduct(avgVector)) / (vector.getNorm() * avgVector.getNorm());

            // 가중치를 곱해 코사인 유사도에 반영(뭘로 반영해야할까)
            double weight = course.getRate() == 0.0 ? 1.0 : (course.getRate() / 5.0) * 2.0;
            // 그냥 5.0으로 나눈 방법 -> 0.0(안받은거)일때보다 평점이 1.0일때 가중치가 더 부과됨.
            // double weight = course.getRate()/5.0
            cosineSimilarity *= weight;

            course.setSimilarity(cosineSimilarity);
            similarCourses.add(course);
        }

        // 코사인 유사도에 따라 정렬(내림차순)
        similarCourses.sort(Comparator.comparing(CourseDto::getSimilarity).reversed());

        return similarCourses;
    }
}
