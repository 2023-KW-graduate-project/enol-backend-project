package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.vo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    // categorygroupcode에 속하는 categoryName를 보여주는 메서드 - 확인
    @Query(value = "select c.category_name from Category c where c.category_Group_Code=:categoryGroupCode", nativeQuery = true)
    List<String> findCategoryNameByCategoryGroupCode(@Param("categoryGroupCode") String categoryGroupCode);
}
