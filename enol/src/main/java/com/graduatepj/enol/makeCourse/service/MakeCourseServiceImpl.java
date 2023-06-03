package com.graduatepj.enol.makeCourse.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduatepj.enol.makeCourse.dao.*;
import com.graduatepj.enol.makeCourse.dto.*;
import com.graduatepj.enol.makeCourse.vo.CategoryPurpose;
import com.graduatepj.enol.makeCourse.vo.CourseV2;
import com.graduatepj.enol.makeCourse.vo.Place;
import com.graduatepj.enol.makeCourse.vo.Restaurant;
import com.graduatepj.enol.member.dao.UserHistoryRepository;
import com.graduatepj.enol.member.dto.UserPreferenceDto;
import com.graduatepj.enol.member.service.MemberService;
import com.graduatepj.enol.member.vo.History;
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

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("makeCourseService")
@RequiredArgsConstructor
public class MakeCourseServiceImpl implements MakeCourseService {

    @Value("${kakao.rest.api.key}")
    private String apikey;

    private final CourseMemberRepository courseMemberRepository;
    private final PlaceRepository placeRepository;
    private final RestaurantRepository restaurantRepository;
    private static final int CATEGORY_NUM = 62;
    private static final double FILTER_RADIUS = 0.2;

    private final CategoryRepository categoryRepository;
    private final CourseV2Repository courseV2Repository;
    private final CategoryPurposeRepository categoryPurposeRepository;
    private final UserHistoryRepository userHistoryRepository;

    private final MemberService memberService;

    // 실제로 해야할 것
    @Override
    public CourseResponse MakeCourse(CourseRequest courseRequest) {
        CourseDto firstCourse = firstCourseFiltering(courseRequest);
        SecondCourse secondCourse = secondCourseFiltering(firstCourse);
        CourseResponse finalCourse = finalCourseFiltering(secondCourse);
        return finalCourse;
    }

