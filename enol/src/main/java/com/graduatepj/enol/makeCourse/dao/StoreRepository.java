package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.vo.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByCategoryInOrderByRateDesc(String wantedCategory);

    @Query(value = "SELECT * FROM STORE WHERE STORE.category= (:category) AND ST_Distance_Sphere(POINT(:longitude, :latitude), location) <= :radius ORDER BY rating DESC LIMIT 1", nativeQuery = true)
    Store findHighestRatedStoreByCategoryAndLocationAndRadius(String category, double longitude, double latitude, double radius);

//    @Query(value = "SELECT * FROM stores WHERE " +
//            "ST_Distance_Sphere(POINT(?1, ?2), POINT(stores.longitude, stores.latitude)) <= ?3 " +
//            "AND stores.category = ?4", nativeQuery = true)
//    List<Store> findStoresByLocationAndCategory(double longitude, double latitude, double range, String category);
}