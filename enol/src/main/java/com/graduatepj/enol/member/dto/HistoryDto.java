package com.graduatepj.enol.member.dto;

import com.graduatepj.enol.makeCourse.dto.PlaceDto;
import com.graduatepj.enol.member.vo.History;
import com.graduatepj.enol.member.vo.History.HistoryCourse;
import com.graduatepj.enol.member.vo.UserPreference;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDto {
    private int number;

    private List<HistoryCourseDto> course;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HistoryCourseDto {
        private List<Long> placeIds;
        private List<PlaceDto> places;
        private double rating;
    }
}



//    public static HistoryDto from(History history) {
//        return HistoryDto.builder()
//                .number(history.getNumber())
//                .course(history.getCourse())
//                .build();
//    }