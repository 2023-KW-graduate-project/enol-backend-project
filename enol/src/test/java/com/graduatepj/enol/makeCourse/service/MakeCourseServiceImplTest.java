package com.graduatepj.enol.makeCourse.service;
import com.graduatepj.enol.makeCourse.dao.*;
import com.graduatepj.enol.makeCourse.dto.CourseDto;
import com.graduatepj.enol.makeCourse.dto.CourseRequest;
import com.graduatepj.enol.makeCourse.dto.SecondCourse;
import com.graduatepj.enol.makeCourse.vo.CourseV2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
@SpringBootTest
class MakeCourseServiceImplTest {
    @Autowired
    private CourseRepository courseRepository;
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
    private CategoryPurpose1Repository categoryPurpose1Repository;
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


        MakeCourseServiceImpl makeCourseServiceImpl = new MakeCourseServiceImpl(courseRepository, courseMemberRepository, placeRepository, categoryRepository, courseV2Repository, categoryPurposeRepository, categoryPurpose1Repository);

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
        courseRequest.setStartTime(21);
        courseRequest.setFinishTime(24);
        courseRequest.setWantedCategoryGroup(null);
        courseRequest.setWantedCategory(null);
        courseRequest.setWantedCategoryGroup("EM1");
        courseRequest.setWantedCategory("CB");
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
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)
    }
    // finalCourse 통합 테스트
    @Test
    void finalCourseTest() {
        //given(어떤 데이터가 있을때)
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)
        // 모든 카테고리 그룹 코스 가져오기
        List<CourseV2> groupCourseList = courseV2Repository.findAll();
        for(CourseV2 groupCourse : groupCourseList){
            List<String> detailCategories1 = new ArrayList<>();
            List<String> detailCategories2 = new ArrayList<>();
            List<String> detailCategories3 = new ArrayList<>();
            List<String> detailCategories4 = new ArrayList<>();
            List<String> detailCategorieCodes = new ArrayList<>();
            SecondCourse secondCourse = new SecondCourse();

            // 카테고리 그룹 코드에 해당하는 세부 카테고리 코드 리스트를 가져오기
            if(groupCourse.getCategoryGroupCode1()!=null){
                detailCategories1=categoryRepository.findCategoryCodeByCategoryGroupCode(groupCourse.getCategoryGroupCode1());
            }
            if(groupCourse.getCategoryGroupCode2()!=null){
                detailCategories2=categoryRepository.findCategoryCodeByCategoryGroupCode(groupCourse.getCategoryGroupCode2());
            }
            if(groupCourse.getCategoryGroupCode3()!=null){
                detailCategories3=categoryRepository.findCategoryCodeByCategoryGroupCode(groupCourse.getCategoryGroupCode3());
            }
            if(groupCourse.getCategoryGroupCode4()!=null){
                detailCategories4=categoryRepository.findCategoryCodeByCategoryGroupCode(groupCourse.getCategoryGroupCode4());
            }

            for (int s = 6; s <= 25; s++) {
                for (int e = 7; e <= 28; e++) {
                    System.out.println("startTime = " + s + "endTime = " + e);
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
                                                            .wantedCategoryCode(detailCategories1.get(i))
                                                            .detailCategoryCodes(detailCategorieCodes)
                                                            .startTime(s)
                                                            .endTime(e)
                                                            .mealCheck(true)
                                                            .build();
                                                    // 식사포함
                                                    System.out.println("4개 코스 | 1번 : " + secondCourse.getDetailCategoryCodes().get(0) +
                                                            " | 2번 : " + secondCourse.getDetailCategoryCodes().get(1) +
                                                            " | 3번 : " + secondCourse.getDetailCategoryCodes().get(2) +
                                                            " | 4번 : " + secondCourse.getDetailCategoryCodes().get(3) +
                                                            " | 식사여부 : " + secondCourse.getMealCheck() +
                                                            " | 필수 카테고리 : " + secondCourse.getWantedCategoryCode());
                                                    makeCourseService.finalCourseFiltering(secondCourse);
                                                    // 식사미포함
                                                    secondCourse.setMealCheck(false);
                                                    System.out.println("4개 코스 | 1번 : " + secondCourse.getDetailCategoryCodes().get(0) +
                                                            " | 2번 : " + secondCourse.getDetailCategoryCodes().get(1) +
                                                            " | 3번 : " + secondCourse.getDetailCategoryCodes().get(2) +
                                                            " | 4번 : " + secondCourse.getDetailCategoryCodes().get(3) +
                                                            " | 식사여부 : " + secondCourse.getMealCheck() +
                                                            " | 필수 카테고리 : " + secondCourse.getWantedCategoryCode());
                                                    makeCourseService.finalCourseFiltering(secondCourse);
                                                }
                                            }else{ // 3개 코스
                                                secondCourse = SecondCourse.builder()
                                                        .wantedCategoryCode(detailCategories1.get(i))
                                                        .detailCategoryCodes(detailCategorieCodes)
                                                        .startTime(s)
                                                        .endTime(e)
                                                        .mealCheck(true)
                                                        .build();
                                                // 식사포함
                                                System.out.println("3개 코스 | 1번 : " + secondCourse.getDetailCategoryCodes().get(0) +
                                                        " | 2번 : " + secondCourse.getDetailCategoryCodes().get(1) +
                                                        " | 3번 : " + secondCourse.getDetailCategoryCodes().get(2) +
                                                        " | 식사여부 : " + secondCourse.getMealCheck() +
                                                        " | 필수 카테고리 : " + secondCourse.getWantedCategoryCode());
                                                makeCourseService.finalCourseFiltering(secondCourse);
                                                // 식사미포함
                                                secondCourse.setMealCheck(false);
                                                System.out.println("3개 코스 | 1번 : " + secondCourse.getDetailCategoryCodes().get(0) +
                                                        " | 2번 : " + secondCourse.getDetailCategoryCodes().get(1) +
                                                        " | 3번 : " + secondCourse.getDetailCategoryCodes().get(2) +
                                                        " | 식사여부 : " + secondCourse.getMealCheck() +
                                                        " | 필수 카테고리 : " + secondCourse.getWantedCategoryCode());
                                                makeCourseService.finalCourseFiltering(secondCourse);
                                            }
                                        }
                                    }else{ // 2개 코스
                                        secondCourse = SecondCourse.builder()
                                                .wantedCategoryCode(detailCategories1.get(i))
                                                .detailCategoryCodes(detailCategorieCodes)
                                                .startTime(s)
                                                .endTime(e)
                                                .mealCheck(true)
                                                .build();
                                        // 식사포함
                                        System.out.println("2개 코스 | 1번 : " + secondCourse.getDetailCategoryCodes().get(0) +
                                                " | 2번 : " + secondCourse.getDetailCategoryCodes().get(1) +
                                                " | 식사여부 : " + secondCourse.getMealCheck() +
                                                " | 필수 카테고리 : " + secondCourse.getWantedCategoryCode());
                                        makeCourseService.finalCourseFiltering(secondCourse);
                                        // 식사미포함
                                        secondCourse.setMealCheck(false);
                                        System.out.println("2개 코스 | 1번 : " + secondCourse.getDetailCategoryCodes().get(0) +
                                                " | 2번 : " + secondCourse.getDetailCategoryCodes().get(1) +
                                                " | 식사여부 : " + secondCourse.getMealCheck() +
                                                " | 필수 카테고리 : " + secondCourse.getWantedCategoryCode());
                                        makeCourseService.finalCourseFiltering(secondCourse);
                                    }
                                }
                            }else{ // 1개 코스
                                secondCourse = SecondCourse.builder()
                                        .wantedCategoryCode(detailCategories1.get(i))
                                        .detailCategoryCodes(detailCategorieCodes)
                                        .startTime(s)
                                        .endTime(e)
                                        .mealCheck(true)
                                        .build();
                                // 식사포함
                                System.out.println("1개 코스 | 1번 : " + secondCourse.getDetailCategoryCodes().get(0) +
                                        " | 식사여부 : " + secondCourse.getMealCheck() +
                                        " | 필수 카테고리 : " + secondCourse.getWantedCategoryCode());
                                makeCourseService.finalCourseFiltering(secondCourse);
                                // 식사미포함
                                secondCourse.setMealCheck(false);
                                System.out.println("1개 코스 | 1번 : " + secondCourse.getDetailCategoryCodes().get(0) +
                                        " | 식사여부 : " + secondCourse.getMealCheck() +
                                        " | 필수 카테고리 : " + secondCourse.getWantedCategoryCode());
                                makeCourseService.finalCourseFiltering(secondCourse);
                            }
                        }
                    }
                }
            }



        }




    }

    // wantedCategory 데이터를 전부 별점순으로 가져오기(내림차순) 테스트(SQL)
    @Test
    void getCategoryPlaceListTest() {
        //given(어떤 데이터가 있을때)
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)
    }
    // 카테고리를 돌려보면서 하나하나 반경 기준으로 가져오기 테스트(SQL)
    @Test
    void getFinalCourseTest() {
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