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

public interface CoursePurposeRepository extends JpaRepository<CategoryPurpose, String> {

//    boolean findByCategoryGroupCodeOrWalkOrDrinkOrExperienceOrHealingOrWatchOrIntellectualOrViewOrNormalOrSportsOrSolo(String categoryGroupCode, Integer walk, Integer drink, Integer experience, Integer healing, Integer watch, Integer intellectual, Integer view, Integer normal, Integer sports, Integer solo);

//    List<CourseDto> findByCategoryGroupCodeOrWalkOrDrinkOrExperienceOrHealingOrWatchOrIntellectualOrViewOrNormalOrSportsOrSolo(String categoryGroupCode, Integer walk, Integer drink, Integer experience, Integer healing, Integer watch, Integer intellectual, Integer view, Integer normal, Integer sports, Integer solo);

    // CategoryGroupCode는 같고 나머지 목적들 중 하나라도 만족하는 CategoryGroupCode List를 반환하는 메서드
//    List<String> findByCategoryGroupCodeOrWalkOrDrinkOrExperienceOrHealingOrWatchOrIntellectualOrViewOrNormalOrSportsOrSolo(String categoryGroupCode, Integer walk, Integer drink, Integer experience, Integer healing, Integer watch, Integer intellectual, Integer view, Integer normal, Integer sports, Integer solo);
//
//
//    @Query("SELECT c FROM CoursePurpose c WHERE c.category_group_code=:categoryGroupCode and c.산책=:walk or c.음주=:drink or c.체험=:experience or c.힐링=:healing or c.관람=:watch or c.지적활동=:intellectual or c.경치=:view or c.일반=:normal or c.스포츠=:sports or c.솔로=:solo")
//    List<CourseDto> findAllByGoalIn(@Param("category_group_code") String categoryGroupCode, @Param("walk") Integer walk, @Param("drink") Integer drink, @Param("experience") Integer experience, @Param("healing") Integer healing, @Param("watch") Integer watch, @Param("intellectual") Integer intellectual, @Param("view") Integer view, @Param("normal") Integer normal, @Param("sports") Integer sports, @Param("solo") Integer solo);
//
//    List<CategoryPurpose> findByCategoryGroupCode(String categoryGroupCode);
//
//    @Query("select cp.walk, cp.drink, cp.experience, cp.healing, cp.watch, cp.watch, cp.intellectual, cp.view, cp.normal, cp.sports, cp.solo from CoursePurpose cp where cp.categoryGroupCode = :categoryGroupCode")
//    List<Integer> findWalkDrinkExperienceHealingWatchIntellectualViewNormalSportsSoloByCategoryGroupCode(@Param("categoryGroupCode") String categoryGroupCode);
//
//
//    List<String> findCategoryCodeByCategoryGroupCodeAnd(String categoryGroupCode, Integer walk, Integer drink, Integer experience, Integer healing, Integer watch, Integer intellectual, Integer view, Integer normal, Integer sports, Integer solo);

}
