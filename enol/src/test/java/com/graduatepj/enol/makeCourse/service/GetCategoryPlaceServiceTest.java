package com.graduatepj.enol.makeCourse.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GetCategoryPlaceServiceTest {
    @Autowired
    private GetCategoryPlaceService getCategoryPlaceService;

    @Test
    void getCategoryPlaceTest(){
        System.out.println(getCategoryPlaceService.getCategoryPlace("클라이밍").size());
    }
}