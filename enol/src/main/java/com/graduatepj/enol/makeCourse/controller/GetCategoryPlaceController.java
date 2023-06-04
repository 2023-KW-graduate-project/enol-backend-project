package com.graduatepj.enol.makeCourse.controller;

import com.graduatepj.enol.makeCourse.dto.CategoryNameDto;
import com.graduatepj.enol.makeCourse.dto.PlaceDto;
import com.graduatepj.enol.makeCourse.service.GetCategoryPlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/categoryPlace")
public class GetCategoryPlaceController {

    private final GetCategoryPlaceService getCategoryPlaceService;

    @PostMapping
    public ResponseEntity<List<PlaceDto>> getCategoryPlace(@RequestBody CategoryNameDto categoryName){
        return ResponseEntity.ok(getCategoryPlaceService.getCategoryPlace(categoryName.getCategoryName()));
    }


}
