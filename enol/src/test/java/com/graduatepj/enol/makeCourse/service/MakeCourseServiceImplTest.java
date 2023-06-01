package com.graduatepj.enol.makeCourse.service;
import com.graduatepj.enol.makeCourse.dao.*;
import com.graduatepj.enol.makeCourse.dto.CourseDto;
import com.graduatepj.enol.makeCourse.dto.CourseRequest;
import com.graduatepj.enol.makeCourse.dto.SecondCourse;
import com.graduatepj.enol.makeCourse.vo.CourseV2;
import com.graduatepj.enol.member.dao.HistoryRepository;
import com.graduatepj.enol.member.dao.UserMarkRepository;
import com.graduatepj.enol.member.dao.UserRepository;
import com.graduatepj.enol.member.service.MemberService;
import com.graduatepj.enol.member.vo.History;
import com.graduatepj.enol.member.vo.User;
import com.graduatepj.enol.member.vo.UserMark;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@SpringBootTest
class MakeCourseServiceImplTest {
    @Autowired
    private CourseMemberRepository courseMemberRepository;
    @Autowired
    private PlaceRepository placeRepository;
    private static final int CATEGORY_NUM = 62;
    private static final int FILTER_RADIUS = 200;
    @Autowired
    private MakeCourseService makeCourseService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CourseV2Repository courseV2Repository;
    @Autowired
    private CategoryPurposeRepository categoryPurposeRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMarkRepository userMarkRepository;
    @Autowired
    private HistoryRepository historyRepository;


    @Resource(name = "memberService")
    private MemberService memberService;

    // makeCourse 통합 테스트
    @Test
    void makeCourseTest() {
        //given(어떤 데이터가 있을때)
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)
    }

    // firstCourse 통합 테스트
    @Test
    void firstCourseTest() {
        //given(어떤 데이터가 있을때)
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)

        // given


        MakeCourseServiceImpl makeCourseServiceImpl = new MakeCourseServiceImpl(courseMemberRepository, placeRepository, restaurantRepository, categoryRepository, courseV2Repository, categoryPurposeRepository, memberService);

        List<String> memberList = new ArrayList<>();
        memberList.add("kim1");
        memberList.add("kim2");

        List<Integer> keywordList = new ArrayList<>();
        keywordList.add(0);
        keywordList.add(0);
        keywordList.add(0);
        keywordList.add(1);
        keywordList.add(0);
        keywordList.add(0);

        List<Integer> goalList = new ArrayList<>();
        goalList.add(1);
        goalList.add(0);
        goalList.add(0);
        goalList.add(1);
        goalList.add(0);
        goalList.add(0);
        goalList.add(0);
        goalList.add(0);
        goalList.add(0);
        goalList.add(0);

        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setNumPeople(2);
        courseRequest.setMemberIdList(memberList);
        courseRequest.setMealCheck(true);
        courseRequest.setStartTime(18);
        courseRequest.setFinishTime(24);
        courseRequest.setWantedCategoryGroup(null);
        courseRequest.setWantedCategory(null);
