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

//    @Query(value = "SELECT * FROM course_v2 cv WHERE cv.total_time=:totalTime AND " +
//            "cv.total_피로도=:fatigability AND cv.total_특이도=:specification AND cv.total_활동도=:activity " +
    //        "AND ( ( :walk = 1 AND SUM(cp.산책) > 0 ) OR (:drink=1 AND SUM(cp.음주)>0) OR (:experience=1 AND SUM(cp.체험)>0) OR (:healing=1 AND SUM(cp.힐링)>0) "
    //        "OR (:watch=1 AND SUM(cp.관람)>0) OR (:intellectual=1 AND SUM(cp.지적활동)>0) OR (:view=1 AND SUM(cp.경치)>0) OR (:normal=1 AND SUM(cp.일반)>0) "
    //        "OR (:sports=1 AND SUM(cp.스포츠)>0) OR (:solo=1 AND SUM(cp.솔로)>0) FROM category_purpose cp " +
    //        "WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4))"

//            "AND :walk = (SELECT CASE WHEN SUM(cp.산책) > 0 THEN 1 ELSE 0 END FROM category_purpose cp " +
//                "WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4))" +
//            "AND :drink = (SELECT CASE WHEN SUM(cp.음주) > 0 THEN 1 ELSE 0 END FROM category_purpose cp " +
//                "WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4))" +
//            "AND :experience = (SELECT CASE WHEN SUM(cp.체험) > 0 THEN 1 ELSE 0 END FROM category_purpose cp " +
//                "WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4))" +
//            "AND :healing = (SELECT CASE WHEN SUM(cp.힐링) > 0 THEN 1 ELSE 0 END FROM category_purpose cp " +
//                "WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4))" +
//            "AND :watch = (SELECT CASE WHEN SUM(cp.관람) > 0 THEN 1 ELSE 0 END FROM category_purpose cp " +
//                "WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4))" +
//            "AND :intellectual = (SELECT CASE WHEN SUM(cp.지적활동) > 0 THEN 1 ELSE 0 END FROM category_purpose cp " +
//                "WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4))" +
//            "AND :view = (SELECT CASE WHEN SUM(cp.경치) > 0 THEN 1 ELSE 0 END FROM category_purpose cp " +
//                "WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4))" +
//            "AND :normal = (SELECT CASE WHEN SUM(cp.일반) > 0 THEN 1 ELSE 0 END FROM category_purpose cp " +
//                "WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4))" +
//            "AND :sports = (SELECT CASE WHEN SUM(cp.스포츠) > 0 THEN 1 ELSE 0 END FROM category_purpose cp " +
//                "WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4))" +
//            "AND :solo = (SELECT CASE WHEN SUM(cp.솔로) > 0 THEN 1 ELSE 0 END FROM category_purpose cp " +
//                "WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4))"
//                , nativeQuery = true)

    // 태호 쿼리문 - 시간, 목적, 키워드로 필터링 한번에 - 다시 해야 함
