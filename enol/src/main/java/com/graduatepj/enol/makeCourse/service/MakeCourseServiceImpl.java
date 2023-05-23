package com.graduatepj.enol.makeCourse.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduatepj.enol.makeCourse.dao.*;
import com.graduatepj.enol.makeCourse.dto.*;
import com.graduatepj.enol.makeCourse.type.CategoryPriority;
import com.graduatepj.enol.makeCourse.vo.CategoryPurpose;
import com.graduatepj.enol.makeCourse.vo.CourseV2;
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
    private final CourseV2Repository courseV2Repository;
    private final CategoryPurposeRepository categoryPurposeRepository;

    // 실제로 해야할 것
    @Override
    public List<PlaceDto> MakeCourse(CourseRequest courseRequest) {
        CourseDto firstCourse = firstCourseFiltering(courseRequest);
        SecondCourse secondCourse = secondCourseFiltering(firstCourse);
        List<PlaceDto> finalCourse = finalCourseFiltering(secondCourse);
        return finalCourse;
    }

    @Override
    public CourseDto firstCourseFiltering(CourseRequest courseRequest) { // course_v2 사용
        // C열 카테고리로 이루어진 코스 생성
        log.info("--- firstCourseFiltering Start!!! ---");

        /** 인자값이 잘 들어왔는지 확인하는 로그 시작 */
        log.info("--- Show courseRequest ---");
        log.info("courseRequest.getNumPeople = {}", courseRequest.getNumPeople());
        log.info("courseRequest.getMemberIdList = {}", courseRequest.getMemberIdList());
        log.info("courseRequest.isMealCheck = {}", courseRequest.isMealCheck());
        log.info("courseRequest.getStartTime = {}, FinishTime = {}", courseRequest.getStartTime(), courseRequest.getFinishTime());
        log.info("courseRequest.getWantedCategoryGroup = {}", courseRequest.getWantedCategoryGroup());
        log.info("courseRequest.getWantedCategory = {}", courseRequest.getWantedCategory());
        log.info("courseRequest.getCourseKeywords = {}", courseRequest.getCourseKeywords());
        log.info("courseRequest.getCourseGoals = {}", courseRequest.getGoals());
        log.info("--- END courseRequest ---");
        /** 인자값이 잘 들어왔는지 확인하는 로그 끝 */

        /** Member List 가져와서 평균 내기 */
        // 멤버 데이터 가져오기 - 약속에 함께하는 멤버 리스트 생성
        // member DB 나오는대로 수정해야 할수도 - ID가 아니라 코드가 pk이면 pk로 find해야 하므로 - 일단 놔둠
        List<MemberDto> memberList = courseMemberRepository
                .findAllByIdIn(courseRequest.getMemberIdList())
                .stream()
                .map(member -> (MemberDto.fromEntity(member)))
                .collect(Collectors.toList());

        // 로그로 멤버 리스트 확인
        log.info("--- memberList Start! --- ");
        for(MemberDto memberDto : memberList) {
            log.info("Member List = ");
            log.info("memberDto.getMemberId = {}", memberDto.getMemberId());
            log.info("memberDto.getMemberName = {}", memberDto.getMemberName());
            log.info("memberDto.getBirthday = {}", memberDto.getBirthday());
            log.info("memberDto.getGender = {}", memberDto.getGender());
            log.info("memberDto.getFatigability = {}", memberDto.getFatigability());
            log.info("memberDto.getSpecification = {}", memberDto.getSpecification());
            log.info("memberDto.getActivity = {}", memberDto.getActivity());
        }
        log.info("--- memberList END! --- ");


        // 멤버들의 특성치 평균 구하기 - 약속에 함께하는 멤버 전체의 피로도, 특이도, 활동성의 평균 값 구하기
        MemberDto avgMember = new MemberDto();
        if (!memberList.isEmpty()) {
            avgMember.setFatigability(memberList.stream().mapToInt(MemberDto::getFatigability).sum() / memberList.size());
            avgMember.setSpecification(memberList.stream().mapToInt(MemberDto::getSpecification).sum() / memberList.size());
            avgMember.setActivity(memberList.stream().mapToInt(MemberDto::getActivity).sum() / memberList.size());
        }

        // 로그로 멤버들의 특성치 평균 확인
        log.info("--- Show avgMember feature Start!!! --- ");
        log.info("avgMember.getFatigability() = {}", avgMember.getFatigability());
        log.info("avgMember.getActivity() = {}", avgMember.getActivity());
        log.info("avgMember.getSpecification() = {}", avgMember.getSpecification());
        log.info("--- Show avgMember feature END!!! --- ");
        /** Member List 가져와서 평균 내기 끝 */


        /** 시간, 목적, 키워드로 필터링한 CourseV2를 DB에서 가져오기 */
        boolean dawnDrink = false; // 새벽이면 음주 추천하기 위해 - 기본 false
        // 시간 제한으로 가능한 모든 코스 받아오기(시간은 24시간으로 받아야함) - 분 말고 몇시인지만 받을거임
        // 시간에 따라 코스에 속할 카테고리 개수 정하기 - 최대 main 4개나 sub 4개
        if(courseRequest.getStartTime()>=courseRequest.getFinishTime()){ // 새벽시간대를 고려한 시간 설정 - 이 경우는 새벽 포함
            courseRequest.setFinishTime(24+courseRequest.getFinishTime());
            dawnDrink = true; // 새벽이 포함되면 true로
        }else if(courseRequest.getStartTime()<=5){ // startTime은 오전 6시 ~ 오전 1시까지만 가능
            courseRequest.setFinishTime(24+courseRequest.getStartTime());
            dawnDrink = true; // 새벽이 포함되면 true로
        }else if(courseRequest.getFinishTime() <= 4){ // finishTime은 오전 7시 ~ 오전 4시까지만 가능
            courseRequest.setFinishTime(24+courseRequest.getFinishTime());
            dawnDrink = true; // 새벽이 포함되면 true로
        }
        int totalTime = courseRequest.getFinishTime() - courseRequest.getStartTime(); // 총 소요 시간

        // 총 소요시간, 사용자가 입력한 키워드, 목적에 대해서 필터링된 코스 - filteringCourse
        List<Integer> keywordAbove = new ArrayList<>();
        List<Integer> keywordBelow = new ArrayList<>();
        keywordAbove.add(0);
        keywordAbove.add(0);
        keywordAbove.add(0);

        keywordBelow.add(100);
        keywordBelow.add(100);
        keywordBelow.add(100);

        if(courseRequest.getCourseKeywords().get(0) == 1) { // 키워드가 이색
            keywordAbove.set(0, avgMember.getSpecification());
        }
        if (courseRequest.getCourseKeywords().get(1) == 1) { // 키워드가 일상
            keywordBelow.set(0, avgMember.getSpecification());
        }
        if(courseRequest.getCourseKeywords().get(2) == 1) { // 키워드가 액티비티
            keywordAbove.set(1, avgMember.getActivity());
        }
        if (courseRequest.getCourseKeywords().get(3) == 1) { // 키워드가 앉아서놀기
            keywordBelow.set(1, avgMember.getActivity());
        }
        if(courseRequest.getCourseKeywords().get(4) == 1) { // 키워드가 체력높음
            keywordAbove.set(2, avgMember.getFatigability());
        }
        if (courseRequest.getCourseKeywords().get(5) == 1) { // 키워드가 체력낮음
            keywordBelow.set(2, avgMember.getFatigability());
        }

        List<CourseV2> filteringCourse = courseV2Repository.findCourseByTimeAndGoalsAndKeywords(totalTime, keywordAbove.get(0), keywordBelow.get(0), keywordAbove.get(1), keywordBelow.get(1), keywordAbove.get(2), keywordBelow.get(2),
                courseRequest.getGoals().get(0), courseRequest.getGoals().get(1), courseRequest.getGoals().get(2), courseRequest.getGoals().get(3), courseRequest.getGoals().get(4), courseRequest.getGoals().get(5), courseRequest.getGoals().get(6),
                courseRequest.getGoals().get(7), courseRequest.getGoals().get(8), courseRequest.getGoals().get(9));

        log.info("filteringCourse.size = {}", filteringCourse.size());
        for(int i=0; i<filteringCourse.size(); i++) {
            log.info("filteringCourse.get({}).getCategoryGroupCode1() = {}", i, filteringCourse.get(i).getCategoryGroupCode1());
            log.info("filteringCourse.get({}).getCategoryGroupCode2() = {}", i, filteringCourse.get(i).getCategoryGroupCode2());
            log.info("filteringCourse.get({}).getCategoryGroupCode3() = {}", i, filteringCourse.get(i).getCategoryGroupCode3());
            log.info("filteringCourse.get({}).getCategoryGroupCode4() = {}", i, filteringCourse.get(i).getCategoryGroupCode4());
            log.info("filteringCourse.get({}).getFatigability() = {}", i, filteringCourse.get(i).getFatigability());
            log.info("filteringCourse.get({}).getSpecification() = {}", i, filteringCourse.get(i).getSpecification());
            log.info("filteringCourse.get({}).getActivity() = {}", i, filteringCourse.get(i).getActivity());
        }

        /** DB에서 CourseV2 가져오기 끝 */

        /** 필수 장소가 코스에 포함되게 필터링 */
        List<Integer> removeFilteringIdx = new ArrayList<>(); // 필수 장소가 포함되지 않는 CourseV2는 지우기 위한 index list
        List<CourseDto> filteredCourse = new ArrayList<>(); // CourseDTO로 변환하여 담기 위한 List

        for(int i = 0; i < filteringCourse.size(); i++) {

            // 시간, 키워드, 목적으로 필터링된 courseV2에서 i번째에 해당하는 categoryGroupCode1,2,3,4만 가져와서 course로 묶어서 저장
            List<String> course = new ArrayList<>(); // i번 인덱스에 해당하는 categoryGroupCode들을 리스트로 가져오기 - ex) 놀기Main 스포츠, 관게Main 휴식, ..
            course.add(filteringCourse.get(i).getCategoryGroupCode1()); // 1번은 무조건 값이 있으므로 그냥 넣음
            if(filteringCourse.get(i).getCategoryGroupCode2() != null)
                course.add(filteringCourse.get(i).getCategoryGroupCode2()); // null이 아닌 경우에만 추가
            if(filteringCourse.get(i).getCategoryGroupCode3() != null)
                course.add(filteringCourse.get(i).getCategoryGroupCode3()); // null이 아닌 경우에만 추가
            if(filteringCourse.get(i).getCategoryGroupCode4() != null)
                course.add(filteringCourse.get(i).getCategoryGroupCode4()); // null이 아닌 경우에만 추가

            if (courseRequest.getWantedCategory() != null)  // 가고싶은 카테고리가 있으면
                if (!course.contains(courseRequest.getWantedCategoryGroup())) { // 필수장소(카테고리) 필터링
                    log.info("removeIndex1 = {}", i);
                    removeFilteringIdx.add(i); // 현재 filertingCourse에 필수 장소가 포함되지 않으면 나중에 한꺼번에 지우기 위해서 list에 i를 추가
                }
        }

        for(int i = removeFilteringIdx.size(); i >= 0; i--) { // 뒤에서부터 removeIdx로 넣어놓은 제거할 것들을 제거
            if(removeFilteringIdx.size()>0) {
                log.info("removeIdx2 = {}", removeFilteringIdx.get(i));
                filteringCourse.remove(removeFilteringIdx.get(i));
            }
        }
        /** 필수 장소가 코스에 포함되게 필터링 끝 */

        /** 시간, 목적, 키워드로 필터링한 코스 - CourseV2 의 개수와 종류를 보여주는 로그 시작 */
        log.info("--- Show filteredCourse!! ---"); // 필터링된 코스 개수
        log.info("filteredCourse.size = {}", filteringCourse.size()); // 필터링된 코스 개수
        for(int i = 0; i < filteringCourse.size(); i++) {
            filteredCourse.add(CourseDto.fromEntity(filteringCourse.get(i))); // CourseV2를 CourseDTO로 변환하는 코드
            // 필터링된 코스를 DTO로 바꾼 것 확인
            log.info("filteredCourse List {}.categoryGroupCode1 = {}", i, filteredCourse.get(i).getCategoryGroupCode1());
            log.info("filteredCourse List {}.CategoryGroupCode2 = {}", i, filteredCourse.get(i).getCategoryGroupCode2());
            log.info("filteredCourse List {}.CategoryGroupCode3 = {}", i, filteredCourse.get(i).getCategoryGroupCode3());
            log.info("filteredCourse List {}.CategoryGroupCode4 = {}", i, filteredCourse.get(i).getCategoryGroupCode4());
            log.info("filteredCourse List {}.Time = {}", i, filteredCourse.get(i).getTime());
            log.info("filteredCourse List {}.Fatigability = {}", i, filteredCourse.get(i).getFatigability());
            log.info("filteredCourse List {}.Specification = {}", i, filteredCourse.get(i).getSpecification());
            log.info("filteredCourse List {}.Activity = {}", i, filteredCourse.get(i).getActivity());
            log.info("filteredCourse List {}.Rate = {}", i, filteredCourse.get(i).getRate());
            log.info(""); // 리스트별로 구분하기 위해서 그냥 넣은거
        }
        log.info("-- Show filteredCourse END!! ---"); // 필터링된 코스 개수
        /** 시간, 목적, 키워드로 필터링한 코스의 개수와 종류를 보여주는 로그 끝 */

        /** 코사인 유사도 평가로 1개만 고르기 */
        // 코사인 유사도 평가로 뽑는 방법(1)
        log.info("--- computeSimilarity Start! ---");
        List<CourseDto> courseAfterSimilarity = computeSimilarity(filteredCourse, avgMember); // 평균 멤버로 코사인유사도 평가
        // 랜덤으로 뽑는 방법(2)
        // 랜덤 객체 생성
        //Random rand = new Random();
        // 리스트에서 무작위로 하나의 요소 선택
        // CourseDto randomCourse = filteredCourse.get(rand.nextInt(filteredCourse.size()));
        CourseDto selectedCourse = courseAfterSimilarity.get(0); // 유사도 계산하거나 랜덤 뽑은것 중 첫번째로 나온 것을 코스틀로 지정 - 큰 틀 1개만 뽑기
        selectedCourse.setMealCheck(courseRequest.isMealCheck()); // 식사 유무 저장
        selectedCourse.setWantedCategoryGroup(courseRequest.getWantedCategoryGroup()); // 필수 카테고리 그룹 저장
        selectedCourse.setWantedCategory(courseRequest.getWantedCategory()); // 필수 카테고리 저장


        /** wantedCategory 없는 경우 우선순위로 주장소 정하기 */
        if (selectedCourse.getWantedCategory() == null) { // 필수 카테고리 없는 경우 우선순위에 따라 코스의 주 카테고리 지정

            selectedCourse.setWantedCategoryGroup(selectedCourse.getCategoryGroupCode1()); // 필수 categoryGroup을 categoryGroupCode1로

            // Random하게 categoryGroupCode에 속하는 categoryCode를 골라서 wantedCategory로 설정
            Random rand = new Random();
            List<String> categories = categoryRepository.findCategoryCodeByCategoryGroupCode(selectedCourse.getCategoryGroupCode1()); // 해당 categoryGroup에 속하는 category들을 꺼내온 리스트
            selectedCourse.setWantedCategory(categories.get(rand.nextInt(categories.size()))); // category들 리스트에서 랜덤으로 하나 뽑아옴 - D열

        }
        /** categoryGroupCode1,2,3,4에 우선순위 순으로 위치 바꾸는 부분 */
        else { // 필수 카테고리가 있으면 우선순위에 따라 순서 변경해주기
            String firstCategoryGroup = selectedCourse.getCategoryGroupCode1(); // 우선순위가 가장 높은 카테고리그룹코드

            if(selectedCourse.getWantedCategoryGroup().equals(selectedCourse.getCategoryGroupCode2())) {
                selectedCourse.setCategoryGroupCode1(selectedCourse.getWantedCategoryGroup()); // 2번째에 있는 필수 카테고리 그룹을 1번째로 옮기기
                selectedCourse.setCategoryGroupCode2(firstCategoryGroup); // 2번째 자리에 원래 1번째 있던 카테고리 그룹 넣기
            }
            else if(selectedCourse.getWantedCategoryGroup().equals(selectedCourse.getCategoryGroupCode3())) { // 3번째 카테고리그룹이 필수 카테고리 그룹인 경우
                // 3번째를 1번째로, 2번째를 3번째로, 1번째를 2번째로
                selectedCourse.setCategoryGroupCode1(selectedCourse.getWantedCategoryGroup()); // 3번째 카테고리그룹이 필수 카테고리 그룹인 경우
                selectedCourse.setCategoryGroupCode3(selectedCourse.getCategoryGroupCode2()); // 2->3
                selectedCourse.setCategoryGroupCode2(firstCategoryGroup); // 1->2
            }
            else if(selectedCourse.getWantedCategoryGroup().equals(selectedCourse.getCategoryGroupCode4())) { // 4번째 카테고리그룹이 필수 카테고리 그룹인 경우
                // 4번째를 1번째로, 3번째를 4번째로, 2번째를 3번째로, 1번째를 2번째로
                selectedCourse.setCategoryGroupCode1(selectedCourse.getWantedCategoryGroup()); // 4->1
                selectedCourse.setCategoryGroupCode4(selectedCourse.getCategoryGroupCode3()); // 3->4
                selectedCourse.setCategoryGroupCode3(selectedCourse.getCategoryGroupCode2()); // 2->3
                selectedCourse.setCategoryGroupCode2(firstCategoryGroup); // 1->2
            }
        }
        /** categoryGroupCode1,2,3,4에 우선순위 순으로 위치 바꾸는 부분 끝 */

        // 코사인 유사도 평가로 뽑은 1개에서 최종 우선순위 순으로 정렬된 한 개의 코스 틀
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
        log.info(""); // 구분하기 위한 빈줄

        /** 코사인 유사도 평가로 1개 뽑기 끝 */


        /** categoryGroup과 매칭되는 목적 맵핑 */
        List<String> selectedCourseGroups = new ArrayList<>(); // 코스 가져오기 - ex) 놀기Main 스포츠, 관게Main 휴식, ..
        selectedCourseGroups.add(selectedCourse.getCategoryGroupCode1()); // 1번은 무조건 값이 있으므로 그냥 넣음
        if(selectedCourse.getCategoryGroupCode2() != null)
            selectedCourseGroups.add(selectedCourse.getCategoryGroupCode2()); // null이 아닌 경우에만 추가
        if(selectedCourse.getCategoryGroupCode3() != null)
            selectedCourseGroups.add(selectedCourse.getCategoryGroupCode3()); // null이 아닌 경우에만 추가
        if(selectedCourse.getCategoryGroupCode4() != null)
            selectedCourseGroups.add(selectedCourse.getCategoryGroupCode4()); // null이 아닌 경우에만 추가
        // 하나로 정해진 selectedCourse의 목적에 따른 CategoryGroup 맵핑시켜주기
        boolean haveGoal; // goal을 가지는 categoryGroup이 있는 경우
        for(int i=0; i<selectedCourseGroups.size(); i++) {
            log.info("selectedCourseGroups.get({}) = {}", i, selectedCourseGroups.get(i));

        }

        // 여기 다시 보기
        // selectedCourseGroups - 최종 코스 틀에서 categoryGroup을 갖는 리스트
        for(String courseCategoryGroup : selectedCourseGroups) {
            log.info("courseCategoryGroup = {}", courseCategoryGroup);
//            List<Integer> goalList = categoryPurposeRepository.findWalkDrinkExperienceHealingWatchIntellectualViewNormalSportsSoloByCategoryGroupCode(courseCategoryGroup);
            // 사용자가 입력한 목적 중 하나라도 1이고 카테고리 그룹 코드가 해당하는 것을 골라야 하는것 아닌가? - 다시 보고 해야할듯
            List<CategoryPurpose> goalCategoryList = categoryPurposeRepository.findByCategoryGroupCode(courseCategoryGroup, courseRequest.getGoals().get(0), courseRequest.getGoals().get(1),courseRequest.getGoals().get(2),courseRequest.getGoals().get(3),courseRequest.getGoals().get(4),courseRequest.getGoals().get(5),courseRequest.getGoals().get(6),courseRequest.getGoals().get(7),courseRequest.getGoals().get(8),courseRequest.getGoals().get(9)); // 목적이 하나라도 1이고 Course_Category_Group이 courseCategoryGroup인 categoryPurposeList

            for(int i=0; i< goalCategoryList.size(); i++) {
                log.info("goalCategoryList.get({}).getCategoryGroupCode = {}", i, goalCategoryList.get(i).getCategoryGroupCode());
                log.info("goalCategoryList.get({}).getCategoryCode = {}", i, goalCategoryList.get(i).getCategoryCode());
                log.info("goalCategoryList.get({}).getWalk = {}", i, goalCategoryList.get(i).getWalk());
                log.info("goalCategoryList.get({}).getDrink = {}", i, goalCategoryList.get(i).getDrink());
                log.info("goalCategoryList.get({}).getExperience = {}", i, goalCategoryList.get(i).getExperience());
                log.info("goalCategoryList.get({}).getWatch = {}", i, goalCategoryList.get(i).getWatch());
                log.info("goalCategoryList.get({}).getHealing = {}", i, goalCategoryList.get(i).getHealing());
                log.info("goalCategoryList.get({}).getView = {}", i, goalCategoryList.get(i).getView());
                log.info("goalCategoryList.get({}).getIntellectual = {}", i, goalCategoryList.get(i).getIntellectual());
                log.info("goalCategoryList.get({}).getNormal = {}", i, goalCategoryList.get(i).getNormal());
                log.info("goalCategoryList.get({}).getSports = {}", i,  goalCategoryList.get(i).getSports());
                log.info("goalCategoryList.get({}).getSolo = {}", i,  goalCategoryList.get(i).getSolo());
            }

            List<List<Integer>> goalList = new ArrayList<>(); // goalCategoryList로 나온 것들에서 목적만 뽑아낼 것
            List<Integer> goalListI = new ArrayList<>(); // i번째에 해당하는 goalList들을 저장할 리스트
            for(int i=0; i< goalCategoryList.size(); i++) { // 해당하는 카테고리그룹 에 해당하는 목적들을 저장
                goalListI.clear(); // 반복 될 때마다 다 지우기

                goalListI.add(goalCategoryList.get(i).getWalk());
                goalListI.add(goalCategoryList.get(i).getDrink());
                goalListI.add(goalCategoryList.get(i).getExperience());
                goalListI.add(goalCategoryList.get(i).getHealing());
                goalListI.add(goalCategoryList.get(i).getWatch());
                goalListI.add(goalCategoryList.get(i).getIntellectual());
                goalListI.add(goalCategoryList.get(i).getView());
                goalListI.add(goalCategoryList.get(i).getNormal());
                goalListI.add(goalCategoryList.get(i).getSports());
                goalListI.add(goalCategoryList.get(i).getSolo());

                goalList.add(goalListI); // 목적들 리스트를 갖는 리스트를 만드는 것
            }

            Map<String, List<Integer>> goalMap = new HashMap<>();
            List<Integer> matching = new ArrayList<>(); // 목적 리스트 0,1로 구성

            for(int i=0; i<goalCategoryList.size(); i++) {
                haveGoal = false;
                matching.clear(); // 반복될 때마다 초기화
                for(int j=0; j<goalList.get(i).size(); j++) {
                    System.out.println("courseRequest.getGoals().get(" + j + ") = " + courseRequest.getGoals().get(j));
                    System.out.println("goalList.get(" + i + ").get(" + j + ") = " + goalList.get(i).get(j));
                    if( (courseRequest.getGoals().get(j) == 1) && (goalList.get(i).get(j) == 1) ) { // request로 받은 목적 리스트에서 목적이 참이고 해당 courseCategoryGroup의 목적도 1인 경우 추가
                        matching.add(1);
                        haveGoal = true;
                    }
                    else matching.add(0);
                }
                if(haveGoal) {
                    goalMap.put(courseCategoryGroup, matching); // 목적이 하나라도 있으면 맵에 저장하고
//                    selectedCourse.setGoalMatch(goalMap); // goalMatch에 저장
                }
            }
            selectedCourse.setGoalMatch(goalMap); // goalMatch에 저장 - 1개 이상의 목적이 있기 때문에 무조건 1개 이상의 goalMap의 원소가 존재할 것

        }
        log.info("selectedCourse.getGoalMatch.size = {}", selectedCourse.getGoalMatch().size());
        log.info("selectedCourse.getGoalMatch = {}", selectedCourse.getGoalMatch());
        if(dawnDrink) selectedCourse.setDawnDrink(true); // 새벽 음주 courseDto에 체크하도록
        log.info("selectedCourse.isDawnDrink = {}", selectedCourse.isDawnDrink());
        return selectedCourse; // 다 거르고 최종 필터링된 selectedCourse 하나만 반환

        // 가고싶은 카테고리 체크 안했으면 우선순위에 따라 처리하는 부분 추가해야 함
        // 사용자가 체크한 목적이 어떤게 어떤 C열 카테고리와 매핑되는지 알 수 있는 변수도 추가해야 함

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
        List<String> resultDetailCategories = new ArrayList<>(); // 우선순위 고려해서 순서 맞춘 d열 카테고리 코스
        List<Integer> categoryGroupCodeGoalMatch = new ArrayList<>(); // goalMatch에서 꺼낸 목적 리스트 담고
//         리스트에서 무작위로 하나의 요소 선택
//        for (String s : firstCourse.getCategories()) {
        for (String s : course) { // course는 CategoryGroupCode1,2,3,4를 가지는 것

            List<String> categories = categoryRepository.findCategoryCodeByCategoryGroupCode(s); // 해당 categoryGroup에 속하는 category들을 꺼내온 리스트
            String randomCategory = categories.get(rand.nextInt(categories.size())); // category들 리스트에서 랜덤으로 하나 뽑아옴 - D열

            // s가 goalMatch의 Category이면 목적리스트 확인해서 DB 접근해서 어떤 Category가 해당 목적을 가지고 있는지 확인해서 List에 담기 - 여러 개 일 수 있으므로
            categoryGroupCodeGoalMatch = firstCourse.getGoalMatch().get(s); // C열은 중복되는거 없으니까 map의 get으로 불러와도 됨
            // goalMatch에서의 categoryGroup에 속하고 목적10개의 값 중 하나라도 같은 categoryCode들을 모아놓은 list
            List<String> goalMatchingCategoryCodes = categoryPurposeRepository.findCategoryCodeByCategoryGroupCode(s, categoryGroupCodeGoalMatch.get(0), categoryGroupCodeGoalMatch.get(1), categoryGroupCodeGoalMatch.get(2), categoryGroupCodeGoalMatch.get(3), categoryGroupCodeGoalMatch.get(4), categoryGroupCodeGoalMatch.get(5), categoryGroupCodeGoalMatch.get(6), categoryGroupCodeGoalMatch.get(7), categoryGroupCodeGoalMatch.get(8), categoryGroupCodeGoalMatch.get(9));

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

        for(int i=0; i<detailCategories.size(); i++) {

        }
        secondCourse.setDetailCategoryCodes(detailCategories); // secondCourse에 확정된 코스 넣는 부분


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
        int num = 0;
        boolean[] meal= new boolean[]{false, false, false, false};
        int[] partition;
        Double[] mainCoordinate = new Double[]{0.0, 0.0};
        List<PlaceDto> restaurants = new ArrayList<>();
        // wantedCategory 데이터를 전부 별점순으로 가져오기(내림차순)
        List<PlaceDto> wantedCategoryPlaceList = getCategoryPlaceList(secondCourse.getWantedCategoryCode());
        // 카테고리를 돌려보면서 하나하나 반경 기준으로 가져오기.
        List<PlaceDto> wantedCourse = getFinalCourse(secondCourse, wantedCategoryPlaceList, 1, mainCoordinate);
        // 만약 결과가 없다면 두번째 우선순위 카테고리 데이터를 전부 별점순으로 가져오기(내림차순)
        if (wantedCourse == null) {
            wantedCategoryPlaceList = getCategoryPlaceList(secondCourse.getDetailCategoryCodes().get(1));
            wantedCourse = getFinalCourse(secondCourse, wantedCategoryPlaceList, 2, mainCoordinate);
            // TODO : 가장 가까운 wantedCategory를 코스에 포함

            if (wantedCourse == null)
                throw new RuntimeException("not enough data so no recommendation!");
        }
        // 아침 : 8시~10시 점심 : 12시~2시 저녁 : 6시~8시에 노는시간이 걸쳐있을 경우, 식사 포함. 안걸쳐있으면 그냥 하나만 추가.
        if (secondCourse.getMealCheck()) {
            meal = getRestaurantNum(secondCourse.getStartTime(), secondCourse.getEndTime());
            for (int i = 0; i < meal.length; i++) if (meal[i]) num++;
            restaurants = getRestaurantFromApi(mainCoordinate, num);
        }
        // 최적화 알고리즘 적용(순서 결정)
        // 식사 앞뒤로 몇개의 가게 들어갈지 구하는 메소드
        partition = getPartition(wantedCourse, secondCourse.getStartTime(), secondCourse.getEndTime());
        // 최적화(노가다 순열)
        return optimizeCourse(wantedCourse, restaurants, partition, meal, secondCourse.getStartTime());
    }

    // 프론트 테스트용
    @Override
    public List<PlaceDto> testCourse() {
        int placeCount = (int) (Math.random() * 5) + 1;  // 1에서 5 사이의 랜덤한 개수
        int restCount = (int) (Math.random() * 4);  // 0에서 3 사이의 랜덤한 개수
        List<PlaceDto> testCourseList = placeRepository
                .findRandomPlaces(placeCount)
                .stream()
                .map(PlaceDto::fromEntity)
                .collect(Collectors.toList());
        Double[] mainCoordinate = new Double[]{testCourseList.get(0).getX(), testCourseList.get(0).getY()};
        testCourseList.addAll(getRestaurantFromApi(mainCoordinate, restCount));
        log.info(String.valueOf(placeCount));
        log.info(String.valueOf(restCount));
        return testCourseList;
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

    private List<PlaceDto> getCategoryPlaceList(String categoryCode) {
        List<PlaceDto> placeDtoList = placeRepository
                .findAllByCategoryCode(categoryCode)
//                .findAllByCategoryInOrderByRateDesc(categoryCode)
                .stream()
                .map(PlaceDto::fromEntity)
                .collect(Collectors.toList());
        Collections.shuffle(placeDtoList);
        return placeDtoList;
    }

    private List<PlaceDto> getFinalCourse(SecondCourse secondCourse, List<PlaceDto> wantedCategoryPlaceList, int start, Double[] mainCoordinate) {
        List<PlaceDto> course = new ArrayList<>();
        Set<PlaceDto> uniquePlaces = new HashSet<>(); // 중복 체크를 위한 HashSet
        for (PlaceDto s : wantedCategoryPlaceList) {
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
                if (retryCount == maxRetry) break;
            }
        }
        return null;
    }

    private List<PlaceDto> getRestaurantFromApi(Double[] mainCoordinate, int num) {
        int cnt = 0;
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=FD6&x=" + mainCoordinate[0] + "&y=" + mainCoordinate[1] + "&radius=" + FILTER_RADIUS;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apikey);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
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
        JsonNode documents = root.path("documents");
        ;
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
            if (places.stream().anyMatch(p -> p.getCategoryCode().equals(String.valueOf(categoryCode))))
                continue;
            places.add(place);
            cnt++;
            if (cnt >= num) {
                for (PlaceDto restaurant : places)
                    restaurant.setCategoryCode("FD6");
                return places;
            }
        }
        return places;
    }

    private boolean[] getRestaurantNum(int start, int end) {
        boolean[] meal = new boolean[]{false, false, false, false};
        if(start<=10) { // 아침, 점심, 저녁 포함 가능
            // 시간대 미포함
            if(end<8){
                meal[3]=true;
                return meal;
            }
            // 아침만 포함
            if(end<12) meal[0]=true;
                // 점심까지 포함
            else if(end<18 && end>=12) {
                meal[0]=true;
                meal[1]=true;
            }
            // 다 포함
            else if(end>=18) {
                meal[0]=true;
                meal[1]=true;
                meal[2]=true;
            }
        } else if(start<=14){ // 점심, 저녁 포함 가능
            // 시간대 미포함
            if(end<=11){
                meal[3]=true;
                return meal;
            }
            // 점심만 포함
            if(end<18 && end>=12) meal[1]=true;
                // 저녁도 포함
            else if(end>=18) {
                meal[1]=true;
                meal[2]=true;
            }
        } else if(start<=20){ // 저녁 포함 가능
            // 시간대 미포함
            if(end<18){
                meal[3]=true;
                return meal;
            }
            // 저녁 포함
            if(end>=18) meal[2]=true;
        } else{
            meal[3]=true;
        }

        return meal;
    }

    private int[] getPartition(List<PlaceDto> wantedCourse, int start, int end) {
        int[] partTime;
        int[] partition = new int[4];
        boolean[] partitionCheck = new boolean[]{false, false, false, false};
        int cnt = wantedCourse.size();
        int maxIndex=0;
        int maxValue=Integer.MIN_VALUE;
        partTime=getPartTime(start, end);
        for (int i = 0; i < partTime.length; i++) {
            if(partTime[i]!=0) partitionCheck[i]=true;
        }
        System.out.println("시간 : 아침 전 = "+ partTime[0] +" | 아침/점심 사이 = "+ partTime[1] +" | 점심/저녁 사이 = "+ partTime[2] + " | 저녁 이후 = " + partTime[3]);
        while(cnt>0) {
            for (int i = 0; i < partTime.length; i++) {
                if (partTime[i] > maxValue && partitionCheck[i]) {
                    maxValue = partTime[i];
                    maxIndex = i;
                }
            }
            partition[maxIndex]++;
            cnt--;
            partTime[maxIndex] -= 3;
            maxValue = partTime[maxIndex];
        }
        return partition;
    }

    private int[] getPartTime(int start, int end){
        int[] partTime = new int[4];
        for (int i = start; i < end; i++) {
            if (i < 9 && i>=6) {
                partTime[0]++;
            }
            else if (i < 13 && i>=9) {
                partTime[1]++;
            }
            else if (i < 20 && i>=13) {
                partTime[2]++;
            }
            else {
                partTime[3]++;
            }
        }
        return partTime;
    }

    private List<PlaceDto> optimizeCourse(List<PlaceDto> wantedCourse, List<PlaceDto> restaurants, int[] partition, boolean[] mealCheck, int startTime) {
        List<PlaceDto> drinkPlace = new ArrayList<>();
        List<PlaceDto> otherPlace = new ArrayList<>();
        List<PlaceDto> finalCourse = new ArrayList<>();
        List<PlaceDto> candidateCourse = new ArrayList<>();

        double minDistance = Double.MAX_VALUE;
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
        if (drinkPlace.isEmpty()) {
            if (restaurants.isEmpty()) {
                // 일반형만 있는 경우
                for (List<PlaceDto> otherPerm : otherPermutations) {
                    candidateCourse.addAll(otherPerm);
                    calculateDistance = calculateDistance(candidateCourse);
                    if (minDistance > calculateDistance) {
                        minDistance = calculateDistance;
                        finalCourse = candidateCourse;
                    }
                }
            } else {
                // 일반형 + 식사체크
                for (List<PlaceDto> otherPerm : otherPermutations) {
                    for (List<PlaceDto> restPerm : restaurantPermutations) {
                        candidateCourse = makeCandidateCourse(otherPerm, null, restPerm, partition, mealCheck, startTime);
                        candidateCourse.addAll(otherPerm);
                        calculateDistance = calculateDistance(candidateCourse);
                        if (minDistance > calculateDistance) {
                            minDistance = calculateDistance;
                            finalCourse = candidateCourse;
                        }
                    }
                }
            }
        } else if (otherPlace.isEmpty()) {
            if (restaurants.isEmpty()) {
                // 음주형만 있는 경우
                for (List<PlaceDto> drinkPerm : drinkPermutations) {
                    candidateCourse.addAll(drinkPerm);
                    calculateDistance = calculateDistance(candidateCourse);
                    if (minDistance > calculateDistance) {
                        minDistance = calculateDistance;
                        finalCourse = candidateCourse;
                    }
                }
            } else {
                // 음주형 + 식사체크
                for (List<PlaceDto> drinkPerm : drinkPermutations) {
                    for (List<PlaceDto> restPerm : restaurantPermutations) {
                        candidateCourse = makeCandidateCourse(null, drinkPerm, restPerm, partition, mealCheck, startTime);
                        calculateDistance = calculateDistance(candidateCourse);
                        if (minDistance > calculateDistance) {
                            minDistance = calculateDistance;
                            finalCourse = candidateCourse;
                        }
                    }
                }
            }
        } else if (restaurants.isEmpty()) {
            // 일반형 + 음주형
            for (List<PlaceDto> otherPerm : otherPermutations) {
                for (List<PlaceDto> drinkPerm : drinkPermutations) {
                    candidateCourse.addAll(otherPerm);
                    candidateCourse.addAll(drinkPerm);
                    calculateDistance = calculateDistance(candidateCourse);
                    if (minDistance > calculateDistance) {
                        minDistance = calculateDistance;
                        finalCourse = candidateCourse;
                    }
                }
            }
        } else {
            // 일반형 + 음주형 + 식사체크
            for (List<PlaceDto> otherPerm : otherPermutations) {
                for (List<PlaceDto> drinkPerm : drinkPermutations) {
                    for (List<PlaceDto> restPerm : restaurantPermutations) {
                        candidateCourse = makeCandidateCourse(otherPerm, drinkPerm, restPerm, partition, mealCheck, startTime);
                        calculateDistance = calculateDistance(candidateCourse);
                        if (minDistance > calculateDistance) {
                            minDistance = calculateDistance;
                            finalCourse = candidateCourse;
                        }
                    }
                }
            }
        }
        return finalCourse;
    }

    private List<PlaceDto> makeCandidateCourse(List<PlaceDto> otherPerm, List<PlaceDto> drinkPerm, List<PlaceDto> restPerm, int[] partition, boolean[] mealCheck, int startTime) {
        int otherIdx = 0;
        int drinkIdx = 0;
        int restIdx = 0;
        List<PlaceDto> candidateCourse = new ArrayList<>();
        for (int i = 0; i < partition.length; i++) {
            for (int j = 0; j < partition[i]; j++) {
                if (otherPerm != null && otherIdx < otherPerm.size()) {
                    candidateCourse.add(otherPerm.get(otherIdx));
                    otherIdx++;
                } else if (drinkPerm != null && drinkIdx < drinkPerm.size()) {
                    candidateCourse.add(drinkPerm.get(drinkIdx));
                    drinkIdx++;
                }
            }
            if (restPerm != null && restIdx < restPerm.size() && mealCheck[i] && i<partition.length-1) {
                candidateCourse.add(restPerm.get(restIdx));
                restIdx++;
            }
        }
        if(restPerm!=null && mealCheck[3]){
            if(startTime<=20){
                candidateCourse.add(restPerm.get(restIdx));
            }else{
                candidateCourse.add(0, restPerm.get(restIdx));
            }
        }
        return candidateCourse;
    }

    private double calculateDistance(List<PlaceDto> candidateCourse) {
        double totalDistance = 0.0;

        for (int i = 0; i < candidateCourse.size() - 1; i++) {
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

    // 최소 음주형 수 구하는 메소드(참고)
    private int getDrinkNum(int start, int end){
        int drinkTime=end-22;
        if(drinkTime>=0){
            if(drinkTime/3==0) return 1;
            else return drinkTime/3;
        }else{
            return 0;
        }
    }
}