//        courseRequest.setWantedCategoryGroup("EM1");
//        courseRequest.setWantedCategory("CB");
        courseRequest.setCourseKeywords(keywordList);
        courseRequest.setGoals(goalList);


        // when
        CourseDto courseDto = makeCourseServiceImpl.firstCourseFiltering(courseRequest);

        //then
        System.out.println("--- firstCourseFiltering START ---");
        System.out.println("courseDto.getWantedCategoryGroup() = " + courseDto.getWantedCategoryGroup());
        System.out.println("courseDto.getWantedCategory() = " + courseDto.getWantedCategory());
        System.out.println("courseDto.getCategoryGroupCode1() = " + courseDto.getCategoryGroupCode1());
        System.out.println("courseDto.getCategoryGroupCode2() = " + courseDto.getCategoryGroupCode2());
        System.out.println("courseDto.getCategoryGroupCode3() = " + courseDto.getCategoryGroupCode3());
        System.out.println("courseDto.getCategoryGroupCode4() = " + courseDto.getCategoryGroupCode4());
        System.out.println("courseDto.getTime() = " + courseDto.getTime());
        System.out.println("courseDto.isMealCheck() = " + courseDto.isMealCheck());
        System.out.println("courseDto.isDawnDrink() = " + courseDto.isDawnDrink());
        System.out.println("courseDto.getFatigability() = " + courseDto.getFatigability());
        System.out.println("courseDto.getSpecification() = " + courseDto.getSpecification());
        System.out.println("courseDto.getActivity() = " + courseDto.getActivity());
        System.out.println("courseDto.getStartTime() = " + courseDto.getStartTime());
        System.out.println("courseDto.getFinishTime() = " + courseDto.getFinishTime());

        System.out.println("courseDto.getGoalMatch()1 = " + courseDto.getGoalMatch().get(courseDto.getCategoryGroupCode1()));
        System.out.println("courseDto.getGoalMatch()2 = " + courseDto.getGoalMatch().get(courseDto.getCategoryGroupCode2()));
        System.out.println("courseDto.getGoalMatch()3 = " + courseDto.getGoalMatch().get(courseDto.getCategoryGroupCode3()));
        System.out.println("courseDto.getGoalMatch()4 = " + courseDto.getGoalMatch().get(courseDto.getCategoryGroupCode4()));

        Assertions.assertThat(courseDto).isNull();

        System.out.println("--- firstCourseFiltering END ---");
    }

    // secondCourse 통합 테스트
    @Test
    void secondCourseTest() {
        //given(어떤 데이터가 있을때)
        MakeCourseServiceImpl makeCourseServiceImpl = new MakeCourseServiceImpl(courseMemberRepository, placeRepository, restaurantRepository, categoryRepository, courseV2Repository, categoryPurposeRepository, memberService);

        List<String> memberList = new ArrayList<>();
        memberList.add("kim1");
        memberList.add("kim2");

        List<Integer> keywordList = new ArrayList<>();
        keywordList.add(0);
        keywordList.add(0);
        keywordList.add(0);
        keywordList.add(1);
        keywordList.add(0);
        keywordList.add(0);

        List<Integer> goalList = new ArrayList<>();
        goalList.add(1);
        goalList.add(0);
        goalList.add(1);
        goalList.add(0);
        goalList.add(0);
        goalList.add(0);
        goalList.add(0);
        goalList.add(0);
        goalList.add(1);
        goalList.add(0);

        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setNumPeople(2);
        courseRequest.setMemberIdList(memberList);
        courseRequest.setMealCheck(true);
        courseRequest.setStartTime(17);
        courseRequest.setFinishTime(24);
        courseRequest.setWantedCategoryGroup(null);
        courseRequest.setWantedCategory(null);
//        courseRequest.setWantedCategoryGroup("EM1");
//        courseRequest.setWantedCategory("CB");
        courseRequest.setCourseKeywords(keywordList);
        courseRequest.setGoals(goalList);


        CourseDto courseDto = makeCourseServiceImpl.firstCourseFiltering(courseRequest);

        //when(어떤 동작을 하게되면)
        SecondCourse secondCourse = makeCourseServiceImpl.secondCourseFiltering(courseDto);

        //then(어떤 결과가 나와야한다)
        System.out.println("secondCourse.getSelectedCourse().getCategoryGroupCode1 = " + secondCourse.getSelectedCourse().getCategoryGroupCode1());
        System.out.println("secondCourse.getSelectedCourse().getCategoryGroupCode2 = " + secondCourse.getSelectedCourse().getCategoryGroupCode2());
        System.out.println("secondCourse.getSelectedCourse().getCategoryGroupCode3 = " + secondCourse.getSelectedCourse().getCategoryGroupCode3());
        System.out.println("secondCourse.getSelectedCourse().getCategoryGroupCode4 = " + secondCourse.getSelectedCourse().getCategoryGroupCode4());
        System.out.println("secondCourse.getSelectedCourse().getWantedCategoryGroup = " + secondCourse.getSelectedCourse().getWantedCategoryGroup());
        System.out.println("secondCourse.getSelectedCourse().getWantedCategory = " + secondCourse.getSelectedCourse().getWantedCategory());
        System.out.println("secondCourse.getSelectedCourse().getGoalMatch = " + secondCourse.getSelectedCourse().getGoalMatch());
        System.out.println("secondCourse.getSelectedCourse().getFatigability = " + secondCourse.getSelectedCourse().getFatigability());
        System.out.println("secondCourse.getSelectedCourse().getSpecification = " + secondCourse.getSelectedCourse().getSpecification());
        System.out.println("secondCourse.getSelectedCourse().getActivity = " + secondCourse.getSelectedCourse().getActivity());
        System.out.println("secondCourse.getSelectedCourse().getTime = " + secondCourse.getSelectedCourse().getTime());
        System.out.println("secondCourse.getSelectedCourse().getRate = " + secondCourse.getSelectedCourse().getRate());
        System.out.println();
        System.out.println("secondCourse.getCategoryGroupCode = " + secondCourse.getCategoryGroupCodes());
        System.out.println("secondCourse.getDetailCategoryCodes = " + secondCourse.getDetailCategoryNames());
        System.out.println("secondCourse.getWantedCategoryGroupCode = " + secondCourse.getWantedCategoryGroupCode());
        System.out.println("secondCourse.getWantedCategoryCode = " + secondCourse.getWantedCategoryName());
        System.out.println("secondCourse.getDawnDrink = " + secondCourse.getDawnDrink());
        System.out.println("secondCourse.getMealCheck = " + secondCourse.getMealCheck());
        System.out.println("secondCourse.getCategoryGroupCode = " + secondCourse.getCategoryGroupCodes());
        System.out.println("secondCourse.getStartTime = " + secondCourse.getStartTime());
        System.out.println("secondCourse.getStartTime = " + secondCourse.getEndTime());

    }

    // finalCourse 통합 테스트
    @Test
    void finalCourseTest() {
        //given(어떤 데이터가 있을때)
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)
        // 모든 카테고리 그룹 코스 가져오기
        int failCnt=0;
        int successCnt=0;
        int groupCourseCnt=0;
        int total = 0;
        List<CourseV2> groupCourseList = courseV2Repository.findAll();
        System.out.println("groupCourseList 개수 : " + groupCourseList.size());
        for(CourseV2 groupCourse : groupCourseList){
            List<String> detailCategories1 = new ArrayList<>();
            List<String> detailCategories2 = new ArrayList<>();
            List<String> detailCategories3 = new ArrayList<>();
            List<String> detailCategories4 = new ArrayList<>();
            List<String> detailCategorieCodes = new ArrayList<>();
            SecondCourse secondCourse = new SecondCourse();

            System.out.print(groupCourseCnt++ + "번째 코스그룹 : ");
            // 카테고리 그룹 코드에 해당하는 세부 카테고리 코드 리스트를 가져오기
            if(groupCourse.getCategoryGroupCode1()!=null){
                detailCategories1=categoryRepository.findCategoryNameByCategoryGroupCode(groupCourse.getCategoryGroupCode1());
                System.out.print(groupCourse.getCategoryGroupCode1() + " ");
                total=detailCategories1.size();
            }
            if(groupCourse.getCategoryGroupCode2()!=null){
                detailCategories2=categoryRepository.findCategoryNameByCategoryGroupCode(groupCourse.getCategoryGroupCode2());
                System.out.print(groupCourse.getCategoryGroupCode2() + " ");
                total*=detailCategories2.size();
            }
            if(groupCourse.getCategoryGroupCode3()!=null){
                detailCategories3=categoryRepository.findCategoryNameByCategoryGroupCode(groupCourse.getCategoryGroupCode3());
                System.out.print(groupCourse.getCategoryGroupCode3() + " ");
                total*=detailCategories3.size();
            }
            if(groupCourse.getCategoryGroupCode4()!=null){
                detailCategories4=categoryRepository.findCategoryNameByCategoryGroupCode(groupCourse.getCategoryGroupCode4());
                System.out.print(groupCourse.getCategoryGroupCode4());
                total*=detailCategories4.size();
            }
            total*=(20*12);
            System.out.println(" | 현재시간 : "+ LocalDateTime.now() + " | 총 경우의 수 : " + total);

            for (int s = 6; s <= 25; s++) {
                for (int e = s+1; e <= s+12; e++) {
//                for (int e = s+1; e <= s+1; e++) {
                    if(e==29) break;
//                    System.out.println("startTime = " + s + " endTime = " + e);
                    if(groupCourse.getCategoryGroupCode1()!=null){
                        for (int i = 0; i < detailCategories1.size(); i++) {
                            detailCategorieCodes.add(detailCategories1.get(i));
                            if(groupCourse.getCategoryGroupCode2()!=null){
                                for (int j = 0; j < detailCategories2.size(); j++) {
                                    detailCategorieCodes.add(detailCategories2.get(j));
                                    if(groupCourse.getCategoryGroupCode3()!=null){
                                        for (int k = 0; k < detailCategories3.size(); k++) {
                                            detailCategorieCodes.add(detailCategories3.get(k));
                                            if(groupCourse.getCategoryGroupCode4()!=null){
                                                for (int l = 0; l < detailCategories4.size(); l++) {
                                                    // 4개 코스
                                                    detailCategorieCodes.add(detailCategories4.get(l));
                                                    secondCourse = SecondCourse.builder()
                                                            .wantedCategoryName(detailCategories1.get(i))
                                                            .detailCategoryNames(detailCategorieCodes)
                                                            .startTime(s)
                                                            .endTime(e)
                                                            .mealCheck(true)
                                                            .build();
                                                    // 식사포함
//                                                    System.out.println("4개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
//                                                            " | 2번 : " + secondCourse.getDetailCategoryNames().get(1) +
//                                                            " | 3번 : " + secondCourse.getDetailCategoryNames().get(2) +
//                                                            " | 4번 : " + secondCourse.getDetailCategoryNames().get(3) +
//                                                            " | 식사여부 : " + secondCourse.getMealCheck() +
//                                                            " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
//                                                    if(makeCourseService.finalCourseFiltering(secondCourse)==null){
//                                                        failCnt++;
//                                                        printThing(groupCourse, s, e);
//                                                        System.out.println("4개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
//                                                                " | 2번 : " + secondCourse.getDetailCategoryNames().get(1) +
//                                                                " | 3번 : " + secondCourse.getDetailCategoryNames().get(2) +
//                                                                " | 4번 : " + secondCourse.getDetailCategoryNames().get(3) +
//                                                                " | 식사여부 : " + secondCourse.getMealCheck() +
//                                                                " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
//                                                    }else{
//                                                        successCnt++;
//                                                    }
                                                    // 식사미포함
                                                    secondCourse.setMealCheck(false);
//                                                    System.out.println("4개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
//                                                            " | 2번 : " + secondCourse.getDetailCategoryNames().get(1) +
//                                                            " | 3번 : " + secondCourse.getDetailCategoryNames().get(2) +
//                                                            " | 4번 : " + secondCourse.getDetailCategoryNames().get(3) +
//                                                            " | 식사여부 : " + secondCourse.getMealCheck() +
//                                                            " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
                                                    if(makeCourseService.finalCourseFiltering(secondCourse)==null){
                                                        failCnt++;
                                                        printThing(groupCourse, s, e);
                                                        System.out.println("4개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
                                                                " | 2번 : " + secondCourse.getDetailCategoryNames().get(1) +
                                                                " | 3번 : " + secondCourse.getDetailCategoryNames().get(2) +
                                                                " | 4번 : " + secondCourse.getDetailCategoryNames().get(3) +
                                                                " | 식사여부 : " + secondCourse.getMealCheck() +
                                                                " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
                                                    }else{
                                                        successCnt++;
                                                    }
                                                    detailCategorieCodes.remove(detailCategorieCodes.size()-1);
                                                }
                                                detailCategorieCodes.remove(detailCategorieCodes.size()-1);
                                            }else{ // 3개 코스
                                                secondCourse = SecondCourse.builder()
                                                        .wantedCategoryName(detailCategories1.get(i))
                                                        .detailCategoryNames(detailCategorieCodes)
                                                        .startTime(s)
                                                        .endTime(e)
                                                        .mealCheck(true)
                                                        .build();
                                                // 식사포함
//                                                System.out.println("3개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
//                                                        " | 2번 : " + secondCourse.getDetailCategoryNames().get(1) +
//                                                        " | 3번 : " + secondCourse.getDetailCategoryNames().get(2) +
//                                                        " | 식사여부 : " + secondCourse.getMealCheck() +
//                                                        " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
//                                                if(makeCourseService.finalCourseFiltering(secondCourse)==null){
//                                                    failCnt++;
//                                                    printThing(groupCourse, s, e);
//                                                    System.out.println("3개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
//                                                            " | 2번 : " + secondCourse.getDetailCategoryNames().get(1) +
//                                                            " | 3번 : " + secondCourse.getDetailCategoryNames().get(2) +
//                                                            " | 식사여부 : " + secondCourse.getMealCheck() +
//                                                            " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
//                                                }else{
//                                                    successCnt++;
//                                                }
                                                // 식사미포함
                                                secondCourse.setMealCheck(false);
//                                                System.out.println("3개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
//                                                        " | 2번 : " + secondCourse.getDetailCategoryNames().get(1) +
//                                                        " | 3번 : " + secondCourse.getDetailCategoryNames().get(2) +
//                                                        " | 식사여부 : " + secondCourse.getMealCheck() +
//                                                        " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
                                                if(makeCourseService.finalCourseFiltering(secondCourse)==null){
                                                    failCnt++;
                                                    printThing(groupCourse, s, e);
                                                    System.out.println("3개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
                                                            " | 2번 : " + secondCourse.getDetailCategoryNames().get(1) +
                                                            " | 3번 : " + secondCourse.getDetailCategoryNames().get(2) +
                                                            " | 식사여부 : " + secondCourse.getMealCheck() +
                                                            " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
                                                }else{
                                                    successCnt++;
                                                }
                                                detailCategorieCodes.remove(detailCategorieCodes.size()-1);
                                            }
                                        }
                                        detailCategorieCodes.remove(detailCategorieCodes.size()-1);
                                    }else{ // 2개 코스
                                        secondCourse = SecondCourse.builder()
                                                .wantedCategoryName(detailCategories1.get(i))
                                                .detailCategoryNames(detailCategorieCodes)
                                                .startTime(s)
                                                .endTime(e)
                                                .mealCheck(true)
                                                .build();
                                        // 식사포함
//                                        System.out.println("2개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
//                                                " | 2번 : " + secondCourse.getDetailCategoryNames().get(1) +
//                                                " | 식사여부 : " + secondCourse.getMealCheck() +
//                                                " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
//                                        if(makeCourseService.finalCourseFiltering(secondCourse)==null){
//                                            failCnt++;
//                                            printThing(groupCourse, s, e);
//                                            System.out.println("2개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
//                                                    " | 2번 : " + secondCourse.getDetailCategoryNames().get(1) +
//                                                    " | 식사여부 : " + secondCourse.getMealCheck() +
//                                                    " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
//                                        }else{
//                                            successCnt++;
//                                        }
                                        // 식사미포함
                                        secondCourse.setMealCheck(false);
//                                        System.out.println("2개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
//                                                " | 2번 : " + secondCourse.getDetailCategoryNames().get(1) +
//                                                " | 식사여부 : " + secondCourse.getMealCheck() +
//                                                " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
                                        if(makeCourseService.finalCourseFiltering(secondCourse)==null){
                                            failCnt++;
                                            printThing(groupCourse, s, e);
                                            System.out.println("2개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
                                                    " | 2번 : " + secondCourse.getDetailCategoryNames().get(1) +
                                                    " | 식사여부 : " + secondCourse.getMealCheck() +
                                                    " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
                                        }else{
                                            successCnt++;
                                        }
                                        detailCategorieCodes.remove(detailCategorieCodes.size()-1);
                                    }
                                }
                                detailCategorieCodes.remove(detailCategorieCodes.size()-1);
                            }else{ // 1개 코스
                                secondCourse = SecondCourse.builder()
                                        .wantedCategoryName(detailCategories1.get(i))
                                        .detailCategoryNames(detailCategorieCodes)
                                        .startTime(s)
                                        .endTime(e)
                                        .mealCheck(true)
                                        .build();
                                // 식사포함
//                                System.out.println("1개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
//                                        " | 식사여부 : " + secondCourse.getMealCheck() +
//                                        " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
//                                if(makeCourseService.finalCourseFiltering(secondCourse)==null){
//                                    failCnt++;
//                                    printThing(groupCourse, s, e);
//                                    System.out.println("1개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
//                                            " | 식사여부 : " + secondCourse.getMealCheck() +
//                                            " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
//                                }else{
//                                    successCnt++;
//                                }
                                // 식사미포함
                                secondCourse.setMealCheck(false);
//                                System.out.println("1개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
//                                        " | 식사여부 : " + secondCourse.getMealCheck() +
//                                        " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
                                if(makeCourseService.finalCourseFiltering(secondCourse)==null){
                                    failCnt++;
                                    printThing(groupCourse, s, e);
                                    System.out.println("1개 코스 | 1번 : " + secondCourse.getDetailCategoryNames().get(0) +
                                            " | 식사여부 : " + secondCourse.getMealCheck() +
                                            " | 필수 카테고리 : " + secondCourse.getWantedCategoryName());
                                }else{
                                    successCnt++;
                                }
                                detailCategorieCodes.remove(detailCategorieCodes.size()-1);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("failCnt = " + failCnt);
        System.out.println("successCnt = " + successCnt);
    }

    private void printThing(CourseV2 groupCourse, int start, int end){
        System.out.println("startTime = " + start + " endTime = " + end);
        System.out.print("코스그룹 : ");
        // 카테고리 그룹 코드에 해당하는 세부 카테고리 코드 리스트를 출력
        if(groupCourse.getCategoryGroupCode1()!=null){
            System.out.print(groupCourse.getCategoryGroupCode1() + " ");
        }
        if(groupCourse.getCategoryGroupCode2()!=null){
            System.out.print(groupCourse.getCategoryGroupCode2() + " ");
        }
        if(groupCourse.getCategoryGroupCode3()!=null){
            System.out.print(groupCourse.getCategoryGroupCode3() + " ");
        }
        if(groupCourse.getCategoryGroupCode4()!=null){
            System.out.print(groupCourse.getCategoryGroupCode4());
        }
        System.out.println();
    }

    // wantedCategory 데이터를 전부 별점순으로 가져오기(내림차순) 테스트(SQL)
    @Test
    void getCategoryPlaceListTest() { // mySql
//        System.out.println(categoryPurposeRepository.findAll());
        System.out.println(categoryPurposeRepository.findById("수영장"));
        //given(어떤 데이터가 있을때)
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)
    }
    // 카테고리를 돌려보면서 하나하나 반경 기준으로 가져오기 테스트(SQL)
    @Test
    void getFinalCourseTest() { //- 몽고디비
        List<User> userList = userRepository.findAll();
        for(User user: userList){
            System.out.println(user.toString());
        }
        System.out.println();

        List<UserMark> userMarkList = userMarkRepository.findAll();

        for(UserMark userMark : userMarkList) {
            System.out.println(userMark.toString());
        }
        System.out.println();

        List<History> historyList = historyRepository.findAll();
        for(History history : historyList) {
            System.out.println(history.toString());
            System.out.println(history.getCourse().size());
            for (int i=0; i<history.getCourse().size(); i++) {
                System.out.println("history_getCourse().get("+i+").getCourseId() = " + history.getCourse().get(i).getCourseId());
                System.out.println("history_getCourse().get("+i+").getPlaceIds() = " + history.getCourse().get(i).getPlaceIds());
                System.out.println("history_getCourse().get("+i+").getRating() = " + history.getCourse().get(i).getRating());
            }
        }
        //given(어떤 데이터가 있을때)
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)
    }
    // 음식점 개수 구하는 메소드 유닛 테스트 -> private이라 따로 해보기
    @Test
    void getRestaurantNumTest() {
        //given(어떤 데이터가 있을때)
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)
    }
    // 파티션 구하기 메소드 유닛 테스트 -> private이라 따로 해보기
    @Test
    void getPartitionTest() {
        //given(어떤 데이터가 있을때)
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)
    }
    // 최적화 알고리즘 유닛 테스트 -> private이라 따로 해보기
    @Test
    void optimizeCourseTest() {
        //given(어떤 데이터가 있을때)
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)
    }
}