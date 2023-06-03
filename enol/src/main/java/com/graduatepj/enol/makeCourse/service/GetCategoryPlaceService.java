package com.graduatepj.enol.makeCourse.service;

import com.graduatepj.enol.makeCourse.dao.PlaceRepository;
import com.graduatepj.enol.makeCourse.dto.PlaceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetCategoryPlaceService {

    private final PlaceRepository placeRepository;

    public List<PlaceDto> getCategoryPlace(String categoryName){
        Pageable pageable = PageRequest.of(0, 15);
        return PlaceDto.fromEntityList(placeRepository.findAllByCategoryNameOrderByRatingDesc(categoryName, pageable));
    }
}
