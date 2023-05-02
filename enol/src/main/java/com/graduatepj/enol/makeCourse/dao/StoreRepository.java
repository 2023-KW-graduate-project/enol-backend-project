package com.graduatepj.enol.makeCourse.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<String, Long> {
    @Query(value = "SELECT * FROM store WHERE blabla", nativeQuery = true)
    List<String> findStoreById(@Param("name") double name);
}