    @Override
    public CourseDto firstCourseFiltering(CourseRequest courseRequest) { // course_v2 사용
        // C열 카테고리로 이루어진 코스 생성
        log.info("--- firstCourseFiltering Start!!! ---");

        /** 인자값이 잘 들어왔는지 확인하는 로그 시작 */
        log.info("--- Show courseRequest ---");
        log.info("courseRequest.getUserCode = {}", courseRequest.getUserCode());
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

        /** Member List 가져와서 평균 내기 */ // member 테이블 나온 user대로 수정
        // 멤버 데이터 가져오기 - 약속에 함께하는 멤버 리스트 생성
        // member DB 나오는대로 수정해야 할수도 - ID가 아니라 코드가 pk이면 pk로 find해야 하므로 - 일단 놔둠
        /** 이 부분 user로 맞춰서 수정하기 */
        /** 이 부분 user로 맞춰서 수정하기 */
        
        // user 바뀐 테이블대로 속성값 가져와서 평균 객체 만드는 부분 만들어야함

        // userPreferenceDto에서 속성 값 가져오기
        List<UserPreferenceDto> userPreferenceList = new ArrayList<>();

        userPreferenceList.add(memberService.getPreferencesById(courseRequest.getUserCode()));
        for(int i=0; i<courseRequest.getMemberIdList().size(); i++) {
            userPreferenceList.add(memberService.getPreferencesById(courseRequest.getMemberIdList().get(i)));
        }

        // 로그로 멤버 리스트 확인
        log.info("--- userPreferenceList Start! --- ");
        log.info("userPreferenceList.size = {}", userPreferenceList.size());
        for(UserPreferenceDto userPreferenceDto : userPreferenceList) {
            log.info(" --- userPreferenceDto List =  --- ");
            log.info("userPreferenceDto.getPreferFatigue = {}", userPreferenceDto.getPrefFatigue());
            log.info("userPreferenceDto.getPreferSpecificity = {}", userPreferenceDto.getPrefSpecificity());
            log.info("userPreferenceDto.getPreferActivity = {}", userPreferenceDto.getPrefActivity());
            log.info(" --------------------------------- ");
        }
        log.info("--- userPreferenceList END! --- ");

        UserPreferenceDto avgUserPreference = new UserPreferenceDto();
        if (!userPreferenceList.isEmpty()) {
            avgUserPreference.setPrefFatigue(userPreferenceList.stream().mapToDouble(UserPreferenceDto::getPrefFatigue).sum() / userPreferenceList.size());
            avgUserPreference.setPrefSpecificity(userPreferenceList.stream().mapToDouble(UserPreferenceDto::getPrefSpecificity).sum() / userPreferenceList.size());
            avgUserPreference.setPrefActivity(userPreferenceList.stream().mapToDouble(UserPreferenceDto::getPrefActivity).sum() / userPreferenceList.size());
        }

        // 로그로 멤버들의 특성치 평균 확인
        log.info("--- Show avgUserPreference feature Start!!! --- ");
        log.info("avgUserPreference.getPreferFatigue() = {}", avgUserPreference.getPrefFatigue());
        log.info("avgUserPreference.getPreferSpecificity() = {}", avgUserPreference.getPrefSpecificity());
        log.info("avgUserPreference.getPreferActivity() = {}", avgUserPreference.getPrefActivity());
        log.info("--- Show avgUserPreference feature END!!! --- ");
        /** avgUserPreference List 가져와서 평균 내기 끝 */
        /** 이 부분 user로 맞춰서 수정하기 끝 */


//        List<MemberDto> memberList = courseMemberRepository
//                .findAllByIdIn(courseRequest.getMemberIdList())
//                .stream()
//                .map(member -> (MemberDto.fromEntity(member)))
//                .collect(Collectors.toList());
//
//        // 로그로 멤버 리스트 확인
//        log.info("--- memberList Start! --- ");
//        for(MemberDto memberDto : memberList) {
//            log.info("Member List = ");
//            log.info("memberDto.getMemberId = {}", memberDto.getMemberId());
//            log.info("memberDto.getMemberName = {}", memberDto.getMemberName());
//            log.info("memberDto.getBirthday = {}", memberDto.getBirthday());
//            log.info("memberDto.getGender = {}", memberDto.getGender());
//            log.info("memberDto.getFatigability = {}", memberDto.getFatigability());
//            log.info("memberDto.getSpecification = {}", memberDto.getSpecification());
//            log.info("memberDto.getActivity = {}", memberDto.getActivity());
//        }
//        log.info("--- memberList END! --- ");
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
//        log.info("--- Show avgMember feature Start!!! --- ");
//        log.info("avgMember.getFatigability() = {}", avgMember.getFatigability());
//        log.info("avgMember.getActivity() = {}", avgMember.getActivity());
//        log.info("avgMember.getSpecification() = {}", avgMember.getSpecification());
//        log.info("--- Show avgMember feature END!!! --- ");
//        /** Member List 가져와서 평균 내기 끝 */


        /** 시간, 목적, 키워드로 필터링한 CourseV2를 DB에서 가져오기 */
        // 시간에 따라 코스에 속할 카테고리 개수 정하기 - 최대 main 4개나 sub 4개
        /** 시간에 따라 새벽 음주 여부 체크하는 부분 */
        boolean dawnDrink = false; // 새벽이면 음주 추천하기 위해 - 기본 false
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

        if(courseRequest.getStartTime() <= 22 && courseRequest.getFinishTime() >= 22) {
            dawnDrink = true;
        }

        int totalTime = courseRequest.getFinishTime() - courseRequest.getStartTime(); // 총 소요 시간

        List<CourseV2> filteringCourse = new ArrayList<>(); // filtering된 코스들을 모아놓을 CourseV2 리스트
        List<Integer> timeRemoveIdx = new ArrayList<>(); // 시간에 해당하지 않는 코스 제거하기 위한 인덱스 리스트

        // 키워드와 목적으로 먼저 필터링하고
        // 그 다음에 시간 오차범위 3시간으로 해서 필터링 추가하는 식으로 변경하기
//        filteringCourse = courseV2Repository.findCourseByGoalsAndKeywords(keywordAbove.get(0), keywordBelow.get(0), keywordAbove.get(1), keywordBelow.get(1), keywordAbove.get(2), keywordBelow.get(2),
//                courseRequest.getGoals().get(0), courseRequest.getGoals().get(1), courseRequest.getGoals().get(2), courseRequest.getGoals().get(3), courseRequest.getGoals().get(4), courseRequest.getGoals().get(5), courseRequest.getGoals().get(6),
//                courseRequest.getGoals().get(7), courseRequest.getGoals().get(8), courseRequest.getGoals().get(9));

        filteringCourse = courseV2Repository.findCourseByGoalsAndKeywords(courseRequest.getGoals().get(0), courseRequest.getGoals().get(1), courseRequest.getGoals().get(2), courseRequest.getGoals().get(3), courseRequest.getGoals().get(4),
                courseRequest.getGoals().get(5), courseRequest.getGoals().get(6), courseRequest.getGoals().get(7), courseRequest.getGoals().get(8), courseRequest.getGoals().get(9));

        log.info("--- Before time filtering filteringCourse.size = {} --- ", filteringCourse.size());

        /** 음주의 개수에 따라 코스 구성 다르게 하기 */
        int drinkNum = getDrinkNum(courseRequest.getStartTime(), courseRequest.getFinishTime()); // 음주의 개수

        /** 음주의 개수가 0이면 기존 필터링대로  */
        if(drinkNum <= 0) {
            drinkFiltering(filteringCourse, drinkNum);
        }
        /** 음주 개수가 N개인 경우 */ // 최소 N개의 음주를 포함해야 함
        else if (drinkNum == 1) {
            drinkFiltering(filteringCourse, drinkNum);
            // 최대 drink 1*3 + main 3*3=12 / 최소 drink1*3+sub3*1 = 6 - 원래 시간대로 되도록
        } else if (drinkNum == 2) {
            drinkFiltering(filteringCourse, drinkNum);
            totalTime = 9; // 최대 drink 2*3+main2*3 = 12 / 최소 drink 2*3 =6
        } else if (drinkNum == 3) {
            drinkFiltering(filteringCourse, drinkNum);
            totalTime = 10; // 최대 drink 3개*3시간+main 3시간 1개 = 12시간 / 최소 drink 3개*3시간 9시간
        } else { // DrinkNum==4
            drinkFiltering(filteringCourse, drinkNum);
            totalTime = 12; // drink만 4개*3시간=12시간
        }

        if(dawnDrink == false && totalTime <= 12) {
            log.info("--- dawnDrink = {}, totalTime = {} ---", dawnDrink, totalTime);
            for(int i = 0; i < filteringCourse.size(); i++) {
                log.info("filteringCourse.get({}) = {}, filteringCourse.get({}).getTime() = {}, range = 3", i, filteringCourse.get(i).getId(), i , filteringCourse.get(i).getTime());
                if( ( (totalTime - 3) <= filteringCourse.get(i).getTime() ) && ( filteringCourse.get(i).getTime() <= (totalTime + 3) ) ) { // 1시간 선택에 필수 카테고리 있고 목적이 필수 카테고리에 포함되지 않으면 하나도 필터링되지 않게 되므로 메인인 3시간을 더하여 목적에 맞는 카테고리 1개 이상은 들어가도록 하기 위해 3시간을 오차범위로 설정
                    continue; // 패스
                }
                else { // 시간 오차 범위를 벗어나면 제거
                    timeRemoveIdx.add(i);
                }
            }

            log.info("timeRemoveIndex.size() = {}", timeRemoveIdx.size());
            for(int i = timeRemoveIdx.size() - 1; i >= 0; i--) {
                if(timeRemoveIdx.size()>0) {
                    filteringCourse.remove((int)timeRemoveIdx.get(i)); // 뒤에서부터 제거
                }
            }
        }
        else if (dawnDrink == false && totalTime>12) {
            log.info("--- dawnDrink = {}, totalTime = {} ---", dawnDrink, totalTime);
            for(int i = 0; i < filteringCourse.size(); i++) {
                log.info("filteringCourse.get({}) = {}, filteringCourse.get({}).getTime() = {}, 10 <= time", i, filteringCourse.get(i).getId(), i , filteringCourse.get(i).getTime());
                if( 10 <= filteringCourse.get(i).getTime() ) { // 필수 장소가 서브인 경우가 있을 수 있으므로 최소 10시간부터로 확인
                    continue; // 패스
                }
                else { // 시간 오차 범위를 벗어나면 제거
                    timeRemoveIdx.add(i);
                }
            }

            log.info("timeRemoveIndex.size() = {}", timeRemoveIdx.size());
            for(int i = timeRemoveIdx.size() - 1; i >= 0; i--) {
                if(timeRemoveIdx.size()>0) {
                    filteringCourse.remove((int)timeRemoveIdx.get(i)); // 뒤에서부터 제거

                }
            }
        }
        else if(dawnDrink==true && totalTime<=12) { // 시간 오차범위 3시간으로 늘림
            log.info("--- dawnDrink = {}, totalTime = {} ---", dawnDrink, totalTime);
            for(int i = 0; i < filteringCourse.size(); i++) {
                log.info("filteringCourse.get({}) = {}, filteringCourse.get({}).getTime() = {}, range = 3", i, filteringCourse.get(i).getId(), i , filteringCourse.get(i).getTime());
                if( ( (totalTime - 3) <= filteringCourse.get(i).getTime() ) && ( filteringCourse.get(i).getTime() <= (totalTime + 3) ) ) { // 시간 오차 범위 3시간 안에 들면 - 12시간인데 서브를 필수 장소로 선택하면 맥시멈 10시간이므로
                    continue; // 패스
                }
                else { // 시간 오차 범위를 벗어나면 제거
                    timeRemoveIdx.add(i);
                }
            }

            log.info("timeRemoveIndex.size() = {}", timeRemoveIdx.size());
            for(int i = timeRemoveIdx.size() - 1; i >= 0; i--) {
                if(timeRemoveIdx.size()>0) {
                    filteringCourse.remove((int)timeRemoveIdx.get(i)); // 뒤에서부터 제거

                }
            }
        }
        else if(dawnDrink = true && totalTime > 12) { // 술만 4개 나와야 함
            log.info("--- dawnDrink = {}, totalTime = {} ---", dawnDrink, totalTime);
            for(int i = 0; i < filteringCourse.size(); i++) {;
                log.info("filteringCourse.get({}) = {}, filteringCourse.get({}).getTime() = {}, 10 <= time", i, filteringCourse.get(i).getId(), i , filteringCourse.get(i).getTime());
//                if( 10 <= filteringCourse.get(i).getTime() ) { // 필수 장소가 서브인 경우가 있을 수 있으므로 최소 10시간부터로 확인
                if( filteringCourse.get(i).getTime() == 4 ) { // 필수 장소가 서브인 경우가 있을 수 있으므로 최소 10시간부터로 확인
                    continue; // 패스
                }
                else { // 시간 오차 범위를 벗어나면 제거
                    timeRemoveIdx.add(i);
                }
            }

            log.info("timeRemoveIndex.size() = {}", timeRemoveIdx.size());
            for(int i = timeRemoveIdx.size() - 1; i >= 0; i--) {
                if(timeRemoveIdx.size()>0) {
                    filteringCourse.remove((int)timeRemoveIdx.get(i)); // 뒤에서부터 제거
                }
            }
        }

        // 시간, 음주 여부로 필터링 후에 다시 확인
        log.info("");
        log.info("--- After Time AND Drink Filtering filteringCourse.size = {} ---", filteringCourse.size());
        log.info("");

        /** DB에서 CourseV2 가져오기 끝 */

        Map<String, List<Integer>> goalMap = new HashMap<>(); // selectedCourse에 set하기 위해 만든 맵

        /** 필수 장소가 코스에 포함되게 필터링 */
        log.info("--- wantedCategoryGroup Filtering! Start ---");
        log.info("wantedCategoryGroup = {}", courseRequest.getWantedCategoryGroup());
        log.info("wantedCategory = {}", courseRequest.getWantedCategory());
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
                    removeFilteringIdx.add(i); // 현재 filertingCourse에 필수 장소가 포함되지 않으면 나중에 한꺼번에 지우기 위해서 list에 i를 추가
                }
        }

        for(int i = removeFilteringIdx.size() - 1; i >= 0; i--) { // 뒤에서부터 removeIdx로 넣어놓은 제거할 것들을 제거
            if(removeFilteringIdx.size()>0) {
                filteringCourse.remove((int)removeFilteringIdx.get(i));
            }
        }
        log.info("--- wantedCategoryGroup Filtering! END ---");
        /** 필수 장소가 코스에 포함되게 필터링 끝 */

        /** 시간, 목적, 키워드로 필터링한 코스 filteredCourse에 추가 시작 */
        log.info("filteredCourse.size = {}", filteringCourse.size()); // 필터링된 코스 개수
        for(int i = 0; i < filteringCourse.size(); i++) {
            filteredCourse.add(CourseDto.fromEntity(filteringCourse.get(i))); // CourseV2를 CourseDTO로 변환하는 코드
        }
        /** 시간, 목적, 키워드로 필터링한 코스 filteredCourse에 추가 끝 */

        /** 코사인 유사도 평가로 1개만 고르기 */
        // 코사인 유사도 평가로 뽑는 방법(1)
        log.info("--- computeSimilarity Start! ---");
        List<CourseDto> courseAfterSimilarity = computeSimilarity(filteredCourse, avgUserPreference, totalTime); // 평균 멤버로 코사인유사도 평가
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
            List<String> categories = categoryRepository.findCategoryNameByCategoryGroupCode(selectedCourse.getCategoryGroupCode1()); // 해당 categoryGroup에 속하는 category들을 꺼내온 리스트
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
        log.info("--- cosine Similarity selectedCourse START ---");
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
        log.info("--- cosine Similarity selectedCourse End ---");
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

        // selectedCourseGroups - 최종 코스 틀에서 categoryGroup을 갖는 리스트
        List<CategoryPurpose> goalCategoryList = new ArrayList<>();
        for(String courseCategoryGroup : selectedCourseGroups) {
            goalCategoryList.clear();
            // 사용자가 입력한 목적 중 하나라도 1이고 카테고리 그룹 코드가 해당하는 것을 골라야 하
            goalCategoryList = categoryPurposeRepository.findByCategoryGroupCode(courseCategoryGroup, courseRequest.getGoals().get(0), courseRequest.getGoals().get(1),courseRequest.getGoals().get(2),courseRequest.getGoals().get(3),courseRequest.getGoals().get(4),courseRequest.getGoals().get(5),courseRequest.getGoals().get(6),courseRequest.getGoals().get(7),courseRequest.getGoals().get(8),courseRequest.getGoals().get(9)); // 목적이 하나라도 1이고 Course_Category_Group이 courseCategoryGroup인 categoryPurposeList

            log.info("--- courseCategoryGroup = {}, goalCategoryList.size() = {} ---", courseCategoryGroup, goalCategoryList.size());

            List<List<Integer>> goalList = new ArrayList<>(); // goalCategoryList로 나온 것들에서 목적만 뽑아낼 것

            log.info("--- goalCategoryList.size = {} ---", goalCategoryList.size());
            for(int i=0; i< goalCategoryList.size(); i++) { // 해당하는 카테고리그룹 에 해당하는 목적들을 저장 - categoryPurpose
                List<Integer> goalListI = new ArrayList<>(); // i번째에 해당하는 goalList들을 저장할 리스트

                goalListI.add(goalCategoryList.get(i).getWalk());
                goalListI.add(goalCategoryList.get(i).getSocializing());
                goalListI.add(goalCategoryList.get(i).getNiceAtmosphere());
                goalListI.add(goalCategoryList.get(i).getHealing());
                goalListI.add(goalCategoryList.get(i).getDrinking());
                goalListI.add(goalCategoryList.get(i).getUnusual());
                goalListI.add(goalCategoryList.get(i).getActive());
                goalListI.add(goalCategoryList.get(i).getDaily());
                goalListI.add(goalCategoryList.get(i).getSummer());
                goalListI.add(goalCategoryList.get(i).getCulturalLife());

                goalList.add(goalListI); // 목적들 리스트를 갖는 리스트를 만드는 것
            }

            for(int i=0; i<goalList.size(); i++) {
                log.info("goalList.get({}) = {}", i, goalList.get(i));
            }

            int[] matching = new int[10]; // 목적 리스트 0,1로 구성
            List<Integer> matched = new ArrayList<>();
            haveGoal = false;
            for(int i=0; i<10; i++)
                matching[i] = 0; // 반복될 때마다 초기화

            matched.clear();
            log.info("goalCategoryList.size = {}", goalCategoryList.size());
            log.info("courseRequest.getGoals() = {}", courseRequest.getGoals());

            for(int i=0; i<goalCategoryList.size(); i++) { // categoryGroupCode에 모든 category에 해당하는 categoryPurpose
                for(int j=0; j<goalList.get(i).size(); j++) { // goalCategoryList에 속하는 10개 목적 값 리스트
                    if( (courseRequest.getGoals().get(j) == 1) && (goalList.get(i).get(j) == 1) ) { // request로 받은 목적 리스트에서 목적이 참이고 해당 courseCategoryGroup의 목적도 1인 경우 추가
                        matching[j]=1;
                        haveGoal = true;
                    }
                }
            }
            if(haveGoal) {
                log.info("--- matching = [{}, {}, {}, {}, {}, {}, {}, {}, {}, {}]", matching[0], matching[1], matching[2], matching[3], matching[4], matching[5], matching[6], matching[7], matching[8], matching[9]);
                for(int k=0; k<10; k++) {
                    matched.add(matching[k]);
                }
                goalMap.put(courseCategoryGroup, matched); // 목적이 하나라도 있으면 맵에 저장
            }
            log.info("--- goalMap.keySet = {} ---", goalMap.keySet());
            log.info("--- goalMap.values = {} ---", goalMap.values());
        }
        selectedCourse.setGoalMatch(goalMap); // goalMatch에 저장 - 1개 이상의 목적이 있기 때문에 무조건 1개 이상의 goalMap의 원소가 존재할 것
        log.info("--- selectedCourse.getGoalMatch.size = {} ---", selectedCourse.getGoalMatch().size());
        log.info("--- selectedCourse.getGoalMatch = {} ---", selectedCourse.getGoalMatch());
        if(dawnDrink) selectedCourse.setDawnDrink(true); // 새벽 음주 courseDto에 체크하도록
        log.info("selectedCourse.isDawnDrink = {}", selectedCourse.isDawnDrink());
        if(courseRequest.isMealCheck()) selectedCourse.setMealCheck(courseRequest.isMealCheck());
        log.info("selectedCourse.isMealCheck = {}", selectedCourse.isMealCheck());
        selectedCourse.setStartTime(courseRequest.getStartTime());
        selectedCourse.setFinishTime(courseRequest.getFinishTime());
        log.info("selectedCourse.startTime = {}", selectedCourse.getStartTime());
        log.info("selectedCourse.finishTime = {}", selectedCourse.getFinishTime());
        return selectedCourse; // 다 거르고 최종 필터링된 selectedCourse 하나만 반환
    }

    private static void drinkFiltering(List<CourseV2> filteringCourse, int drinkNum) {
        log.info("--- drinkFiltering START! ---");
        log.info("drinkNum = {}", drinkNum);
        log.info("Before drinkFiltering.size() = {}", filteringCourse.size());
        // drinkNum이 0이면 할 필요 없으므로 바로 리턴
        if(drinkNum == 0) return;

        int drinkCount = 0;
        List<Integer> removeNDrinkIdx = new ArrayList<>();

        List<String> courseV2List = new ArrayList<>(); // i번 인덱스에 해당하는 categoryGroupCode들을 리스트로 가져오기 - ex) 놀기Main 스포츠, 관게Main 휴식, ..
        for(int i = 0; i< filteringCourse.size(); i++) {
            drinkCount=0;
            courseV2List.add(filteringCourse.get(i).getCategoryGroupCode1()); // 1번은 무조건 값이 있으므로 그냥 넣음
            if(filteringCourse.get(i).getCategoryGroupCode2() != null)
                courseV2List.add(filteringCourse.get(i).getCategoryGroupCode2()); // null이 아닌 경우에만 추가
            if(filteringCourse.get(i).getCategoryGroupCode3() != null)
                courseV2List.add(filteringCourse.get(i).getCategoryGroupCode3()); // null이 아닌 경우에만 추가
            if(filteringCourse.get(i).getCategoryGroupCode4() != null)
                courseV2List.add(filteringCourse.get(i).getCategoryGroupCode4()); // null이 아닌 경우에만 추가

            if(filteringCourse.get(i).getCategoryGroupCode1().equals("RS2")) { // 현재 RS2가 음주 카테고리 그룹
                drinkCount++;
            }
            if( ( filteringCourse.get(i).getCategoryGroupCode2() != null ) && ( filteringCourse.get(i).getCategoryGroupCode2().equals("RS2") ) ) {
                drinkCount++;
            }
            if( ( filteringCourse.get(i).getCategoryGroupCode3() != null ) && ( filteringCourse.get(i).getCategoryGroupCode3().equals("RS2") ) ) {
                drinkCount++;
            }
            if( ( filteringCourse.get(i).getCategoryGroupCode4() != null ) && ( filteringCourse.get(i).getCategoryGroupCode4().equals("RS2") ) ) {
                drinkCount++;
            }

            // drink의 개수가 drinkNum보다 작으면 제거 리스트에 추가
            if(drinkCount < drinkNum) {
                removeNDrinkIdx.add(i);
            }

        }
        for(int i = removeNDrinkIdx.size() - 1; i >= 0; i--) {
            if(removeNDrinkIdx.size()>0) {
                filteringCourse.remove((int)removeNDrinkIdx.get(i)); // 뒤에서부터 제거
            }
        }
        log.info("After drinkFiltering.size() = {}", filteringCourse.size());
        log.info("--- drinkFiltering END! ---");
    }


    // 세부 카테고리를 결정
    // 일단 죄다 랜덤으로 뽑긴 했는데 세부적으로 뭐 기타특징 같은거 고려해야함.
    @Override
    public SecondCourse secondCourseFiltering(CourseDto firstCourse) {
        log.info("--- SecondCourseFiltering START!! ---");
        List<String> course = new ArrayList<>(); // 코스 가져오기 - ex) 놀기Main 스포츠, 관게Main 휴식, ..
        course.add(firstCourse.getCategoryGroupCode1()); // 1번은 무조건 값이 있으므로 그냥 넣음
        if(firstCourse.getCategoryGroupCode2() != null)
            course.add(firstCourse.getCategoryGroupCode2()); // null이 아닌 경우에만 추가
        if(firstCourse.getCategoryGroupCode3() != null)
            course.add(firstCourse.getCategoryGroupCode3()); // null이 아닌 경우에만 추가
        if(firstCourse.getCategoryGroupCode4() != null)
            course.add(firstCourse.getCategoryGroupCode4()); // null이 아닌 경우에만 추가

        SecondCourse secondCourse = new SecondCourse(firstCourse, course); // firstCourse로 나온 CourseDto로 SecondCoures의 CourseDto selectedCourse 채움
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
        log.info("secondCourse.getMealCheck = {}", secondCourse.getMealCheck());
        log.info("secondCourse.getDawnDrink = {}", secondCourse.getDawnDrink());
        log.info("secondCourse.getCategoryGroupCodes = {}", secondCourse.getCategoryGroupCodes());
        log.info("second.getStartTime = {}", secondCourse.getStartTime());
        log.info("second.getEndTime = {}", secondCourse.getEndTime());

        Random rand = new Random();
        List<String> detailCategories = new ArrayList<>(); // 확정된 d열 카테고리 리스트
        List<Integer> categoryGroupCodeGoalMatch = new ArrayList<>(); // goalMatch에서 꺼낸 목적 리스트 담고
//         리스트에서 무작위로 하나의 요소 선택
        for (String s : secondCourse.getCategoryGroupCodes()) { // course는 CategoryGroupCode1,2,3,4를 가지는 것

            List<String> categories = categoryRepository.findCategoryNameByCategoryGroupCode(s); // 해당 categoryGroup에 속하는 category들을 꺼내온 리스트
            String randomCategory = categories.get(rand.nextInt(categories.size())); // category들 리스트에서 랜덤으로 하나 뽑아옴 - D열

            // s가 goalMatch의 Category이면 목적리스트 확인해서 DB 접근해서 어떤 Category가 해당 목적을 가지고 있는지 확인해서 List에 담기 - 여러 개 일 수 있으므로
            categoryGroupCodeGoalMatch = firstCourse.getGoalMatch().get(s); // C열은 중복되는거 없으니까 map의 get으로 불러와도 됨
            log.info("--- secondCourse.getCategoryGroupCode = {} ---", s);
            log.info("categoryGroupCodeGoalMatch = {}", categoryGroupCodeGoalMatch);
            // goalMatch에서의 categoryGroup에 속하고 목적10개의 값 중 하나라도 같은 categoryCode들을 모아놓은 list - 없으면 에러

            if (firstCourse.getWantedCategoryGroup().equals(s)) { // 필수 카테고리 그룹을 course에서 찾았으면
                detailCategories.add(0, firstCourse.getWantedCategory()); // 필수 카테고리 그룹이 s이면 필수 카테고리를 detailCategories에 넣어줌
                secondCourse.setWantedCategoryName(firstCourse.getWantedCategory()); // wantedCategory(D열)을 채워줌
                continue;
            }

            log.info("firstCourse.getGoalMatch().get({}) = {}", s, firstCourse.getGoalMatch().get(s));
            if(firstCourse.getGoalMatch().get(s) == null) { // 사용자가 입력한 목적에 해당하는 코스의 목적이 없는 경우
                if(!firstCourse.getWantedCategoryGroup().equals(s)) { // 필수 카테고리가 아닌 경우
                    detailCategories.add(randomCategory);
                }
            }
            else {
                List<String> goalMatchingCategoryCodes = categoryPurposeRepository.findCategoryCodeByCategoryGroupCodeAndGoals(s, categoryGroupCodeGoalMatch.get(0), categoryGroupCodeGoalMatch.get(1), categoryGroupCodeGoalMatch.get(2), categoryGroupCodeGoalMatch.get(3), categoryGroupCodeGoalMatch.get(4), categoryGroupCodeGoalMatch.get(5), categoryGroupCodeGoalMatch.get(6), categoryGroupCodeGoalMatch.get(7), categoryGroupCodeGoalMatch.get(8), categoryGroupCodeGoalMatch.get(9));
                log.info("--- secondCourse.getCategoryGroupCode = {} ---", s);
                log.info("categoryGroupCodeGoalMatch = {}", categoryGroupCodeGoalMatch);
                log.info("goalMatchingCategoryCodes = {}", goalMatchingCategoryCodes);
                if (goalMatchingCategoryCodes.size() == 1) { // 1개라면 이거 넣고
                    detailCategories.add(goalMatchingCategoryCodes.get(0));
                } else if (goalMatchingCategoryCodes.size() >= 2) { // 여러개라면
                    detailCategories.add(goalMatchingCategoryCodes.get(rand.nextInt(goalMatchingCategoryCodes.size()))); // 그 중에서 랜덤으로
                }
            }
        }

        for(int i=0; i<detailCategories.size(); i++) {
            log.info("detailCategories.get({}) = {}", i, detailCategories.get(i));
        }
        secondCourse.setDetailCategoryNames(detailCategories); // secondCourse에 확정된 코스 넣는 부분

        return secondCourse;
    }

    // 실제 가게들 선택 및 최적화 알고리즘 적용
    @Override
    public CourseResponse finalCourseFiltering(SecondCourse secondCourse) {
        int num = 0;
        boolean[] meal= new boolean[]{false, false, false, false};
        int[] partition;
        CourseResponse finalCourse;
        Double[] mainCoordinate = new Double[]{0.0, 0.0};
        List<PlaceDto> restaurants = new ArrayList<>();
        // wantedCategory 데이터를 전부 별점순으로 가져오기(내림차순)
        List<PlaceDto> wantedCategoryPlaceList = getCategoryPlaceList(secondCourse.getWantedCategoryName());
//        log.info("wantedCategory 개수 : " + wantedCategoryPlaceList.size());
        // 카테고리를 돌려보면서 하나하나 반경 기준으로 가져오기.
        List<PlaceDto> wantedCourse = getFinalCourse(secondCourse, wantedCategoryPlaceList, 1, mainCoordinate);
//        log.info("선정된 wantedCourse 있는지 확인(숫자) : " + (wantedCourse!=null?wantedCourse.size():"null"));
        // 만약 결과가 없다면 두번째 우선순위 카테고리 데이터를 전부 별점순으로 가져오기(내림차순)
        if(secondCourse.getDetailCategoryNames().size()>1){
            if (wantedCourse == null || wantedCourse.isEmpty()) {
//                log.info("주 카테고리를 기준으로 결과가 나오지 않았다. 차선책 진행");
                wantedCategoryPlaceList = getCategoryPlaceList(secondCourse.getDetailCategoryNames().get(1));
                wantedCourse = getFinalCourse(secondCourse, wantedCategoryPlaceList, 2, mainCoordinate);
                // 가장 가까운 wantedCategory를 코스에 포함
                wantedCourse.add(PlaceDto.
                        fromEntity(placeRepository
                                .findNearestPlaceByCategoryNameAndLocation(
                                        secondCourse.getWantedCategoryName(),
                                        mainCoordinate[0],
                                        mainCoordinate[1])));
//                log.info("선정된 wantedCourse 있는지 확인(숫자) : " + wantedCourse.size());
                // 이것마저 없을 경우 에러를 반환
                if (wantedCourse == null || wantedCourse.isEmpty()){
                    log.warn("이것마저 없을 경우 에러를 반환.(일단 null 반환) -> 못찾음");
                    //                throw new RuntimeException("not enough data so no recommendation!");
                    return null;
                }


            }
        }
        if(wantedCourse==null){
            log.warn("이것마저 없을 경우 에러를 반환.(일단 null 반환) -> 이건 왜 없지?");
            return null;
        }
        // 아침 : 8시~10시 점심 : 12시~2시 저녁 : 6시~8시에 노는시간이 걸쳐있을 경우, 식사 포함. 안걸쳐있으면 그냥 하나만 추가.
        if (secondCourse.getMealCheck()) {
            meal = getRestaurantNum(secondCourse.getStartTime(), secondCourse.getEndTime());
            for (int i = 0; i < meal.length; i++) if (meal[i]) num++;
//            log.info("breakfast = " + meal[0] + " | lunch = " + meal[1] + " | dinner = " + meal[2] + " | else = " + meal[3]);
            restaurants = getRestaurantFromDB(mainCoordinate, num);
//            restaurants = getRestaurantFromApi(mainCoordinate, num);
//            log.info("실제로 가져온 음식점 수 : " + restaurants.size());
        }
        // 최적화 알고리즘 적용(순서 결정)
        // 식사 앞뒤로 몇개의 가게 들어갈지 구하는 메소드
        partition = getPartition(wantedCourse, secondCourse.getStartTime(), secondCourse.getEndTime());
        // 최적화(노가다 순열)
        finalCourse = optimizeCourse(wantedCourse, restaurants, partition, meal, secondCourse.getStartTime());
        // 히스토리에 추가
        saveHistory(finalCourse, secondCourse.getCourseId(), secondCourse.getUserCode());
        return finalCourse;
    }

    private Void saveHistory(CourseResponse finalCourse, String courseId, String userCode){
        History history = userHistoryRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("해당 유저의 히스토리 정보를 찾을 수 없습니다."));
        List<Long> placeIds = new ArrayList<>();
        for(PlaceDto place : finalCourse.getPlaceDto()){
            placeIds.add((long) place.getId());
        }
        history.setNumber(history.getNumber()+1);
        history.getCourse().add(new History.HistoryCourse(courseId, placeIds, 0.0));
        userHistoryRepository.save(history);
        return null;
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

    /** user 버전 */
    public List<CourseDto> computeSimilarity(List<CourseDto> filteredCourse, UserPreferenceDto avgUserPreference, int time) {
        List<CourseDto> similarCourses = new ArrayList<>();

        // 평균 값을 이용해 RealVector 생성
        RealVector avgVector = new ArrayRealVector(new double[]{
                avgUserPreference.getPrefFatigue(),
                avgUserPreference.getPrefSpecificity(),
                avgUserPreference.getPrefActivity(),
                (double)time
        });

        // L1-norm 가중치 계산
        for (CourseDto course : filteredCourse) { // 1차로 필터링된 코스들
            RealVector vector = new ArrayRealVector(new double[]{
                    course.getFatigability(),
                    course.getSpecification(),
                    course.getActivity(),
                    (double)course.getTime()
            });

            // L1-norm 값이 클수록 유사도가 낮은 것을 의미
            double l1Norm = vector.getL1Distance(avgVector);

            // 가중치를 곱해 L1-norm에 반영
            double weight;
            if (course.getRate() >= 5.0) {
                weight = 0.5;
            } else if (course.getRate() >= 4.0) {
                weight = 0.7;
            } else if (course.getRate() >= 3.0) {
                weight = 0.9;
            } else if (course.getRate() >= 2.5) {
                weight = 1.0;
            } else if (course.getRate() >= 2.0) {
                weight = 1.1;
            } else {
                weight = 1.3;
            }
            l1Norm *= weight;

            course.setSimilarity(l1Norm);
            similarCourses.add(course);
        }

        // L1-norm에 따라 정렬(오름차순)
        similarCourses.sort(Comparator.comparing(CourseDto::getSimilarity));

        return similarCourses;
    }


