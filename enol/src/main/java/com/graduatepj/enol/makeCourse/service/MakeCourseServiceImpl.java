package com.graduatepj.enol.makeCourse.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduatepj.enol.makeCourse.dao.*;
import com.graduatepj.enol.makeCourse.dto.*;
import com.graduatepj.enol.makeCourse.type.CategoryPriority;
import com.graduatepj.enol.makeCourse.vo.Category;
import com.graduatepj.enol.makeCourse.vo.Course;
import com.graduatepj.enol.makeCourse.vo.DColCategory;
import com.graduatepj.enol.makeCourse.dto.PlaceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        // 인자값이 잘 들어왔는지 확인하는 로그
        log.info("courseRequest.getNumPeople = {}", courseRequest.getNumPeople());
        log.info("courseRequest.getMemberIdList = {}", courseRequest.getMemberIdList());
        log.info("courseRequest.isMealCheck = {}", courseRequest.isMealCheck());
        log.info("courseRequest.getStartTime = {}, FinishTime = {}", courseRequest.getStartTime(), courseRequest.getFinishTime());
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


        // 시간 제한으로 가능한 모든 코스 받아오기(시간은 24시간으로 받아야함) - 분 말고 몇시인지만 받을거임
        // 시간에 따라 코스에 속할 카테고리 개수 정하기 - 최대 main 4개나 sub 4개
        if(courseRequest.getStartTime()>=courseRequest.getFinishTime()){
            courseRequest.setFinishTime(24+courseRequest.getFinishTime());
        }
        int time = courseRequest.getFinishTime() - courseRequest.getStartTime(); // 약속 총 소요시간
        List<Course> timeCourse = courseRepository.findAllByTime(time); // 해당 time에 맞는 코스리스트 가져오기 - 시간으로 필터링
        List<CourseDto> filteredCourse = new ArrayList<>();
        for (Course courseInfo : timeCourse) { // 코스틀 전체에서 코스 하나하나 가져오기
            List<String> course = courseInfo.getCategories(); // 코스 가져오기 - ex) 놀기Main, 관게Main

            // 필수 장소 있을때 그게 들어가게 일단 필터링 한거 아님?
            if (courseRequest.getWantedCategory() != null) { // 가고싶은 카테고리 있으면
                if (!course.contains(courseRequest.getWantedCategory())) { // 필수장소(카테고리) 필터링
                    continue;
                }
            }
            // 여기까지 filteredCourse에 아무것도 없는거임
//            if (!firstCourseGoalFiltering(courseInfo, courseRequest, filteredCourse)) // 코스 목적으로 필터링
//                continue;

            firstCourseGoalFiltering(courseInfo, courseRequest, filteredCourse); // 코스 목적으로 필터링 - 안에서 filterCourse를 add하고 C열 카테고리에 목적을 map으로 가짐


            int N = 0; // 키워드 수치 비교에서 N을 어떻게 할지에 따라 결정

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
        selectedCourse.setWantedCategory(courseRequest.getWantedCategory()); // 필수 카테고리 저장

        // wantedCategory가 null인 경우 우선순위에 따른 주장소 선택(main)
        log.info("firstCourseFiltering Start!");
        if (selectedCourse.getWantedCategory() == null) { // 필수 카테고리 없는 경우 우선순위에 따라 코스의 주 카테고리 지정
            // 우선순위에 따른 주장소 선택
            for (CategoryPriority p : CategoryPriority.values()) {
                for (String category : selectedCourse.getCategories()) {
                    if (category.contains(p.getName())) { // 우선순위 순으로 해당 C열 카테고리를 가졌는지 확인
                        selectedCourse.setWantedCategory(p.getName()); // 가졌으면 wantedCategory로 지정하고 탈출
                        break;
                    }
                }
                if (selectedCourse.getWantedCategory() != null) { // ???
                    break;
                }
            }
        }
        log.info("selectedCourse = {}", selectedCourse);
        return selectedCourse; // 다 거르고 최종 필터링된 selectedCourse 하나만 반환
    }


    @Override
    public CourseDto firstCourseFiltering1(CourseRequest courseRequest) { // course_v2 사용
        // C열 카테고리로 이루어진 코스 생성
        log.info("firstCourseFiltering Start!");

        // 인자값이 잘 들어왔는지 확인하는 로그
        log.info("courseRequest.getNumPeople = {}", courseRequest.getNumPeople());
        log.info("courseRequest.getMemberIdList = {}", courseRequest.getMemberIdList());
        log.info("courseRequest.isMealCheck = {}", courseRequest.isMealCheck());
        log.info("courseRequest.getStartTime = {}, FinishTime = {}", courseRequest.getStartTime(), courseRequest.getFinishTime());
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


        // 시간 제한으로 가능한 모든 코스 받아오기(시간은 24시간으로 받아야함) - 분 말고 몇시인지만 받을거임
        // 시간에 따라 코스에 속할 카테고리 개수 정하기 - 최대 main 4개나 sub 4개
        int totalTime = courseRequest.getFinishTime() - courseRequest.getStartTime(); // 총 소요 시간

        List<CourseDto> timeCourseList = courseV2Repository.findAllByTime(totalTime); // 원래처럼 하나의 테이블에서 시간에 맞는거만 뽑아냄


        
        List<CourseDto> filteredCourse = new ArrayList<>(); // 마지막에 반환할 CourseDto를 담을 리스트, 모든 조건에 해당하는 것만 담을 예정


        List<Integer> courseGoalList = new ArrayList<>();

        for(int i=0; i<10; i++) {
            if(courseRequest.getGoals().get(i)) {
                courseGoalList.add(1);
            }
            else courseGoalList.add(0);
        }


        for (CourseDto courseInfo : timeCourseList) { // 시간으로 필터링한 코스 틀 전체에서 하나씩 가져옴
            List<String> course = courseInfo.getCategories(); // 코스 가져오기 - ex) 놀기Main, 관게Main


            // 필수 장소 있을때 그게 들어가게 일단 필터링 
            if (courseRequest.getWantedCategory() != null) { // 가고싶은 카테고리 있으면
                if (!course.contains(courseRequest.getWantedCategoryGroup())) { // 필수장소(카테고리) 필터링 - 없으면 continue하도록
                    continue;
                }
            }
            // 여기까지 filteredCourse에 아무것도 없는거임

            /** 목적으로 필터링 */
            // 시간이랑 목적 한거 교집합 구하기 - 생긴게 너무 달라서 안됨

            // goal로 필터링해서 맞는 categoryGroupCode를 뽑아낸 리스트
            List<String> goalFilteringList =  coursePurposeRepository.findByCategoryGroupCodeOrWalkOrDrinkOrExperienceOrHealingOrWatchOrIntellectualOrViewOrNormalOrSportsOrSolo(courseGroupCode, courseGoalList.get(0), courseGoalList.get(1), courseGoalList.get(2), courseGoalList.get(3), courseGoalList.get(4), courseGoalList.get(5), courseGoalList.get(6), courseGoalList.get(7), courseGoalList.get(8), courseGoalList.get(9)));

            // 그렇다면 time으로 필터링 한 것 중 목적으로 필터링한거에서 CategoryGroupCode를 가지지 않는 것을 지워보자


            // 여기까지 filteredCourse에 아무것도 없는거임
//            if (!firstCourseGoalFiltering(courseInfo, courseRequest, filteredCourse)) // 코스 목적으로 필터링
//                continue;


            int count = 0;
            for(String courseGroupCode : course) { // course에 들어가는 categoryGroup 중 하나라도 목적을 만족하는게 있으면 목적에 맞는거고 아니면 안맞는 것 - 목적3개인데 카테고리 2개짜리인거 있을 수도 있으니까
                for(String goalfilteringcourseGroupCode : goalFilteringList) {
                    if(!courseGroupCode.equals(goalfilteringcourseGroupCode)) { // 목적으로 필터링한 것을 가지지 않으면 제거
                        continue;
                    }
                    else // 목적으로 필터링 한것을 가지는 코스 틀이라면
                        count++;

                }
                if(count == 0) {
                    timeCourseList.remove(courseInfo); // 현재 courseDto인 courseInfo 삭제
                }

            }
            /** 목적으로 필터링 끝 */ // - 내일 다시 점검함

//            firstCourseGoalFiltering(courseInfo, courseRequest, filteredCourse); // 코스 목적으로 필터링 - 안에서 filterCourse를 add하고 C열 카테고리에 목적을 map으로 가짐


            int N = 0; // 키워드 수치 비교에서 N을 어떻게 할지에 따라 결정

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
        selectedCourse.setWantedCategory(courseRequest.getWantedCategory()); // 필수 카테고리 저장

        // wantedCategory가 null인 경우 우선순위에 따른 주장소 선택(main)
        log.info("firstCourseFiltering Start!");
        if (selectedCourse.getWantedCategory() == null) { // 필수 카테고리 없는 경우 우선순위에 따라 코스의 주 카테고리 지정
            // 우선순위에 따른 주장소 선택
            for (CategoryPriority p : CategoryPriority.values()) {
                for (String category : selectedCourse.getCategories()) {
                    if (category.contains(p.getName())) { // 우선순위 순으로 해당 C열 카테고리를 가졌는지 확인
                        selectedCourse.setWantedCategory(p.getName()); // 가졌으면 wantedCategory로 지정하고 탈출
                        break;
                    }
                }
                if (selectedCourse.getWantedCategory() != null) { // ???
                    break;
                }
            }
        }
        log.info("selectedCourse = {}", selectedCourse);
        return selectedCourse; // 다 거르고 최종 필터링된 selectedCourse 하나만 반환
    }

    @Override
    public CourseDto firstCourseFiltering2(CourseRequest courseRequest) { // course1,2,3,4 사용
        // C열 카테고리로 이루어진 코스 생성
        log.info("firstCourseFiltering Start!");

        // 인자값이 잘 들어왔는지 확인하는 로그
        log.info("courseRequest.getNumPeople = {}", courseRequest.getNumPeople());
        log.info("courseRequest.getMemberIdList = {}", courseRequest.getMemberIdList());
        log.info("courseRequest.isMealCheck = {}", courseRequest.isMealCheck());
        log.info("courseRequest.getStartTime = {}, FinishTime = {}", courseRequest.getStartTime(), courseRequest.getFinishTime());
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


        // 시간 제한으로 가능한 모든 코스 받아오기(시간은 24시간으로 받아야함) - 분 말고 몇시인지만 받을거임
        // 시간에 따라 코스에 속할 카테고리 개수 정하기 - 최대 main 4개나 sub 4개
        int totalTime = courseRequest.getFinishTime() - courseRequest.getStartTime(); // 총 소요 시간

        List<CourseDto> timeCourseList1 = Stream.concat(course1Repository.findAllByTime(totalTime).stream(), course2Repository.findAllByTime(totalTime).stream()).collect(Collectors.toList()); // Course1, Course2에서 시간 맞는거 가져와서 합친 리스트
        List<CourseDto> timeCourseList2 = Stream.concat(course3Repository.findAllByTime(totalTime).stream(), course4Repository.findAllByTime(totalTime).stream()).collect(Collectors.toList()); // Course3, Course4에서 시간 맞는거 가져와서 합친 리스트
        List<CourseDto> timeCourseList = Stream.concat(timeCourseList1.stream(), timeCourseList2.stream()).collect(Collectors.toList()); // 위 두 개 리스트를 합쳐서 Course1부터 Course4까지 나온 리스트 다 가진 리스트

        // Course1,2,3,4 4개가 다 다르므로 그것을 통합하여 필요한 것들을 사용하기 위해 CourseDto로 반환형 하고 이것에 넣어서 사용
        List<CourseDto> filteredCourse = new ArrayList<>(); // 마지막에 반환할 CourseDto를 담을 리스트, 모든 조건에 해당하는 것만 담을 예정

        List<Integer> courseGoalList = new ArrayList<>();

        for(int i=0; i<10; i++) {
            if(courseRequest.getGoals().get(i)) {
                courseGoalList.add(1);
            }
            else courseGoalList.add(0);
        }

        for (CourseDto courseInfo : timeCourseList) { // 시간으로 필터링한 코스 틀 전체에서 하나씩 가져옴
            List<String> course = courseInfo.getCategories(); // 코스 가져오기 - ex) 놀기Main 스포츠, 관게Main 관람


            // 필수 장소 있을때 그게 들어가게 일단 필터링
            if (courseRequest.getWantedCategory() != null) { // 가고싶은 카테고리 있으면
                if (!course.contains(courseRequest.getWantedCategoryGroup())) { // 필수장소(카테고리) 필터링 - 가져온 코스에서 wantedCategoryGroup이 있는지 확인
                    continue;
                }
            }

            /** 목적으로 필터링 */
            // 시간이랑 목적 한거 교집합 구하기 - 생긴게 너무 달라서 안됨

            // goal로 필터링해서 맞는 categoryGroupCode를 뽑아낸 리스트
            List<String> goalFilteringList =  coursePurposeRepository.findByCategoryGroupCodeOrWalkOrDrinkOrExperienceOrHealingOrWatchOrIntellectualOrViewOrNormalOrSportsOrSolo(courseGroupCode, courseGoalList.get(0), courseGoalList.get(1), courseGoalList.get(2), courseGoalList.get(3), courseGoalList.get(4), courseGoalList.get(5), courseGoalList.get(6), courseGoalList.get(7), courseGoalList.get(8), courseGoalList.get(9)));

            // 그렇다면 time으로 필터링 한 것 중 목적으로 필터링한거에서 CategoryGroupCode를 가지지 않는 것을 지워보자


            // 여기까지 filteredCourse에 아무것도 없는거임
//            if (!firstCourseGoalFiltering(courseInfo, courseRequest, filteredCourse)) // 코스 목적으로 필터링
//                continue;


            int count = 0;
            for(String courseGroupCode : course) { // course에 들어가는 categoryGroup 중 하나라도 목적을 만족하는게 있으면 목적에 맞는거고 아니면 안맞는 것 - 목적3개인데 카테고리 2개짜리인거 있을 수도 있으니까
                for(String goalfilteringcourseGroupCode : goalFilteringList) {
                    if(!courseGroupCode.equals(goalfilteringcourseGroupCode)) { // 목적으로 필터링한 것을 가지지 않으면 제거
                        continue;
                    }
                    else // 목적으로 필터링 한것을 가지는 코스 틀이라면
                        count++;

                }
                if(count == 0) {
                    timeCourseList.remove(courseInfo); // 현재 courseDto인 courseInfo 삭제
                }

            }
            /** 목적으로 필터링 끝 */ // - 내일 다시 점검함

//            firstCourseGoalFiltering(courseInfo, courseRequest, filteredCourse); // 코스 목적으로 필터링 - 안에서 filterCourse를 add하고 C열 카테고리에 목적을 map으로 가짐


            int N = 0; // 키워드 수치 비교에서 N을 어떻게 할지에 따라 결정

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
        selectedCourse.setWantedCategory(courseRequest.getWantedCategory()); // 필수 카테고리 저장

        // wantedCategory가 null인 경우 우선순위에 따른 주장소 선택(main)
        log.info("firstCourseFiltering Start!");
        if (selectedCourse.getWantedCategory() == null) { // 필수 카테고리 없는 경우 우선순위에 따라 코스의 주 카테고리 지정
            // 우선순위에 따른 주장소 선택
            for (CategoryPriority p : CategoryPriority.values()) {
                for (String category : selectedCourse.getCategories()) {
                    if (category.contains(p.getName())) { // 우선순위 순으로 해당 C열 카테고리를 가졌는지 확인
                        selectedCourse.setWantedCategory(p.getName()); // 가졌으면 wantedCategory로 지정하고 탈출
                        break;
                    }
                }
                if (selectedCourse.getWantedCategory() != null) { // ???
                    break;
                }
            }
        }
        log.info("selectedCourse = {}", selectedCourse);
        return selectedCourse; // 다 거르고 최종 필터링된 selectedCourse 하나만 반환
        return null;
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
            String randomCategory = smallCategories.get(rand.nextInt(smallCategories.size()));
            // firstCourse의 wantedCategory를 secondCourse에 옮기기
            if(firstCourse.getWantedCategory().equals(s)){
                detailCategories.add(0, randomCategory);
                secondCourse.setWantedCategoryCode(firstCourse.getWantedCategory());
            }else{
                detailCategories.add(randomCategory);
            }
        }
        secondCourse.setDetailCategoryCodes(detailCategories);
        return secondCourse;
    }

    // 실제 가게들 선택 및 최적화 알고리즘 적용
    @Override
    public FinalCourse finalCourseFiltering(SecondCourse secondCourse) {
        boolean complete=false;
        int num=1;
        Double[] mainCoordinate = new Double[]{0.0, 0.0};
        List<PlaceDto> restaurants=new ArrayList<>();
        // wantedCategory 데이터를 전부 별점순으로 가져오기(내림차순)
        List<PlaceDto> wantedCategoryPlaceList = getCategoryPlaceList(secondCourse.getWantedCategoryCode());
        // 카테고리를 돌려보면서 하나하나 반경 기준으로 가져오기.
        List<PlaceDto> wantedCourse = getFinalCourse(secondCourse, wantedCategoryPlaceList, 1, mainCoordinate);
        // 만약 결과가 없다면 두번째 우선순위 카테고리 데이터를 전부 별점순으로 가져오기(내림차순)
        if(wantedCourse.isEmpty()){
            wantedCategoryPlaceList = getCategoryPlaceList(secondCourse.getDetailCategoryCodes().get(1));
            wantedCourse = getFinalCourse(secondCourse, wantedCategoryPlaceList, 2, mainCoordinate);
            if(wantedCourse.isEmpty()) throw new RuntimeException("not enough data so no recommendation!");
        }
        // 아침 : 8시~10시 점심 : 12시~2시 저녁 : 6시~8시에 노는시간이 걸쳐있을 경우, 식사 포함. 안걸쳐있으면 그냥 하나만 추가.
        if(secondCourse.getMealCheck()){
            num=getRestaurantNum(secondCourse.getStartTime(), secondCourse.getEndTime());
            restaurants = getRestaurantFromApi(mainCoordinate, num);
        }
        // 최적화 알고리즘 적용(순서 결정)



        return places;
    }




    /**
     * 코스 목적에 따른 필터링 메서드 - FirstCourse에서 사용
     * @param course - 현재 확인중인 코스
     * @param courseRequest - 입력으로 들어온 코스 목적을 사용하기 위해
     * @return
     */
    private void firstCourseGoalFiltering(CourseDto course, CourseRequest courseRequest, List<CourseDto> filteredCourse) {
        // course에 목적이 있으면 return true, 없으면 return false

        String[] courseGoals = course.getGoals().split(","); // 각 코스의 목적이 여러 개 있는 경우를 위해 ,로 구분

        List<Integer> courseGoalList = new ArrayList<>();

        for(int i=0; i<10; i++) {
            if(courseRequest.getGoals().get(i)) {
                courseGoalList.add(1);
            }
            else courseGoalList.add(0)
        }



        String[] goalNames = {"산책", "음주", "체험", "힐링", "관람", "지적활동", "경치", "일반", "스포츠", "솔로"}; // 목적 이름 리스트


        for (int i = 0; i < 10; i++) { // 신책, 음주, 체험, 힐링, 관람, 지적활동, 경치, 일반, 스포츠, 솔로
            if (courseRequest.getGoals().get(i)) { // 목적 하나씩 가져와서 있는 경우만 필터링 진행

                for (String goal : courseGoals) { // course가 가지는 목적들 하나하나가 goal
                    if (!goal.equals(goalNames[i])) { // 해당 목적이 없으면 다음으로 넘김
                        continue; // 해당 목적이 없으면 다음으로 넘기기
                    } else { // 목적이 있으면

                        List<String> CColKeyList = new ArrayList<>(); // d열 카테고리가 속하는 c열 카테고리의 이름을 담을 리스트 - 어차피 1개만 담김
                        // 해당 목적이 속하는 D열 카테고리를 찾고, - 여러개니까 리스트일 것
                        List<Category> dColCategoriesList = categoryRepository.findAllByGoal(goal); // 해당 goal에 맞는 DColCategory 가져오기

                        for (Category dColCategory: dColCategoriesList) { // 리스트에 속하는 D열 카테고리 하나하나마다 수행

                            SmallCategory smallCategory = SmallCategory.getInstance();
                            // 그 D열 카테고리가 속하는 C열 카테고리를 찾고 - CColKeyList에 넣어놓음
                            for(Map.Entry<String, List<String>> entry : smallCategory.getCategories().entrySet()) {
                                for(int j=0; j<dColCategoriesList.size(); j++) {
                                    if(entry.getValue().contains(dColCategoriesList.get(j))) {
                                        CColKeyList.add(entry.getKey());
                                    }
                                }
                            }

                        }

                        // 그 C열 카테고리에 목적을 연결시키는 goalMatch를 채운다 - CColKeyList에 있는 C열 카테고리들을 이용해 goalMatch에 채움
                        for(String cColKey : CColKeyList) {
                                filteredCourse.add(CourseDto.fromEntity(course)); // 사용자가 입력한 목적을 갖는 것에 대해 filterCourse.add시킴
                                CourseDto courseDto = filteredCourse.get(filteredCourse.size()-1); // add시킨 CourseDto를 set하기 위해 get으로 꺼내옴

                                Map<String, String> addGoalMatch = new HashMap<>(); // setGoalMatch 하기 위해 Map 하나 생성
                                addGoalMatch.put(cColKey, goal); // C열이 키, 들어가는 목적이 value
                                courseDto.setGoalMatch(addGoalMatch); // 키 밸류 맞춘 map을 추가

                        }

                        return; // 하나라도 해당하는 목적이 있으면 true 반환 - 목적 2개 했는데 3시간이면 메인 한개만 해야할 수도 있으므로
                    }
                }
            }
        }
        return; // 위 반복문에서 return되지 않으면 add없이 그냥 끝냄
    }

    private boolean firstCourseGoalFiltering2(List<String> course, CourseRequest courseRequest, List<CourseDto> filteredCourse) {
        // course에 목적이 있으면 return true, 없으면 return false

        for(String categoryGroupCode : course) {
            if()
        }


        String[] courseGoals = course.getGoals().split(","); // 각 코스의 목적이 여러 개 있는 경우를 위해 ,로 구분

        List<Integer> courseGoalList = new ArrayList<>();

        for(int i=0; i<10; i++) {
            if(courseRequest.getGoals().get(i)) {
                courseGoalList.add(1);
            }
            else courseGoalList.add(0)
        }



        String[] goalNames = {"산책", "음주", "체험", "힐링", "관람", "지적활동", "경치", "일반", "스포츠", "솔로"}; // 목적 이름 리스트


        for (int i = 0; i < 10; i++) { // 신책, 음주, 체험, 힐링, 관람, 지적활동, 경치, 일반, 스포츠, 솔로
            if (courseRequest.getGoals().get(i)) { // 목적 하나씩 가져와서 있는 경우만 필터링 진행

                for (String goal : courseGoals) { // course가 가지는 목적들 하나하나가 goal
                    if (!goal.equals(goalNames[i])) { // 해당 목적이 없으면 다음으로 넘김
                        continue; // 해당 목적이 없으면 다음으로 넘기기
                    } else { // 목적이 있으면

                        List<String> CColKeyList = new ArrayList<>(); // d열 카테고리가 속하는 c열 카테고리의 이름을 담을 리스트 - 어차피 1개만 담김
                        // 해당 목적이 속하는 D열 카테고리를 찾고, - 여러개니까 리스트일 것
                        List<Category> dColCategoriesList = categoryRepository.findAllByGoal(goal); // 해당 goal에 맞는 DColCategory 가져오기

                        for (Category dColCategory: dColCategoriesList) { // 리스트에 속하는 D열 카테고리 하나하나마다 수행

                            SmallCategory smallCategory = SmallCategory.getInstance();
                            // 그 D열 카테고리가 속하는 C열 카테고리를 찾고 - CColKeyList에 넣어놓음
                            for(Map.Entry<String, List<String>> entry : smallCategory.getCategories().entrySet()) {
                                for(int j=0; j<dColCategoriesList.size(); j++) {
                                    if(entry.getValue().contains(dColCategoriesList.get(j))) {
                                        CColKeyList.add(entry.getKey());
                                    }
                                }
                            }

                        }

                        // 그 C열 카테고리에 목적을 연결시키는 goalMatch를 채운다 - CColKeyList에 있는 C열 카테고리들을 이용해 goalMatch에 채움
                        for(String cColKey : CColKeyList) {
                            filteredCourse.add(CourseDto.fromEntity(course)); // 사용자가 입력한 목적을 갖는 것에 대해 filterCourse.add시킴
                            CourseDto courseDto = filteredCourse.get(filteredCourse.size()-1); // add시킨 CourseDto를 set하기 위해 get으로 꺼내옴

                            Map<String, String> addGoalMatch = new HashMap<>(); // setGoalMatch 하기 위해 Map 하나 생성
                            addGoalMatch.put(cColKey, goal); // C열이 키, 들어가는 목적이 value
                            courseDto.setGoalMatch(addGoalMatch); // 키 밸류 맞춘 map을 추가

                        }

                        return; // 하나라도 해당하는 목적이 있으면 true 반환 - 목적 2개 했는데 3시간이면 메인 한개만 해야할 수도 있으므로
                    }
                }
            }
        }
        return; // 위 반복문에서 return되지 않으면 add없이 그냥 끝냄
    }

    /**
     * 코스 키워드 필터링
     * @param course
     * @param courseRequest
     * @param N
     * @return
     */
    private Boolean firstCourseKeywordFiltering(CourseDto course, CourseRequest courseRequest, int N) {
        // N보다 높거나 낮은것을 따지므로
        // course에 키워드가 해당되면 return true, 해당되지 않으면 return false

        for (int i = 0; i < 8; i++) { // 이색, 일상 / 액티비티, 앉아서놀기 / 체력높은, 체력낮음 / 실외위주, 실내위주
            if (courseRequest.getCourseKeywords().get(i)) { // 키워드 하나씩 가져와서 있는 경우만 필터링 진행
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

//                    case 6: // 실외위주
//                        if( !(course.getOutdoor() > N) ) {
//                            return false; // 실외위주인데 실내외수치가 낮으면 false 반환
//                        }
//                        else {
//                            continue; // 액티비티인데 활동성이 높으면 다른 키워드들도 비교해봐야 함
//                        }
//
//                    case 7: // 실내위주
//                        if( (course.getOutdoor() > N) ) {
//                            return false; // 실내위주인데 실내외수치가 높으면 false 반환
//                        }
//                        else {
//                            continue; // 앉아서놀기인데 활동성이 낮으면 다른 키워드들도 비교해봐야 함
//                        }
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
        for(PlaceDto s : wantedCategoryPlaceList){
            course.clear();
            for (int i = start; i < secondCourse.getDetailCategoryCodes().size(); i++) {
                PlaceDto place= PlaceDto.fromEntity(
                        placeRepository.findRandomPlaceByCategoryAndLocationAndRadius(
                                secondCourse.getDetailCategoryCodes().get(i), s.getX(), s.getY(), FILTER_RADIUS));
                if(place==null){
                    break;
                }else if(i== secondCourse.getDetailCategoryCodes().size()-1){
                    mainCoordinate[0]=s.getX();
                    mainCoordinate[1]=s.getY();
                    return course;
                }
                course.add(place);
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

    private int getRestaurantNum(int start, int end) {
        int cnt=1;
        boolean breakfast = (start >= 8 && end <= 10);
        boolean lunch = (start >= 12 && end <= 14);
        boolean dinner = (start >= 18 && end <= 20);

        if (breakfast && lunch && dinner) {
            cnt = 3;
        } else if ((breakfast && lunch) || (lunch && dinner) || (breakfast && dinner)) {
            cnt = 2;
        } else if (breakfast || lunch || dinner) {
            cnt = 1;
        } else {
            cnt = 1;
        }

        return cnt;
    }
}
