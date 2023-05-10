package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.vo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    List<String> findCategoryCodeByCategoryGroupCode(String categoryGroupCode);
}
