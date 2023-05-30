package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.dto.CourseDto;
import com.graduatepj.enol.makeCourse.vo.CourseV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseV2Repository extends JpaRepository<CourseV2, String> {
    // 확인
    List<CourseV2> findAllByTime(Integer time);


//    // 시간, 키워드, 목적에 해당하는 코스 필터링하는 쿼리 - 확인
//    @Query(value = "SELECT * FROM course_v2 cv WHERE cv.total_time=:totalTime AND " +
//            "(cv.total_피로도>=:fatigability1 AND cv.total_피로도 < :fatigability2 AND cv.total_특이도>=:specification1 AND cv.total_특이도 < :specification2 AND cv.total_활동도>=:activity1 AND cv.total_활동도<:activity2) " +
//            "AND ((:walk=1 AND (SELECT SUM(cp.산책) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
//            "OR (:drink=1 AND (SELECT SUM(cp.음주) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
//            "OR (:experience=1 AND (SELECT SUM(cp.체험) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
//            "OR (:healing=1 AND (SELECT SUM(cp.힐링) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
//            "OR (:watch=1 AND (SELECT SUM(cp.관람) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
//            "OR (:intellectual=1 AND (SELECT SUM(cp.지적활동) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
//            "OR (:view=1 AND (SELECT SUM(cp.경치) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
//            "OR (:normal=1 AND (SELECT SUM(cp.일반) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
//            "OR (:sports=1 AND (SELECT SUM(cp.스포츠) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
//            "OR (:solo=1 AND (SELECT SUM(cp.솔로) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0))"
//            , nativeQuery = true)
//    List<CourseV2> findCourseByTimeAndGoalsAndKeywords(@Param("totalTime") Integer totalTime, @Param("fatigability1") Integer fatigability1, @Param("fatigability2") Integer fatigability2, @Param("specification1") Integer specification1, @Param("specification2") Integer specification2, @Param("activity1") Integer activity1, @Param("activity2") Integer activity2,
//                                                       @Param("walk") Integer walk, @Param("drink") Integer drink, @Param("experience") Integer experience, @Param("healing") Integer healing, @Param("watch") Integer watch, @Param("intellectual") Integer intellectual, @Param("view") Integer view, @Param("normal") Integer normal, @Param("sports") Integer sports, @Param("solo") Integer solo);
//
//    @Query(value = "SELECT * FROM course_v2 cv WHERE " +
//            "(cv.total_피로도>=:fatigability1 AND cv.total_피로도 < :fatigability2 AND cv.total_특이도>=:specification1 AND cv.total_특이도 < :specification2 AND cv.total_활동도>=:activity1 AND cv.total_활동도<:activity2) " +
//            "AND ( ( :walk=1 AND ( ( SELECT SUM(cp.산책) FROM category_purpose cp WHERE cp.category_group_code IN ( cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
//            "OR ( :drink=1 AND ( ( SELECT SUM(cp.음주) FROM category_purpose cp WHERE cp.category_group_code IN ( cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
//            "OR ( :experience=1 AND ( ( SELECT SUM(cp.체험) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
//            "OR ( :healing=1 AND ( ( SELECT SUM(cp.힐링) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
//            "OR ( :watch=1 AND ( ( SELECT SUM(cp.관람) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
//            "OR ( :intellectual=1 AND ( ( SELECT SUM(cp.지적활동) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
//            "OR ( :view=1 AND ( ( SELECT SUM(cp.경치) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
//            "OR ( :normal=1 AND ( ( SELECT SUM(cp.일반) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
//            "OR ( :sports=1 AND ( ( SELECT SUM(cp.스포츠) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
//            "OR ( :solo=1 AND ( ( SELECT SUM(cp.솔로) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) ) )"
//            , nativeQuery = true)
//    List<CourseV2> findCourseByGoalsAndKeywords(@Param("fatigability1") Integer fatigability1, @Param("fatigability2") Integer fatigability2, @Param("specification1") Integer specification1, @Param("specification2") Integer specification2, @Param("activity1") Integer activity1, @Param("activity2") Integer activity2,
//                                                @Param("walk") Integer walk, @Param("drink") Integer drink, @Param("experience") Integer experience, @Param("healing") Integer healing, @Param("watch") Integer watch, @Param("intellectual") Integer intellectual, @Param("view") Integer view, @Param("normal") Integer normal, @Param("sports") Integer sports, @Param("solo") Integer solo);
//


    // 키워드를 없애고 목적을 재정의한 테이블을 쓰는 쿼리문
    @Query(value = "SELECT * FROM course cv WHERE " +
            "( ( :walk=1 AND ( ( SELECT SUM(cp.walk) FROM category_purpose cp WHERE cp.category_group_code IN ( cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
            "OR ( :socializing=1 AND ( ( SELECT SUM(cp.socializing) FROM category_purpose cp WHERE cp.category_group_code IN ( cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
            "OR ( :niceAtmosphere=1 AND ( ( SELECT SUM(cp.nice_atmosphere) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
            "OR ( :healing=1 AND ( ( SELECT SUM(cp.healing) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
            "OR ( :drinking=1 AND ( ( SELECT SUM(cp.drinking) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
            "OR ( :unusual=1 AND ( ( SELECT SUM(cp.unusual) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
            "OR ( :active=1 AND ( ( SELECT SUM(cp.active) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
            "OR ( :daily=1 AND ( ( SELECT SUM(cp.daily) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
            "OR ( :summer=1 AND ( ( SELECT SUM(cp.summer) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) )" +
            "OR ( :culturalLife=1 AND ( ( SELECT SUM(cp.cultural_life) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4 ) ) > 0 ) ) )"
            , nativeQuery = true)
    List<CourseV2> findCourseByGoalsAndKeywords(@Param("walk") Integer walk, @Param("socializing") Integer socializing, @Param("niceAtmosphere") Integer niceAtmosphere, @Param("healing") Integer healing, @Param("drinking") Integer drinking, @Param("unusual") Integer unusual, @Param("active") Integer active, @Param("daily") Integer daily, @Param("summer") Integer summer, @Param("culturalLife") Integer culturalLife);



}
