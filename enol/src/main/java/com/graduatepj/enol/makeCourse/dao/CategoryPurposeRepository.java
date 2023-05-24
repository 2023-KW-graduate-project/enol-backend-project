package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.dto.CourseDto;
import com.graduatepj.enol.makeCourse.vo.CategoryPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public interface CategoryPurposeRepository extends JpaRepository<CategoryPurpose, String> {

//    boolean findByCategoryGroupCodeOrWalkOrDrinkOrExperienceOrHealingOrWatchOrIntellectualOrViewOrNormalOrSportsOrSolo(String categoryGroupCode, Integer walk, Integer drink, Integer experience, Integer healing, Integer watch, Integer intellectual, Integer view, Integer normal, Integer sports, Integer solo);

//    List<CourseDto> findByCategoryGroupCodeOrWalkOrDrinkOrExperienceOrHealingOrWatchOrIntellectualOrViewOrNormalOrSportsOrSolo(String categoryGroupCode, Integer walk, Integer drink, Integer experience, Integer healing, Integer watch, Integer intellectual, Integer view, Integer normal, Integer sports, Integer solo);

    // CategoryGroupCode는 같고 나머지 목적들 중 하나라도 만족하는 CategoryGroupCode List를 반환하는 메서드 - 확인
    @Query(value = "select cp.category_group_code from Category_Purpose cp where (cp.산책=:walk OR cp.음주=:drink OR cp.체험=:experience OR cp.힐링=:healing OR cp.관람=:watch OR cp.지적활동=:intellectual OR cp.경치=:view OR cp.일반=:normal OR cp.스포츠=:sports OR cp.솔로=:solo) AND cp.category_group_code=:categoryGroupCode ", nativeQuery = true)
    List<String> findCategoryGroupCodeByCategoryGroupCodeAndWalkOrDrinkOrExperienceOrHealingOrWatchOrIntellectualOrViewOrNormalOrSportsOrSolo(@Param("categoryGroupCode") String categoryGroupCode, @Param("walk") Integer walk, @Param("drink") Integer drink, @Param("experience") Integer experience, @Param("healing") Integer healing, @Param("watch") Integer watch, @Param("intellectual") Integer intellectual, @Param("view") Integer view, @Param("normal") Integer normal, @Param("sports") Integer sports, @Param("solo") Integer solo);


//    @Query("SELECT c FROM CoursePurpose c WHERE c.category_group_code=:categoryGroupCode and c.산책=:walk or c.음주=:drink or c.체험=:experience or c.힐링=:healing or c.관람=:watch or c.지적활동=:intellectual or c.경치=:view or c.일반=:normal or c.스포츠=:sports or c.솔로=:solo")
//    List<CourseDto> findAllByGoalIn(@Param("categoryGroupCode") String categoryGroupCode, @Param("walk") Integer walk, @Param("drink") Integer drink, @Param("experience") Integer experience, @Param("healing") Integer healing, @Param("watch") Integer watch, @Param("intellectual") Integer intellectual, @Param("view") Integer view, @Param("normal") Integer normal, @Param("sports") Integer sports, @Param("solo") Integer solo);
//
//    List<CategoryPurpose> findByCategoryGroupCode(String categoryGroupCode);


//    @Query(value = "select cp.산책, cp.음주, cp.체험, cp.힐링, cp.관람, cp.지적활동, cp.경치, cp.일반, cp.스포츠, cp.솔로 from Category_Purpose cp where cp.category_group_code=:categoryGroupCode", nativeQuery = true)
//    List<Integer> findWalkDrinkExperienceHealingWatchIntellectualViewNormalSportsSoloByCategoryGroupCode(@Param("categoryGroupCode") String categoryGroupCode);

    // 목적이 1인 경우에만 같은지 비교하는 쿼리문 - 확인
    @Query(value = "select cp.category_code from category_purpose cp where cp.category_group_code=:categoryGroupCode AND ((:walk=1 AND cp.산책=1) OR (:drink=1 AND cp.음주=1) OR (:experience=1 AND cp.체험=1) OR (:healing=1 AND cp.힐링=1) OR (:watch=1 AND cp.관람=1) OR (:intellectual=1 AND cp.지적활동=1) OR (:view=1 AND cp.경치=1) OR (:normal=1 AND cp.일반=1) OR (:sports=1 AND cp.스포츠=1) OR (:solo=1 AND cp.솔로=1))", nativeQuery=true)
    List<String> findCategoryCodeByCategoryGroupCode(@Param("categoryGroupCode") String categoryGroupCode, @Param("walk") Integer walk, @Param("drink") Integer drink, @Param("experience") Integer experience, @Param("healing") Integer healing, @Param("watch") Integer watch, @Param("intellectual") Integer intellectual, @Param("view") Integer view, @Param("normal") Integer normal, @Param("sports") Integer sports, @Param("solo") Integer solo);


    // 카테고리 그룹이 맞고 한 개라도 목적이 1인 CategoryPurpose를 가져오는 메서드 - 목적들만 리스트로 가져오는 메서드가 안돼서 이걸로 수정 - 확인
    @Query(value = "select * from Category_Purpose cp where cp.category_group_code=:categoryGroupCode AND ((:walk=1 AND cp.산책=1) OR (:drink=1 AND cp.음주=1) OR (:experience=1 AND cp.체험=1) OR (:healing=1 AND cp.힐링=1) OR (:watch=1 AND cp.관람=1) OR (:intellectual=1 AND cp.지적활동=1) OR (:view=1 AND cp.경치=1) OR (:normal=1 AND cp.일반=1) OR (:sports=1 AND cp.스포츠=1) OR (:solo=1 AND cp.솔로=1))", nativeQuery = true)
    List<CategoryPurpose> findByCategoryGroupCode(@Param("categoryGroupCode") String categoryGroupCode, @Param("walk") Integer walk, @Param("drink") Integer drink, @Param("experience") Integer experience, @Param("healing") Integer healing, @Param("watch") Integer watch, @Param("intellectual") Integer intellectual, @Param("view") Integer view, @Param("normal") Integer normal, @Param("sports") Integer sports, @Param("solo") Integer solo);

}
