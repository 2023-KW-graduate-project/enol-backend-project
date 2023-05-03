package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.vo.DColCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DColCategoryRepository extends JpaRepository<DColCategory, Long> {
    List<DColCategory> findAllByGoal(String goal);
}