//    @Query(value = "SELECT * FROM course_v2 cv WHERE cv.total_time=:totalTime AND " +
//            "cv.total_피로도>:fatigability and total_피로도<:fatigability AND cv.total_특이도=:specification AND cv.total_활동도=:activity " +
//            "AND (:walk, :drink, :experience, :healing, :watch, :intellectual, :view, :normal, :sports, :solo) = " +
//            "(SELECT CASE WHEN SUM(cp.산책) > 0 THEN 1 ELSE 0 END, " +
//            "CASE WHEN SUM(cp.음주) > 0 THEN 1 ELSE 0 END, " +
//            "CASE WHEN SUM(cp.체험) > 0 THEN 1 ELSE 0 END, " +
//            "CASE WHEN SUM(cp.힐링) > 0 THEN 1 ELSE 0 END, " +
//            "CASE WHEN SUM(cp.관람) > 0 THEN 1 ELSE 0 END, " +
//            "CASE WHEN SUM(cp.지적활동) > 0 THEN 1 ELSE 0 END, " +
//            "CASE WHEN SUM(cp.경치) > 0 THEN 1 ELSE 0 END, " +
//            "CASE WHEN SUM(cp.일반) > 0 THEN 1 ELSE 0 END, " +
//            "CASE WHEN SUM(cp.스포츠) > 0 THEN 1 ELSE 0 END, " +
//            "CASE WHEN SUM(cp.솔로) > 0 THEN 1 ELSE 0 END " +
//                "FROM category_purpose cp " +
//                "WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4))"
//                , nativeQuery = true)
//    @Query(value = "SELECT * FROM course_v2 cv WHERE cv.total_time=:totalTime AND " +
//            "(cv.total_피로도>:fatigability AND cv.total_피로도<:fatigability) AND (cv.total_특이도>:specification AND cv.total_특이도<:specification) AND (cv.total_활동도>:activity AND cv.total_활동도<:activity) " +
//            "AND (SELECT SUM(:purpose1+:purpose2+:purpose3) FROM category_purpose cp " +
//            "WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0"
//            , nativeQuery = true)
    // 시간, 키워드, 목적에 해당하는 코스 필터링하는 쿼리 - 확인 - 키워드 부분 수정해야 함 - N보다 크게 작게로 해서 CASE문 쓰던지 해야할듯
    @Query(value = "SELECT * FROM course_v2 cv WHERE cv.total_time=:totalTime AND " +
            "(cv.total_피로도>=:fatigability1 AND cv.total_피로도 < :fatigability2 AND cv.total_특이도>=:specification1 AND cv.total_특이도 < :specification2 AND cv.total_활동도>=:activity1 AND cv.total_활동도<:activity2) " +
            "AND ((:walk=1 AND (SELECT SUM(cp.산책) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
            "OR (:drink=1 AND (SELECT SUM(cp.음주) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
            "OR (:experience=1 AND (SELECT SUM(cp.체험) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
            "OR (:healing=1 AND (SELECT SUM(cp.힐링) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
            "OR (:watch=1 AND (SELECT SUM(cp.관람) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
            "OR (:intellectual=1 AND (SELECT SUM(cp.지적활동) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
            "OR (:view=1 AND (SELECT SUM(cp.경치) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
            "OR (:normal=1 AND (SELECT SUM(cp.일반) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
            "OR (:sports=1 AND (SELECT SUM(cp.스포츠) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0)" +
            "OR (:solo=1 AND (SELECT SUM(cp.솔로) FROM category_purpose cp WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4)) > 0))"

//            "AND ((:walk=1 AND SUM(cp.산책)>0) OR (:drink=1 AND SUM(cp.음주)>0) OR (:experience=1 AND SUM(cp.체험)>0) OR (:healing=1 AND SUM(cp.힐링)>0) " +
//            "OR (:watch=1 AND SUM(cp.관람)>0) OR (:intellectual=1 AND SUM(cp.지적활동)>0) OR (:view=1 AND SUM(cp.경치)>0) OR (:normal=1 AND SUM(cp.일반)>0) " +
//            "OR (:sports=1 AND SUM(cp.스포츠)>0) OR (:solo=1 AND SUM(cp.솔로)>0) FROM category_purpose cp " +
//            "WHERE cp.category_group_code IN (cv.category_group_code_1, cv.category_group_code_2, cv.category_group_code_3, cv.category_group_code_4))"
            , nativeQuery = true)
    List<CourseV2> findCourseByTimeAndGoalsAndKeywords(@Param("totalTime") Integer totalTime, @Param("fatigability1") Integer fatigability1, @Param("fatigability2") Integer fatigability2, @Param("specification1") Integer specification1, @Param("specification2") Integer specification2, @Param("activity1") Integer activity1, @Param("activity2") Integer activity2,
                                                       @Param("walk") Integer walk, @Param("drink") Integer drink, @Param("experience") Integer experience, @Param("healing") Integer healing, @Param("watch") Integer watch, @Param("intellectual") Integer intellectual, @Param("view") Integer view, @Param("normal") Integer normal, @Param("sports") Integer sports, @Param("solo") Integer solo);
}
