package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.vo.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    // 랜덤순서로 가져오는 쿼리문
    List<Place> findAllByCategoryCode(String categoryCode);
    // 별점순으로 가져오는 쿼리문
    List<Place> findAllByCategoryInOrderByRateDesc(String wantedCategoryCode);

    // 좌표를 기준으로 가장 가까운 categoryCode의 가게 가져오기
    @Query(value = "SELECT * FROM place WHERE place.category_code = :categoryCode ORDER BY ST_Distance(POINT(:longitude, :latitude), POINT(place.x, place.y)) LIMIT 1", nativeQuery = true)
    Place findNearestPlaceByCategoryAndLocation(@Param("categoryCode") String categoryCode, @Param("longitude") double longitude, @Param("latitude") double latitude);

    // 골라진 가게들 중에서 랜덤으로 limit개의 가게를 가져오는 쿼리문
    @Query(value = "SELECT * FROM place WHERE place.category_code = :categoryCode AND ST_Distance(POINT(:longitude, :latitude), POINT(place.x, place.y)) <= :radius ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Place findRandomPlaceByCategoryAndLocationAndRadius(@Param("categoryCode") String categoryCode, @Param("longitude") double longitude, @Param("latitude") double latitude, @Param("radius") double radius);

    // 가게 평점 데이터가 없으므로 일단 보류, 평점 기준으로 하나만 가져오는 쿼리문
    @Query(value = "SELECT * FROM place WHERE place.category_code = :categoryCode AND ST_Distance(POINT(:longitude, :latitude), POINT(place.x, place.y)) <= :radius ORDER BY rating DESC LIMIT :limit", nativeQuery = true)
    List<Place> findHighestRatedPlaceByCategoryAndLocationAndRadius(@Param("categoryCode") String categoryCode, @Param("longitude") double longitude, @Param("latitude") double latitude, @Param("radius") double radius, @Param("limit") int limit);

//    // 골라진 가게들 중에서 랜덤으로 하나의 가게만 가져오는 쿼리문
//    @Query(value = "SELECT * FROM place WHERE place.category_code= (:categoryCode) AND ST_Distance_Sphere(POINT(:longitude, :latitude), ST_Point(x, y)) <= :radius ORDER BY RAND() DESC LIMIT 1", nativeQuery = true)
//    Place findRandomPlaceByCategoryAndLocationAndRadius(String categoryCode, double longitude, double latitude, double radius);
//
//    // 가게 평점 데이터가 없으므로 일단 보류, 평점 기준으로 하나만 가져오는 쿼리문
//    @Query(value = "SELECT * FROM place WHERE place.category_code= (:categoryCode) AND ST_Distance_Sphere(POINT(:longitude, :latitude), ST_Point(x, y)) <= :radius ORDER BY rating DESC LIMIT :limit", nativeQuery = true)
//    List<Place> findHighestRatedPlaceByCategoryAndLocationAndRadius(String categoryCode, double longitude, double latitude, double radius, int limit);

//    @Query(value = "SELECT * FROM stores WHERE " +
//            "ST_Distance_Sphere(POINT(?1, ?2), POINT(stores.longitude, stores.latitude)) <= ?3 " +
//            "AND stores.category = ?4", nativeQuery = true)
//    List<Store> findStoresByLocationAndCategory(double longitude, double latitude, double range, String category);

    // 프론트 테스트용
    @Query(value = "SELECT * FROM place ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Place> findRandomPlaces(int count);
}