//    public List<CourseDto> computeSimilarity(List<CourseDto> filteredCourse, MemberDto avgMember, int time) {
//        List<CourseDto> similarCourses = new ArrayList<>();
//
//        // 평균 값을 이용해 RealVector 생성
//        RealVector avgVector = new ArrayRealVector(new double[]{
//                avgMember.getFatigability(),
//                avgMember.getSpecification(),
//                avgMember.getActivity(),
//                (double)time
//        });
//
//        // L1-norm 가중치 계산
//        for (CourseDto course : filteredCourse) { // 1차로 필터링된 코스들
//            RealVector vector = new ArrayRealVector(new double[]{
//                    course.getFatigability(),
//                    course.getSpecification(),
//                    course.getActivity(),
//                    (double)course.getTime()
//            });
//
//            // L1-norm 값이 클수록 유사도가 낮은 것을 의미
//            double l1Norm = vector.getL1Distance(avgVector);
//
//            // 가중치를 곱해 L1-norm에 반영
//            double weight;
//            if (course.getRate() >= 5.0) {
//                weight = 0.5;
//            } else if (course.getRate() >= 4.0) {
//                weight = 0.7;
//            } else if (course.getRate() >= 3.0) {
//                weight = 0.9;
//            } else if (course.getRate() >= 2.5) {
//                weight = 1.0;
//            } else if (course.getRate() >= 2.0) {
//                weight = 1.1;
//            } else {
//                weight = 1.3;
//            }
//            l1Norm *= weight;
//
//            course.setSimilarity(l1Norm);
//            similarCourses.add(course);
//        }
//
//        // L1-norm에 따라 정렬(오름차순)
//        similarCourses.sort(Comparator.comparing(CourseDto::getSimilarity));
//
//        return similarCourses;
//    }

    public List<CourseDto> computeSimilarity2(List<CourseDto> filteredCourse, MemberDto avgMember) {
        List<CourseDto> similarCourses = new ArrayList<>();

        // 평균 값을 이용해 RealVector 생성
        RealVector avgVector = new ArrayRealVector(new double[]{
                avgMember.getFatigability(),
                avgMember.getSpecification(),
                avgMember.getActivity(),
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

    private List<PlaceDto> getCategoryPlaceList(String categoryName) {
        return PlaceDto.fromEntityList(placeRepository.findAllByCategoryNameOrderByRatingDesc(categoryName));
    }

    private List<PlaceDto> getCategoryPlaceList2(String categoryCode) {
        List<PlaceDto> placeDtoList = placeRepository
                .findAllByCategoryName(categoryCode)
//                .findAllByCategoryInOrderByRateDesc(categoryCode)
                .stream()
                .map(PlaceDto::fromEntity)
                .collect(Collectors.toList());
        Collections.shuffle(placeDtoList);
        return placeDtoList;
    }

    private List<PlaceDto> getFinalCourse(SecondCourse secondCourse, List<PlaceDto> wantedCategoryPlaceList, int start, Double[] mainCoordinate) {
        List<PlaceDto> course = new ArrayList<>();
        Map<String, Integer> duplicatesCountMap = new HashMap<>();

        // 코드 : 중복 수의 킷값 쌍 만들기
        for (String code : secondCourse.getDetailCategoryNames()) {
            duplicatesCountMap.put(code, duplicatesCountMap.getOrDefault(code, 0) + 1);
        }

        for (PlaceDto s : wantedCategoryPlaceList) {
            course.clear();
            for (int i = start; i < duplicatesCountMap.size(); i++) {
                String categoryCode = secondCourse.getDetailCategoryNames().get(i);

                List<PlaceDto> places = PlaceDto.fromEntityList(
                                placeRepository.findHighestRatedPlaceByCategoryAndLocationAndRadius(
                                        categoryCode, s.getX(), s.getY(), FILTER_RADIUS, duplicatesCountMap.get(categoryCode)));

                // 만족하지 못했을 경우 다음 주 카테고리 가게로 이동, 만족했을 경우 가져온 모든것을 course에 넣는다.
                if (places == null || places.size()<duplicatesCountMap.get(categoryCode)) {
                    break;
                }else{
                    course.addAll(places);
                }

                // 마지막 것까지 성공했다면 해당 주 카테고리의 좌표를 중심값으로 설정하고 리턴
                if (i == duplicatesCountMap.size() - 1) {
                    mainCoordinate[0] = s.getX();
                    mainCoordinate[1] = s.getY();
                    System.out.println(s.getAddressName());
                    System.out.println(mainCoordinate[0].toString() + " " + mainCoordinate[1].toString());
                    return course;
                }
            }
            if(start==secondCourse.getDetailCategoryNames().size()){
                mainCoordinate[0] = s.getX();
                mainCoordinate[1] = s.getY();
                course.add(s);
                return course;
            }
        }
        return null;
    }

    private List<PlaceDto> getFinalCourse2(SecondCourse secondCourse, List<PlaceDto> wantedCategoryPlaceList, int start, Double[] mainCoordinate) {
        List<PlaceDto> course = new ArrayList<>();
        Set<PlaceDto> uniquePlaces = new HashSet<>(); // 중복 체크를 위한 HashSet
        for (PlaceDto s : wantedCategoryPlaceList) {
            course.clear();
            uniquePlaces.clear(); // 새로운 시작 위치마다 HashSet 초기화
            Loop1:
            for (int i = start; i < secondCourse.getDetailCategoryNames().size(); i++) {
                int maxRetry = 3; // 최대 재시도 횟수 설정
                int retryCount = 0; // 재시도 횟수 카운트

                // 별점순으로 할 경우, 중복 카테고리가 있을 때 처리가 필요
                while (retryCount < maxRetry) {
                    PlaceDto place = PlaceDto.fromEntity(
                            placeRepository.findRandomPlaceByCategoryNameAndLocationAndRadius(
                                    secondCourse.getDetailCategoryNames().get(i), s.getX(), s.getY(), FILTER_RADIUS));
                    if (place == null) {
                        break Loop1;
                    } else if (uniquePlaces.add(place)) {
                        course.add(place);
                        if (i == secondCourse.getDetailCategoryNames().size() - 1) {
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

    private List<PlaceDto> getRestaurantFromDB(Double[] mainCoordinate, int num) {
        int cnt = 0;
        System.out.println(mainCoordinate[0].toString() + " " + mainCoordinate[1].toString());
        List<PlaceDto> places = new ArrayList<>();
        List<Restaurant> restaurants = restaurantRepository.findRandomRestaurantByLocationAndRadius(mainCoordinate[0], mainCoordinate[1], FILTER_RADIUS);
        for(Restaurant restaurant : restaurants){
            PlaceDto place = PlaceDto.builder()
                    .placeName(restaurant.getPlaceName())
                    .categoryName(restaurant.getCategoryCode())
                    .addressName(restaurant.getAddressName())
                    .x(restaurant.getX())
                    .y(restaurant.getY())
                    .build();
            if (places.stream().anyMatch(p -> p.getCategoryName().equals(String.valueOf(restaurant.getCategoryCode()))))
                continue;
            places.add(place);
            cnt++;
            if (cnt >= num) {
                for (PlaceDto rest : places)
                    rest.setCategoryName(restaurant.getCategoryName());
                return places;
            }
        }
        return places;
    }

    private List<PlaceDto> getRestaurantFromApi(Double[] mainCoordinate, int num) {
        int cnt = 0;
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=FD6&x=" + mainCoordinate[0] + "&y=" + mainCoordinate[1] + "&radius=" + 200;
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
        List<PlaceDto> places = new ArrayList<>();
        for (JsonNode document : documents) {
            String placeName = document.path("place_name").asText();
            String categoryCode = document.path("category_name").asText();
            String addressName = document.path("address_name").asText();
            double x = Double.parseDouble(document.path("x").asText());
            double y = Double.parseDouble(document.path("y").asText());
            String phone = document.path("phone").asText();

            PlaceDto place = PlaceDto.builder()
                    .placeName(placeName)
                    .categoryName(categoryCode)
                    .addressName(addressName)
                    .x(x)
                    .y(y)
                    .phoneNumber(phone)
                    .build();
            if (places.stream().anyMatch(p -> p.getCategoryName().equals(String.valueOf(categoryCode))))
                continue;
            places.add(place);
            cnt++;
            if (cnt >= num) {
                for (PlaceDto restaurant : places)
                    restaurant.setCategoryName("음식점");
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
//        System.out.println("시간 : 아침 전 = "+ partTime[0] +" | 아침/점심 사이 = "+ partTime[1] +" | 점심/저녁 사이 = "+ partTime[2] + " | 저녁 이후 = " + partTime[3]);
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

    private CourseResponse optimizeCourse(List<PlaceDto> wantedCourse, List<PlaceDto> restaurants, int[] partition, boolean[] mealCheck, int startTime) {
        List<PlaceDto> drinkPlace = new ArrayList<>();
        List<PlaceDto> otherPlace = new ArrayList<>();
        List<PlaceDto> finalCourse = new ArrayList<>();
        List<PlaceDto> candidateCourse = new ArrayList<>();
        CourseResponse courseResponse = new CourseResponse();

        double minDistance = Double.MAX_VALUE;
        double calculateDistance = 0.0;

        List<List<PlaceDto>> drinkPermutations = new ArrayList<>();
        List<List<PlaceDto>> otherPermutations = new ArrayList<>();
        List<List<PlaceDto>> restaurantPermutations = new ArrayList<>();

        for (PlaceDto place : wantedCourse) {
            if (place.getCategoryName().equals("RS2")) {
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
                log.info("일반형만 있는 경우");
                for (List<PlaceDto> otherPerm : otherPermutations) {
                    candidateCourse.addAll(otherPerm);
                    calculateDistance = calculateDistance(candidateCourse);
                    if (minDistance > calculateDistance) {
                        minDistance = calculateDistance;
                        finalCourse = new ArrayList<>(candidateCourse);
                    }
                    candidateCourse.clear();
                }
            } else {
                // 일반형 + 식사체크
                log.info("일반형 + 식사체크");
                for (List<PlaceDto> otherPerm : otherPermutations) {
                    for (List<PlaceDto> restPerm : restaurantPermutations) {
                        candidateCourse = makeCandidateCourse(otherPerm, null, restPerm, partition, mealCheck, startTime);
//                        candidateCourse.addAll(otherPerm);
                        calculateDistance = calculateDistance(candidateCourse);
                        if (minDistance > calculateDistance) {
                            minDistance = calculateDistance;
                            finalCourse = new ArrayList<>(candidateCourse);
                        }
                    }
                }
            }
        } else if (otherPlace.isEmpty()) {
            if (restaurants.isEmpty()) {
                // 음주형만 있는 경우
                log.info("음주형만 있는 경우");
                for (List<PlaceDto> drinkPerm : drinkPermutations) {
                    candidateCourse.addAll(drinkPerm);
                    calculateDistance = calculateDistance(candidateCourse);
                    if (minDistance > calculateDistance) {
                        minDistance = calculateDistance;
                        finalCourse = new ArrayList<>(candidateCourse);
                    }
                    candidateCourse.clear();
                }
            } else {
                // 음주형 + 식사체크
                log.info("음주형 + 식사체크");
                for (List<PlaceDto> drinkPerm : drinkPermutations) {
                    for (List<PlaceDto> restPerm : restaurantPermutations) {
                        candidateCourse = makeCandidateCourse(null, drinkPerm, restPerm, partition, mealCheck, startTime);
                        calculateDistance = calculateDistance(candidateCourse);
                        if (minDistance > calculateDistance) {
                            minDistance = calculateDistance;
                            finalCourse = new ArrayList<>(candidateCourse);
                        }
                    }
                }
            }
        } else if (restaurants.isEmpty()) {
            // 일반형 + 음주형
            log.info("일반형 + 음주형");
            for (List<PlaceDto> otherPerm : otherPermutations) {
                for (List<PlaceDto> drinkPerm : drinkPermutations) {
                    candidateCourse.addAll(otherPerm);
                    candidateCourse.addAll(drinkPerm);
                    calculateDistance = calculateDistance(candidateCourse);
                    if (minDistance > calculateDistance) {
                        minDistance = calculateDistance;
                        finalCourse = new ArrayList<>(candidateCourse);
                    }
                    candidateCourse.clear();
                }
            }
        } else {
            // 일반형 + 음주형 + 식사체크
            log.info("일반형 + 음주형 + 식사체크");
            for (List<PlaceDto> otherPerm : otherPermutations) {
                for (List<PlaceDto> drinkPerm : drinkPermutations) {
                    for (List<PlaceDto> restPerm : restaurantPermutations) {
                        candidateCourse = makeCandidateCourse(otherPerm, drinkPerm, restPerm, partition, mealCheck, startTime);
                        calculateDistance = calculateDistance(candidateCourse);
                        if (minDistance > calculateDistance) {
                            minDistance = calculateDistance;
                            finalCourse = new ArrayList<>(candidateCourse);
                        }
                    }
                }
            }
        }
        System.out.print("결과물 -> ");
        for (int i = 0; i < finalCourse.size(); i++) {
            System.out.print(i + "번째 가게 : " + finalCourse.get(i).getPlaceName() + " 코드 : " + finalCourse.get(i).getCategoryName() + " | ");
        }
        System.out.println();
        courseResponse.setPlaceDto(finalCourse);

        return courseResponse;
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
