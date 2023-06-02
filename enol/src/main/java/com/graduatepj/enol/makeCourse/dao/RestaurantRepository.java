package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.vo.Place;
import com.graduatepj.enol.makeCourse.vo.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query(value = "SELECT * FROM restaurant WHERE ( 6371 * acos( cos( radians(:latitude) ) * cos( radians( y ) ) * cos( radians( x ) - radians(:longitude) ) + sin( radians(:latitude) ) * sin( radians( y ) ) ) ) <= :radius ORDER BY RAND() LIMIT 15", nativeQuery = true)
    List<Restaurant> findRandomRestaurantByLocationAndRadius(@Param("longitude") double longitude, @Param("latitude") double latitude, @Param("radius") double radius);
}
