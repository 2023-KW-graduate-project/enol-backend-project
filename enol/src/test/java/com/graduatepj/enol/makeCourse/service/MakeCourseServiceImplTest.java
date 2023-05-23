package com.graduatepj.enol.makeCourse.service;

import com.graduatepj.enol.makeCourse.dao.CourseMemberRepository;
import com.graduatepj.enol.makeCourse.dao.CourseRepository;
import com.graduatepj.enol.makeCourse.dao.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Autowired
//    private Course1Repository course1Repository;
//    @Autowired
//    private Course2Repository course2Repository;
//    @Autowired
//    private Course3Repository course3Repository;
//    @Autowired
//    private Course4Repository course4Repository;
//    @Autowired
//    private CourseV2Repository courseV2Repository;


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