package com.graduatepj.enol.makeCourse.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduatepj.enol.makeCourse.dao.*;
import com.graduatepj.enol.makeCourse.dto.*;
import com.graduatepj.enol.makeCourse.type.CategoryPriority;
import com.graduatepj.enol.makeCourse.vo.Category;
import com.graduatepj.enol.makeCourse.vo.Course;
import com.graduatepj.enol.makeCourse.vo.CourseV2;
import com.graduatepj.enol.makeCourse.vo.DColCategory;
import com.graduatepj.enol.makeCourse.dto.PlaceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MakeCourseServiceImpl implements MakeCourseService {

    @Value("${kakao.rest.api.key}")
    private String apikey;

    private final CourseRepository courseRepository;
    private final CourseMemberRepository courseMemberRepository;
    private final PlaceRepository placeRepository;
    private static final int CATEGORY_NUM = 62;
    private static final int FILTER_RADIUS = 200;

    private final CategoryRepository categoryRepository;
    private final Course1Repository course1Repository;
    private final Course2Repository course2Repository;
    private final Course3Repository course3Repository;
    private final Course4Repository course4Repository;
    private final CourseV2Repository courseV2Repository;
    private final CoursePurposeRepository coursePurposeRepository;

    // 실제로 해야할 것
    @Override
    public List<PlaceDto> MakeCourse(CourseRequest courseRequest) {
        CourseDto firstCourse = firstCourseFiltering(courseRequest);
        SecondCourse secondCourse = secondCourseFiltering(firstCourse);
        List<PlaceDto> finalCourse = finalCourseFiltering(secondCourse);
        return finalCourse;
    }


    // 코스를 고르는거잖아 -> 필터링 + 코스 유사도 평가까지 끝내야겠네
//    @Override
//    public CourseDto firstCourseFiltering(CourseRequest courseRequest) { // 기존 firstCourseFiltering 메서드 - DB 수정하면서 아래처럼 변경
//        // C열 카테고리로 이루어진 코스 생성
//        log.info("firstCourseFiltering Start!");
//
//        // 인자값이 잘 들어왔는지 확인하는 로그
//        log.info("courseRequest.getNumPeople = {}", courseRequest.getNumPeople());
//        log.info("courseRequest.getMemberIdList = {}", courseRequest.getMemberIdList());
//        log.info("courseRequest.isMealCheck = {}", courseRequest.isMealCheck());
//        log.info("courseRequest.getStartTime = {}, FinishTime = {}", courseRequest.getStartTime(), courseRequest.getFinishTime());
//        log.info("courseRequest.getWantedCategory = {}", courseRequest.getWantedCategory());
//        log.info("courseRequest.getCourseKeywords = {}", courseRequest.getCourseKeywords());
//        log.info("courseRequest.getCourseGoals = {}", courseRequest.getGoals());
//
//
//        // 멤버 데이터 가져오기 - 약속에 함께하는 멤버 리스트 생성
//        // member DB 나오는대로 수정해야 할수도 - ID가 아니라 코드가 pk이면 pk로 find해야 하므로 - 일단 놔둠
//        List<MemberDto> memberList = courseMemberRepository
//                .findAllByIdIn(courseRequest.getMemberIdList())
//                .stream()
//                .map(member -> (MemberDto.fromEntity(member)))
//                .collect(Collectors.toList());
//
//        // 로그로 멤버 리스트 확인
//        for(MemberDto memberDto : memberList) {
//            log.info("Member List = ");
//            log.info("{}", memberDto);
//        }
//        log.info("--- memberList end --- ");
//
//
//        // 멤버들의 특성치 평균 구하기 - 약속에 함께하는 멤버 전체의 피로도, 특이도, 활동성의 평균 값 구하기
//        MemberDto avgMember = new MemberDto();
//        if (!memberList.isEmpty()) {
//            avgMember.setFatigability(memberList.stream().mapToInt(MemberDto::getFatigability).sum() / memberList.size());
//            avgMember.setSpecification(memberList.stream().mapToInt(MemberDto::getSpecification).sum() / memberList.size());
//            avgMember.setActivity(memberList.stream().mapToInt(MemberDto::getActivity).sum() / memberList.size());
//        }
//
//        // 로그로 멤버들의 특성치 평균 확인
//        log.info("avgMember.getFatigability() = {}", avgMember.getFatigability());
//        log.info("avgMember.getActivity() = {}", avgMember.getActivity());
//        log.info("avgMember.getSpecification() = {}", avgMember.getSpecification());
//
//
//        // 시간 제한으로 가능한 모든 코스 받아오기(시간은 24시간으로 받아야함) - 분 말고 몇시인지만 받을거임
//        // 시간에 따라 코스에 속할 카테고리 개수 정하기 - 최대 main 4개나 sub 4개
//        if(courseRequest.getStartTime()>=courseRequest.getFinishTime()){
//            courseRequest.setFinishTime(24+courseRequest.getFinishTime());
//        }
//        int time = courseRequest.getFinishTime() - courseRequest.getStartTime(); // 약속 총 소요시간
//        List<Course> timeCourse = courseRepository.findAllByTime(time); // 해당 time에 맞는 코스리스트 가져오기 - 시간으로 필터링
//        List<CourseDto> filteredCourse = new ArrayList<>();
//        for (Course courseInfo : timeCourse) { // 코스틀 전체에서 코스 하나하나 가져오기
//            List<String> course = courseInfo.getCategories(); // 코스 가져오기 - ex) 놀기Main, 관게Main
//
//            // 필수 장소 있을때 그게 들어가게 일단 필터링 한거 아님?
//            if (courseRequest.getWantedCategory() != null) { // 가고싶은 카테고리 있으면
//                if (!course.contains(courseRequest.getWantedCategory())) { // 필수장소(카테고리) 필터링
//                    continue;
//                }
//            }
//            // 여기까지 filteredCourse에 아무것도 없는거임
////            if (!firstCourseGoalFiltering(courseInfo, courseRequest, filteredCourse)) // 코스 목적으로 필터링
////                continue;
//
//            firstCourseGoalFiltering(courseInfo, courseRequest, filteredCourse); // 코스 목적으로 필터링 - 안에서 filterCourse를 add하고 C열 카테고리에 목적을 map으로 가짐
//
//
//            int N = 0; // 키워드 수치 비교에서 N을 어떻게 할지에 따라 결정
//
//            if (!firstCourseKeywordFiltering(courseInfo, courseRequest, N)) // 코스 키워드로 필터링
//                continue;
//            filteredCourse.add(CourseDto.fromEntity(courseInfo)); // 시간 맞는거 중에서 필수 장소, 목적, 키워드 해당하는 것만 add
//        } // filteredCourse에는 모든 조건에 맞는 C열로 이루어진 코스 틀만 들어있을 것
//
//        // 개수, 목적, 키워드까지로 필터링된 코스 개수와 뭔지 보여주는 로그
//        log.info("filteredCourse.size = {}", filteredCourse.size()); // 필터링된 코스 개수
//        for(int i=0; i< filteredCourse.size(); i++)
//            log.info("filteredCourse List {} = {}", i, filteredCourse.get(i)); // 필터링된 코스 확인
//
//
//        // 코사인 유사도 평가로 뽑는 방법(1)
//        log.info("computeSimilarity Start!");
//        List<CourseDto> courseAfterSimilarity = computeSimilarity(filteredCourse, avgMember);
//        // 랜덤으로 뽑는 방법(2)
//        // 랜덤 객체 생성
//        //Random rand = new Random();
//        // 리스트에서 무작위로 하나의 요소 선택
//        // CourseDto randomCourse = filteredCourse.get(rand.nextInt(filteredCourse.size()));
//        CourseDto selectedCourse = courseAfterSimilarity.get(0); // 유사도 계산하거나 랜덤 뽑은것 중 첫번재로 나온 것을 코스틀로 지정
//        selectedCourse.setMealCheck(courseRequest.isMealCheck()); // 식사 유무 저장
//        selectedCourse.setWantedCategory(courseRequest.getWantedCategory()); // 필수 카테고리 저장
//
//        // wantedCategory가 null인 경우 우선순위에 따른 주장소 선택(main)
//        log.info("firstCourseFiltering Start!");
//        if (selectedCourse.getWantedCategory() == null) { // 필수 카테고리 없는 경우 우선순위에 따라 코스의 주 카테고리 지정
//            // 우선순위에 따른 주장소 선택
//            for (CategoryPriority p : CategoryPriority.values()) {
//                for (String category : selectedCourse.getCategories()) {
//                    if (category.contains(p.getName())) { // 우선순위 순으로 해당 C열 카테고리를 가졌는지 확인
//                        selectedCourse.setWantedCategory(p.getName()); // 가졌으면 wantedCategory로 지정하고 탈출
//                        break;
//                    }
//                }
//                if (selectedCourse.getWantedCategory() != null) { // ???
//                    break;
//                }
//            }
//        }
//        log.info("selectedCourse = {}", selectedCourse);
//        return selectedCourse; // 다 거르고 최종 필터링된 selectedCourse 하나만 반환
//    }


    @Override
    public CourseDto firstCourseFiltering(CourseRequest courseRequest) { // course_v2 사용
        // C열 카테고리로 이루어진 코스 생성
        log.info("firstCourseFiltering Start!");

        // 인자값이 잘 들어왔는지 확인하는 로그
        log.info("courseRequest.getNumPeople = {}", courseRequest.getNumPeople());
        log.info("courseRequest.getMemberIdList = {}", courseRequest.getMemberIdList());
        log.info("courseRequest.isMealCheck = {}", courseRequest.isMealCheck());
        log.info("courseRequest.getStartTime = {}, FinishTime = {}", courseRequest.getStartTime(), courseRequest.getFinishTime());
        log.info("courseRequest.getWantedCategoryGroup = {}", courseRequest.getWantedCategoryGroup());
        log.info("courseRequest.getWantedCategory = {}", courseRequest.getWantedCategory());
        log.info("courseRequest.getCourseKeywords = {}", courseRequest.getCourseKeywords());
        log.info("courseRequest.getCourseGoals = {}", courseRequest.getGoals());


        // 멤버 데이터 가져오기 - 약속에 함께하는 멤버 리스트 생성
        // member DB 나오는대로 수정해야 할수도 - ID가 아니라 코드가 pk이면 pk로 find해야 하므로 - 일단 놔둠
        List<MemberDto> memberList = courseMemberRepository
                .findAllByIdIn(courseRequest.getMemberIdList())
                .stream()
                .map(member -> (MemberDto.fromEntity(member)))
                .collect(Collectors.toList());

        // 로그로 멤버 리스트 확인
        for(MemberDto memberDto : memberList) {
            log.info("Member List = ");
            log.info("{}", memberDto);
        }
        log.info("--- memberList end --- ");


        // 멤버들의 특성치 평균 구하기 - 약속에 함께하는 멤버 전체의 피로도, 특이도, 활동성의 평균 값 구하기
        MemberDto avgMember = new MemberDto();
        if (!memberList.isEmpty()) {
            avgMember.setFatigability(memberList.stream().mapToInt(MemberDto::getFatigability).sum() / memberList.size());
            avgMember.setSpecification(memberList.stream().mapToInt(MemberDto::getSpecification).sum() / memberList.size());
            avgMember.setActivity(memberList.stream().mapToInt(MemberDto::getActivity).sum() / memberList.size());
        }

        // 로그로 멤버들의 특성치 평균 확인
        log.info("avgMember.getFatigability() = {}", avgMember.getFatigability());
        log.info("avgMember.getActivity() = {}", avgMember.getActivity());
        log.info("avgMember.getSpecification() = {}", avgMember.getSpecification());


        boolean dawnDrink = false; // 새벽이라 음주 추천하기 위해 - 기본 false
        // 시간 제한으로 가능한 모든 코스 받아오기(시간은 24시간으로 받아야함) - 분 말고 몇시인지만 받을거임
        // 시간에 따라 코스에 속할 카테고리 개수 정하기 - 최대 main 4개나 sub 4개
        if(courseRequest.getStartTime()>=courseRequest.getFinishTime()){ // 새벽시간대를 고려한 시간 설정 - 이 경우는 새벽 포함
            courseRequest.setFinishTime(24+courseRequest.getFinishTime());
            dawnDrink = true; // 새벽이 포함되면 true로
        }
        int totalTime = courseRequest.getFinishTime() - courseRequest.getStartTime(); // 총 소요 시간


        /** 총 소요 시간으로 필터링 */
        List<CourseV2> timeCourseList = courseV2Repository.findAllByTime(totalTime); // courseV2 테이블에서 시간에 맞는 것들만 courseV2로 뽑아낸 리스트


        List<CourseDto> filteredCourse = new ArrayList<>(); // 마지막에 반환할 CourseDto를 담을 리스트, 모든 조건에 해당하는 것만 담을 예정


        /** 프론트에서 받은 목적리스트 형식 변환 - 참이면 1, 거짓이면 0*/ // 형식 상관없이 줄 수 있다고 함 - int List로 받을 것
//        List<Integer> courseGoalList = new ArrayList<>();
//        for(int i=0; i<10; i++) {
//            if(courseRequest.getGoals().get(i)) {
//                courseGoalList.add(1);
//            }
//            else courseGoalList.add(0);
//        }

        int countGoal = 0; // 목적에 하나라도 해당하는 카테고리가 들어있는지 확인하기 위한 count 변수

        /** 시간으로 필터링한 courseDto에서 목적과 키워드, 필수 장소를 이용하여 필터링 할 것 */
        for (CourseV2 courseInfo : timeCourseList) { // 시간 필터링 리스트에서 코스 정보 하나씩 가져오기 - courseDto

            List<String> course = new ArrayList<>(); // 코스 가져오기 - ex) 놀기Main 스포츠, 관게Main 휴식, ..
            course.add(courseInfo.getCategoryGroupCode1()); // 1번은 무조건 값이 있으므로 그냥 넣음
            if(courseInfo.getCategoryGroupCode2() != null)
                course.add(courseInfo.getCategoryGroupCode2()); // null이 아닌 경우에만 추가
            if(courseInfo.getCategoryGroupCode3() != null)
                course.add(courseInfo.getCategoryGroupCode3()); // null이 아닌 경우에만 추가
            if(courseInfo.getCategoryGroupCode4() != null)
                course.add(courseInfo.getCategoryGroupCode4()); // null이 아닌 경우에만 추가


            /** 필수 장소가 코스에 포함되게 필터링 */
            if (courseRequest.getWantedCategory() != null) { // 가고싶은 카테고리 있으면
                if (!course.contains(courseRequest.getWantedCategoryGroup())) { // 필수장소(카테고리) 필터링 - 없으면 continue하도록
                    continue;
                }
            }

            /** 목적으로 필터링 */

            // 그렇다면 time으로 필터링 한 것 중 목적으로 필터링한거에서 CategoryGroupCode를 가지지 않는 것을 지워보자
            countGoal = 0;
            for(String courseGroupCode : course) { // course에 들어가는 categoryGroup 중 하나라도 목적을 만족하는게 있으면 목적에 맞는거고 아니면 안맞는 것 - 목적3개인데 카테고리 2개짜리인거 있을 수도 있으니까

                // goal이 단 한개라도 있는 category_group_code를 뽑은 리스트
                List<String> goalFilteringList =  coursePurposeRepository.findByCategoryGroupCodeOrWalkOrDrinkOrExperienceOrHealingOrWatchOrIntellectualOrViewOrNormalOrSportsOrSolo(courseGroupCode, courseRequest.getGoals().get(0), courseRequest.getGoals().get(1), courseRequest.getGoals().get(2), courseRequest.getGoals().get(3), courseRequest.getGoals().get(4), courseRequest.getGoals().get(5), courseRequest.getGoals().get(6), courseRequest.getGoals().get(7), courseRequest.getGoals().get(8), courseRequest.getGoals().get(9));

                for(String goalFilteringCategoryGroupCode : goalFilteringList) { // 목적 필터링 리스트에서 CategoryGroupCode만 뽑아
                    if(!courseGroupCode.equals(goalFilteringCategoryGroupCode)) { // 목적으로 필터링한 것을 가지지 않으면 제거
                        continue;
                    }
                    else {// 목적으로 필터링 한것을 가지는 코스 틀이라면
                        countGoal++;

                    }
                }
            }
            // 현재 Dto의 카테고리들이 목적을 만족하는 CourseGroupCode들 중 한 개라도 가지는지 확인
            if(countGoal == 0) { // goal로 필터링 된거랑 하나도 매칭되는게 없으면 timeCourseList에서 빼버리기
                timeCourseList.remove(courseInfo); // 현재 courseDto인 courseInfo 삭제
            }

            /** 목적으로 필터링 끝 */
            // 가고싶은 카테고리 체크 안했으면 우선순위에 따라 처리하는 부분 추가해야 함
            // 사용자가 체크한 목적이 어떤게 어떤 C열 카테고리와 매핑되는지 알 수 있는 변수도 추가해야 함



            int N = 0; // 키워드 수치 비교에서 N을 어떻게 할지에 따라 결정 ----------- 멤버 DB 나오면 해야 함

            if (!firstCourseKeywordFiltering(courseInfo, courseRequest, N)) // 코스 키워드로 필터링
                continue;

            filteredCourse.add(CourseDto.fromEntity(courseInfo)); // 시간 맞는거 중에서 필수 장소, 목적, 키워드 해당하는 것만 add
        } // filteredCourse에는 모든 조건에 맞는 C열로 이루어진 코스 틀만 들어있을 것

        // 개수, 목적, 키워드까지로 필터링된 코스 개수와 뭔지 보여주는 로그
        log.info("filteredCourse.size = {}", filteredCourse.size()); // 필터링된 코스 개수
        for(int i=0; i< filteredCourse.size(); i++)
            log.info("filteredCourse List {} = {}", i, filteredCourse.get(i)); // 필터링된 코스 확인


        // 코사인 유사도 평가로 뽑는 방법(1)
        log.info("computeSimilarity Start!");
        List<CourseDto> courseAfterSimilarity = computeSimilarity(filteredCourse, avgMember);
        // 랜덤으로 뽑는 방법(2)
        // 랜덤 객체 생성
        //Random rand = new Random();
        // 리스트에서 무작위로 하나의 요소 선택
        // CourseDto randomCourse = filteredCourse.get(rand.nextInt(filteredCourse.size()));
        CourseDto selectedCourse = courseAfterSimilarity.get(0); // 유사도 계산하거나 랜덤 뽑은것 중 첫번재로 나온 것을 코스틀로 지정
        selectedCourse.setMealCheck(courseRequest.isMealCheck()); // 식사 유무 저장
        selectedCourse.setWantedCategoryGroup(courseRequest.getWantedCategoryGroup()); // 필수 카테고리 그룹 저장
        selectedCourse.setWantedCategory(courseRequest.getWantedCategory()); // 필수 카테고리 저장

        // categories 변수 지우고 categoryGroupCode1,2,3,4로 DB 맞춰서 새로 만듦
        List<String> course = new ArrayList<>(); // 코스 가져오기 - ex) 놀기Main 스포츠, 관게Main 휴식, ..
        course.add(selectedCourse.getCategoryGroupCode1()); // 1번은 무조건 값이 있으므로 그냥 넣음
        if(selectedCourse.getCategoryGroupCode2() != null)
            course.add(selectedCourse.getCategoryGroupCode2()); // null이 아닌 경우에만 추가
        if(selectedCourse.getCategoryGroupCode3() != null)
            course.add(selectedCourse.getCategoryGroupCode3()); // null이 아닌 경우에만 추가
        if(selectedCourse.getCategoryGroupCode4() != null)
            course.add(selectedCourse.getCategoryGroupCode4()); // null이 아닌 경우에만 추가

        // wantedCategory가 null인 경우 우선순위에 따른 주장소 선택(main)
        if (selectedCourse.getWantedCategory() == null) { // 필수 카테고리 없는 경우 우선순위에 따라 코스의 주 카테고리 지정
            // 우선순위에 따른 주장소 선택
            for (CategoryPriority p : CategoryPriority.values()) {
//                for (String category : selectedCourse.getCategories()) {
                for (String categoryGroup : course) {
                    if (categoryGroup.contains(p.getName())) { // 우선순위 순으로 해당 C열 카테고리를 가졌는지 확인
                        selectedCourse.setWantedCategoryGroup(categoryGroup); // 필수 categoryGroup을 categoryGroup으로
                        selectedCourse.setWantedCategory(p.getName()); // 가졌으면 wantedCategory로 지정하고 탈출
                        break;
                    }
                }
                if (selectedCourse.getWantedCategory() != null) { // 있는 경우 - 그대로 넘어가면 됨
                    break;
                }
            }
        }


        log.info("selectedCourse.getCategoryGroupCode1 = {}", selectedCourse.getCategoryGroupCode1());
        log.info("selectedCourse.getCategoryGroupCode2 = {}", selectedCourse.getCategoryGroupCode2());
        log.info("selectedCourse.getCategoryGroupCode3 = {}", selectedCourse.getCategoryGroupCode3());
        log.info("selectedCourse.getCategoryGroupCode4 = {}", selectedCourse.getCategoryGroupCode4());
        log.info("selectedCourse.getFatigability = {}", selectedCourse.getFatigability());
        log.info("selectedCourse.getSpecification = {}", selectedCourse.getSpecification());
        log.info("selectedCourse.getActivity = {}", selectedCourse.getActivity());
        log.info("selectedCourse.isMealCheck = {}", selectedCourse.isMealCheck());
        log.info("selectedCourse.getWantedCategoryGroup = {}", selectedCourse.getWantedCategoryGroup());
        log.info("selectedCourse.getWantedCategory = {}", selectedCourse.getWantedCategory());

        // 하나로 정해진 selectedCourse의 목적에 따른 CategoryGroup 맵핑시켜주기
        boolean haveGoal; // goal을 가지는 categoryGroup이 있는 경우

        // course - 최종 코스 틀에서 categoryGroup을 갖는 리스트
        for(String courseCategoryGroup : course) {
            List<Integer> goalList = coursePurposeRepository.findWalkDrinkExperienceHealingWatchIntellectualViewNormalSportsSoloByCategoryGroupCode(courseCategoryGroup);

            Map<String, List<Integer>> goalMap = new HashMap<>();
            List<Integer> matching = new ArrayList<>(); // 목적 리스트 0,1로 구성
            haveGoal = false;

            for(int i=0; i<10; i++) {
                if( (courseRequest.getGoals().get(i) == 1) && (goalList.get(i) == 1) ) { // request로 받은 목적 리스트에서 목적이 참이고 해당 courseCategoryGroup의 목적도 1인 경우 추가
                    matching.add(1);
                    haveGoal = true;
                }
                else matching.add(0);
            }
            if(haveGoal) {
                goalMap.put(courseCategoryGroup, matching); // 목적이 하나라도 있으면 맵에 저장하고
                selectedCourse.setGoalMatch(goalMap); // goalMatch에 저장
            }

        }
        log.info("selectedCourse.getGoalMatch = {}", selectedCourse.getGoalMatch());
        if(dawnDrink) selectedCourse.setDawnDrink(true); // 새벽 음주 courseDto에 체크하도록
        log.info("selectedCourse.isDawnDrink = {}", selectedCourse.isDawnDrink());
        return selectedCourse; // 다 거르고 최종 필터링된 selectedCourse 하나만 반환
    }


    // 세부 카테고리를 결정
    // 일단 죄다 랜덤으로 뽑긴 했는데 세부적으로 뭐 기타특징 같은거 고려해야함.
    @Override
    public SecondCourse secondCourseFiltering(CourseDto firstCourse) {

        // categories 변수 지우고 categoryGroupCode1,2,3,4로 DB 맞춰서 새로 만듦
        List<String> course = new ArrayList<>(); // 코스 가져오기 - ex) 놀기Main 스포츠, 관게Main 휴식, ..
        course.add(firstCourse.getCategoryGroupCode1()); // 1번은 무조건 값이 있으므로 그냥 넣음
        if(firstCourse.getCategoryGroupCode2() != null)
            course.add(firstCourse.getCategoryGroupCode2()); // null이 아닌 경우에만 추가
        if(firstCourse.getCategoryGroupCode3() != null)
            course.add(firstCourse.getCategoryGroupCode3()); // null이 아닌 경우에만 추가
        if(firstCourse.getCategoryGroupCode4() != null)
            course.add(firstCourse.getCategoryGroupCode4()); // null이 아닌 경우에만 추가

        SecondCourse secondCourse = new SecondCourse(firstCourse); // firstCourse로 나온 CourseDto로 SecondCoures의 CourseDto selectedCourse 채움
        log.info("SecondCourse.getSelectedCourse.getCategoryGroupCode1 = {}", secondCourse.getSelectedCourse().getCategoryGroupCode1());
        log.info("SecondCourse.getSelectedCourse.getCategoryGroupCode2 = {}", secondCourse.getSelectedCourse().getCategoryGroupCode2());
        log.info("SecondCourse.getSelectedCourse.getCategoryGroupCode3 = {}", secondCourse.getSelectedCourse().getCategoryGroupCode3());
        log.info("SecondCourse.getSelectedCourse.getCategoryGroupCode4 = {}", secondCourse.getSelectedCourse().getCategoryGroupCode4());
        log.info("secondCourse.getSelectedCourse.getWantedCategoryGroup = {}", secondCourse.getSelectedCourse().getWantedCategoryGroup());
        log.info("secondCourse.getSelectedCourse.getWantedCategory = {}", secondCourse.getSelectedCourse().getWantedCategory());
        log.info("secondCourse.getSelectedCourse.getFatigability = {}", secondCourse.getSelectedCourse().getFatigability());
        log.info("secondCourse.getSelectedCourse.getSpecification = {}", secondCourse.getSelectedCourse().getSpecification());
        log.info("secondCourse.getSelectedCourse.getActivity = {}", secondCourse.getSelectedCourse().getActivity());
        log.info("secondCourse.getSelectedCourse.getGoalMatch.keySet = {}", secondCourse.getSelectedCourse().getGoalMatch().keySet());
        log.info("secondCourse.getSelectedCourse.getGoalMatch.values = {}", secondCourse.getSelectedCourse().getGoalMatch().values());

        Random rand = new Random();
        List<String> detailCategories = new ArrayList<>(); // 확정된 d열 카테고리 리스트
        List<Integer> categoryGroupCodeGoalMatch = new ArrayList<>(); // goalMatch에서 꺼낸 리스트 담고
//         리스트에서 무작위로 하나의 요소 선택
//        for (String s : firstCourse.getCategories()) {
        for (String s : course) { // course는 CategoryGroupCode1,2,3,4를 가지는 것

            List<String> categories = categoryRepository.findCategoryCodeByCategoryGroupCode(s); // 해당 categoryGroup에 속하는 category들을 꺼내온 리스트
            String randomCategory = categories.get(rand.nextInt(categories.size())); // category들 리스트에서 랜덤으로 하나 뽑아옴 - D열

            // s가 goalMatch의 Category이면 목적리스트 확인해서 DB 접근해서 어떤 Category가 해당 목적을 가지고 있는지 확인해서 List에 담기 - 여러 개 일 수 있으므로
            categoryGroupCodeGoalMatch = firstCourse.getGoalMatch().get(s);
            // goalMatch에서의 categoryGroup에 속하고 목적10개의 값 중 하나라도 같은 categoryCode들을 모아놓은 list
            List<String> goalMatchingCategoryCodes = coursePurposeRepository.findCategoryCodeByCategoryGroupCodeAnd(s, categoryGroupCodeGoalMatch.get(0), categoryGroupCodeGoalMatch.get(1), categoryGroupCodeGoalMatch.get(2), categoryGroupCodeGoalMatch.get(3), categoryGroupCodeGoalMatch.get(4), categoryGroupCodeGoalMatch.get(5), categoryGroupCodeGoalMatch.get(6), categoryGroupCodeGoalMatch.get(7), categoryGroupCodeGoalMatch.get(8), categoryGroupCodeGoalMatch.get(9));

            if (firstCourse.getWantedCategoryGroup().equals(s)) { // 필수 카테고리 그룹을 course에서 찾았으면
                detailCategories.add(0, firstCourse.getWantedCategory()); // 필수 카테고리 그룹이 s이면 필수 카테고리를 detailCategories에 넣어줌
                secondCourse.setWantedCategoryCode(firstCourse.getWantedCategory()); // wantedCategory(D열)을 채워줌
            }
            // 필수가 아니면 목적을 가지는 D열 카테고리가 한 개 있다면 이것을 먼저 가지고 2개 이상 있다면 그 중에서 랜덤으로 없다면 완전 랜덤으로
            else if (goalMatchingCategoryCodes.size() == 1) { // 1개라면 이거 넣고
                detailCategories.add(goalMatchingCategoryCodes.get(0));
            } else if (goalMatchingCategoryCodes.size() >= 2) { // 여러개라면
                detailCategories.add(goalMatchingCategoryCodes.get(rand.nextInt(goalMatchingCategoryCodes.size()))); // 그 중에서 랜덤으로
            } else {
                detailCategories.add(randomCategory); // 필수 카테고리가 아니고 goalMatching된 categoryGroup도 없는 경우 랜덤한거 넣기
            }
        }
            secondCourse.setDetailCategoryCodes(detailCategories);


//            //원래 내용
//            List<String> smallCategories = SmallCategory.getInstance().getCategories().get(s);
//            String randomCategory = smallCategories.get(rand.nextInt(smallCategories.size()));
//            // firstCourse의 wantedCategory를 secondCourse에 옮기기
//            if(firstCourse.getWantedCategory().equals(s)){
//                detailCategories.add(0, randomCategory);
//                secondCourse.setWantedCategoryCode(firstCourse.getWantedCategory());
//            }else{
//                detailCategories.add(randomCategory);
//            }
//        }
//        secondCourse.setDetailCategoryCodes(detailCategories);
        return secondCourse;
    }

    // 실제 가게들 선택 및 최적화 알고리즘 적용
    @Override
    public List<PlaceDto> finalCourseFiltering(SecondCourse secondCourse) {
        int num=0;
        boolean[] meal;
        int[] partition;
        Double[] mainCoordinate = new Double[]{0.0, 0.0};
        List<PlaceDto> restaurants=new ArrayList<>();
        // wantedCategory 데이터를 전부 별점순으로 가져오기(내림차순)
        List<PlaceDto> wantedCategoryPlaceList = getCategoryPlaceList(secondCourse.getWantedCategoryCode());
        // 카테고리를 돌려보면서 하나하나 반경 기준으로 가져오기.
        List<PlaceDto> wantedCourse = getFinalCourse(secondCourse, wantedCategoryPlaceList, 1, mainCoordinate);
        // 만약 결과가 없다면 두번째 우선순위 카테고리 데이터를 전부 별점순으로 가져오기(내림차순)
        if(wantedCourse == null){
            wantedCategoryPlaceList = getCategoryPlaceList(secondCourse.getDetailCategoryCodes().get(1));
            wantedCourse = getFinalCourse(secondCourse, wantedCategoryPlaceList, 2, mainCoordinate);
            if(wantedCourse == null) throw new RuntimeException("not enough data so no recommendation!");
        }
        // 아침 : 8시~10시 점심 : 12시~2시 저녁 : 6시~8시에 노는시간이 걸쳐있을 경우, 식사 포함. 안걸쳐있으면 그냥 하나만 추가.
        if(secondCourse.getMealCheck()){
            meal=getRestaurantNum(secondCourse.getStartTime(), secondCourse.getEndTime());
            for (int i = 0; i < meal.length; i++) if(meal[i]) num++;
            restaurants = getRestaurantFromApi(mainCoordinate, num);
        }
        // 최적화 알고리즘 적용(순서 결정)
        // 식사 앞뒤로 몇개의 가게 들어갈지 구하는 메소드
        partition=getPartition(wantedCourse, secondCourse.getStartTime(), secondCourse.getEndTime());
        // 최적화(노가다 순열)
        List<PlaceDto> finalCourse = optimizeCourse(wantedCourse, restaurants, partition);
        return finalCourse;
    }




    /**
     * 코스 키워드 필터링
     * @param course
     * @param courseRequest
     * @param N
     * @return
     */
    private Boolean firstCourseKeywordFiltering(CourseV2 course, CourseRequest courseRequest, int N) {
        // N보다 높거나 낮은것을 따지므로
        // course에 키워드가 해당되면 return true, 해당되지 않으면 return false

        for (int i = 0; i < 8; i++) { // 이색, 일상 / 액티비티, 앉아서놀기 / 체력높은, 체력낮음 / 실외위주, 실내위주
            if (courseRequest.getCourseKeywords().get(i) == 1) { // 키워드 하나씩 가져와서 있는 경우만 필터링 진행
                switch (i) {
                    case 0: // 이색
                        if( !(course.getSpecification() > N) ) { // 하나라도 해당되지 않으면 false 반환
                            return false; // 이색인데 특이도가 낮으면 false 반환
                        }
                        else {
                            continue; // 이색인데 특이도가 높으면 다른 키워드들도 비교해봐야 함
                        }

                    case 1: // 일상
                        if( (course.getSpecification() > N) ) {
                            return false; // 일상인데 특이도가 높으면 false 반환
                        }
                        else {
                            continue; // 일상인데 특이도가 낮으면 다른 키워드들도 비교해봐야 함
                        }

                    case 2: // 체력높음
                        if( !(course.getFatigability() > N) ) {
                            return false; // 채력높음인데 피로도가 낮으면 false 반환
                        }
                        else {
                            continue; // 체력높음인데 피로도가 높으면 다른 키워드들도 비교해봐야 함
                        }

                    case 3: // 체력낮음
                        if( (course.getFatigability() > N) ) {
                            return false; // 체력낮음인데 피로도가 높으면 false 반환
                        }
                        else {
                            continue; // 체력낮음인데 피로도가 낮으면 다른 키워드들도 비교해봐야 함
                        }

                    case 4: // 액티비티
                        if( !(course.getActivity() > N) ) {
                            return false; // 액티비티인데 활동성이 낮으면 false 반환
                        }
                        else {
                            continue; // 액티비티인데 활동성이 높으면 다른 키워드들도 비교해봐야 함
                        }

                    case 5: // 앉아서 놀기
                        if( (course.getActivity() > N) ) {
                            return false; // 앉아서놀기인데 활동성이 높으면 false 반환
                        }
                        else {
                            continue; // 앉아서놀기인데 활동성이 낮으면 다른 키워드들도 비교해봐야 함
                        }

                }
            }
        }
        return true; // 위 반복문에서 return false 되지 않으면 해당 목적이 하나라도 있는 것이므로 true 반환

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
        for (CourseDto course : filteredCourse) { // 1차로 필터링된 코스들
            RealVector vector = new ArrayRealVector(new double[]{
                    course.getFatigability(),
                    course.getSpecification(),
                    course.getActivity()
            });

            double cosineSimilarity = (vector.dotProduct(avgVector)) / (vector.getNorm() * avgVector.getNorm()); // getNorm() - L1놈 반환

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

    private List<PlaceDto> getCategoryPlaceList(String category) {
        return placeRepository
                .findAllByCategoryInOrderByRateDesc(category)
                .stream()
                .map(PlaceDto::fromEntity)
                .collect(Collectors.toList());
    }

    private List<PlaceDto> getFinalCourse(SecondCourse secondCourse, List<PlaceDto> wantedCategoryPlaceList, int start, Double[] mainCoordinate) {
        List<PlaceDto> course=new ArrayList<>();
        Set<PlaceDto> uniquePlaces = new HashSet<>(); // 중복 체크를 위한 HashSet
        for(PlaceDto s : wantedCategoryPlaceList){
            course.clear();
            uniquePlaces.clear(); // 새로운 시작 위치마다 HashSet 초기화
            Loop1:
            for (int i = start; i < secondCourse.getDetailCategoryCodes().size(); i++) {
                int maxRetry = 3; // 최대 재시도 횟수 설정
                int retryCount = 0; // 재시도 횟수 카운트

                while (retryCount < maxRetry) {
                    PlaceDto place = PlaceDto.fromEntity(
                            placeRepository.findRandomPlaceByCategoryAndLocationAndRadius(
                                    secondCourse.getDetailCategoryCodes().get(i), s.getX(), s.getY(), FILTER_RADIUS));
                    if (place == null) {
                        break Loop1;
                    } else if (uniquePlaces.add(place)) {
                        course.add(place);
                        if (i == secondCourse.getDetailCategoryCodes().size() - 1) {
                            mainCoordinate[0] = s.getX();
                            mainCoordinate[1] = s.getY();
                            return course;
                        }
                        break;
                    }

                    retryCount++;
                }
                if(retryCount==maxRetry) break;
            }
        }
        return null;
    }

    private List<PlaceDto> getRestaurantFromApi(Double[] mainCoordinate, int num) {
        int cnt=0;
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=FD6&x="+mainCoordinate[0]+"&y="+mainCoordinate[1]+"&radius="+FILTER_RADIUS;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apikey);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("getRestaurantApi failed!");
        }
        JsonNode documents = root.path("documents");;
        List<PlaceDto> places = new ArrayList<>();
        for (JsonNode document : documents) {
            String placeName = document.path("place_name").asText();
            String categoryCode = document.path("category_name").asText();
            String addressName = document.path("address_name").asText();
            double x = Double.parseDouble(document.path("x").asText());
            double y = Double.parseDouble(document.path("y").asText());
            String phone = document.path("phone").asText();

            PlaceDto place = PlaceDto.builder()
                    .name(placeName)
                    .categoryCode(categoryCode)
                    .addressName(addressName)
                    .x(x)
                    .y(y)
                    .phoneNumber(phone)
                    .build();
            if(places.stream().anyMatch(p -> p.getCategoryCode().equals(String.valueOf(categoryCode)))) continue;
            places.add(place);
            cnt++;
            if(cnt>=num){
                for(PlaceDto restaurant : places) restaurant.setCategoryCode("FD6");
                return places;
            }
        }
        return places;
    }

    private boolean[] getRestaurantNum(int start, int end) {
        int cnt=1;
        boolean[] meal=new boolean[]{false, false, false, false};
        if(start<=10){
            meal[0]=true;
            if(end<=14){
                meal[1]=true;
            }
            if (end<=20){
                meal[2]=true;
            }
        }else if(start<=14){
            meal[1]=true;
            if(end<=20){
                meal[2]=true;
            }
        }else if(start<=20){
            meal[2]=true;
        }else{
            meal[3]=true;
        }

        return meal;
    }

    private int[] getPartition(List<PlaceDto> wantedCourse, int start, int end) {
        int[] partTime=new int[4];
        int[] partition = new int[4];
        int cnt=wantedCourse.size();
        for(int i=start;i<=end;i++){
            if(i<9) partTime[0]++;
            else if(i<13) partTime[1]++;
            else if(i<20) partTime[2]++;
            else partTime[3]++;
        }
        if(partTime[1]!=0 && cnt!=0){
            partition[1]++;
            cnt--;
        }
        if(partTime[2]!=0 && cnt!=0){
            partition[2]++;
            cnt--;
        }
        if(partTime[0]!=0 && cnt!=0){
            partition[0]++;
            cnt--;
        }
        if(partTime[3]!=0 && cnt!=0){
            partition[3]++;
            cnt--;
        }
        if(cnt!=0){
            while(cnt!=0){
                int index=0;
                int max=0;
                for (int i = 0; i < partition.length; i++) {
                    if(max<partTime[i] && partTime[i]<=0){
                        max=partTime[i];
                        index=i;
                    }
                }
                partTime[index]-=3;
                cnt--;
            }
        }
        return partition;
    }

    private List<PlaceDto> optimizeCourse(List<PlaceDto> wantedCourse, List<PlaceDto> restaurants, int[] partition) {
        List<PlaceDto> drinkPlace = new ArrayList<>();
        List<PlaceDto> otherPlace = new ArrayList<>();
        List<PlaceDto> finalCourse = new ArrayList<>();
        List<PlaceDto> candidateCourse = new ArrayList<>();

        double minDistance=Double.MAX_VALUE;
        double calculateDistance = 0.0;

        List<List<PlaceDto>> drinkPermutations = new ArrayList<>();
        List<List<PlaceDto>> otherPermutations = new ArrayList<>();
        List<List<PlaceDto>> restaurantPermutations = new ArrayList<>();

        for (PlaceDto place : wantedCourse) {
            if (place.getCategoryCode().equals("RS2")) {
                drinkPlace.add(place);
            } else {
                otherPlace.add(place);
            }
        }

        drinkPermutations = generatePermutations(drinkPlace);
        otherPermutations = generatePermutations(otherPlace);
        restaurantPermutations = generatePermutations(restaurants);

        // 팩토리얼과 순열을 구하는 메소드가 필요함.
        // 노가다하면 구할 수 있음(최대 144번)
        if(drinkPlace.isEmpty()){
            if(restaurants.isEmpty()){
                // 일반형만 있는 경우
                for(List<PlaceDto> otherPerm : otherPermutations){
                    candidateCourse.addAll(otherPerm);
                    calculateDistance = calculateDistance(candidateCourse);
                    if(minDistance>calculateDistance){
                        minDistance = calculateDistance;
                        finalCourse=candidateCourse;
                    }

                }
            }else{
                // 일반형 + 식사체크
                for(List<PlaceDto> otherPerm : otherPermutations){
                    for(List<PlaceDto> restPerm : restaurantPermutations){
                        candidateCourse = makeCandidateCourse(otherPerm, null, restPerm, partition);
                        candidateCourse.addAll(otherPerm);
                        calculateDistance = calculateDistance(candidateCourse);
                        if(minDistance>calculateDistance){
                            minDistance = calculateDistance;
                            finalCourse=candidateCourse;
                        }
                    }
                }
            }
        }else if(otherPlace.isEmpty()){
            if(restaurants.isEmpty()){
                // 음주형만 있는 경우
                for(List<PlaceDto> drinkPerm : drinkPermutations){
                    candidateCourse.addAll(drinkPerm);
                    calculateDistance = calculateDistance(candidateCourse);
                    if(minDistance>calculateDistance){
                        minDistance = calculateDistance;
                        finalCourse=candidateCourse;
                    }
                }
            }else{
                // 음주형 + 식사체크
                for(List<PlaceDto> drinkPerm : drinkPermutations){
                    for(List<PlaceDto> restPerm : restaurantPermutations){
                        candidateCourse = makeCandidateCourse(null, drinkPerm, restPerm, partition);
                        calculateDistance = calculateDistance(candidateCourse);
                        if(minDistance>calculateDistance){
                            minDistance = calculateDistance;
                            finalCourse=candidateCourse;
                        }
                    }
                }
            }
        }else if (restaurants.isEmpty()){
            // 일반형 + 음주형
            for(List<PlaceDto> otherPerm : otherPermutations){
                for(List<PlaceDto> drinkPerm : drinkPermutations){
                    candidateCourse.addAll(otherPerm);
                    candidateCourse.addAll(drinkPerm);
                    calculateDistance = calculateDistance(candidateCourse);
                    if(minDistance>calculateDistance){
                        minDistance = calculateDistance;
                        finalCourse=candidateCourse;
                    }
                }
            }
        }else{
            // 일반형 + 음주형 + 식사체크
            for(List<PlaceDto> otherPerm : otherPermutations){
                for(List<PlaceDto> drinkPerm : drinkPermutations){
                    for(List<PlaceDto> restPerm : restaurantPermutations){
                        candidateCourse = makeCandidateCourse(otherPerm, drinkPerm, restPerm, partition);
                        calculateDistance = calculateDistance(candidateCourse);
                        if(minDistance>calculateDistance){
                            minDistance = calculateDistance;
                            finalCourse=candidateCourse;
                        }
                    }
                }
            }
        }
        return finalCourse;
    }

    private List<PlaceDto> makeCandidateCourse(List<PlaceDto> otherPerm, List<PlaceDto> drinkPerm, List<PlaceDto> restPerm, int[] partition) {
        int otherIdx=0;
        int drinkIdx=0;
        int restIdx=0;
        List<PlaceDto> candidateCourse= new ArrayList<>();
        for (int i = 0; i < partition.length; i++) {
            for (int j = 0; j < partition[i]; j++) {
                if(otherPerm!=null && otherIdx<otherPerm.size()){
                    candidateCourse.add(otherPerm.get(otherIdx));
                    otherIdx++;
                }else if(drinkPerm!=null && drinkIdx<drinkPerm.size()){
                    candidateCourse.add(otherPerm.get(drinkIdx));
                    drinkIdx++;
                }
            }
            if(restPerm!=null && restIdx<restPerm.size()){
                candidateCourse.add(restPerm.get(restIdx));
                restIdx++;
            }
        }
        return candidateCourse;
    }

    private double calculateDistance(List<PlaceDto> candidateCourse) {
        double totalDistance=0.0;

        for (int i = 0; i < candidateCourse.size()-1; i++) {
            PlaceDto placeA = candidateCourse.get(i);
            PlaceDto placeB = candidateCourse.get(i + 1);

            double distance = calculateEuclideanDistance(placeA.getX(), placeA.getY(), placeB.getX(), placeB.getY());
            totalDistance += distance;
        }
        return totalDistance;
    }

    private double calculateEuclideanDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private List<List<PlaceDto>> generatePermutations(List<PlaceDto> places) {
        List<List<PlaceDto>> result = new ArrayList<>(CollectionUtils.permutations(places));
        return result;
    }

}
