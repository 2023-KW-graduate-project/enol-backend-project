package com.graduatepj.enol.makeCourse.dao;

import com.graduatepj.enol.makeCourse.vo.Place;
import com.graduatepj.enol.makeCourse.vo.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query(value = "SELECT * FROM restaurant WHERE ST_Distance(POINT(:longitude, :latitude), POINT(restaurant.x, restaurant.y)) <= :radius ORDER BY RAND() LIMIT 15", nativeQuery = true)
    List<Restaurant> findRandomRestaurantByLocationAndRadius(@Param("longitude") double longitude, @Param("latitude") double latitude, @Param("radius") double radius);
}
