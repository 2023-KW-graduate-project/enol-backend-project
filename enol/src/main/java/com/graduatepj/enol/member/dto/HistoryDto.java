package com.graduatepj.enol.member.dto;

import com.graduatepj.enol.member.vo.History;
import com.graduatepj.enol.member.vo.UserPreference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class HistoryDto {
    private int number;

    // 안에는 courseId, categoryCode, rating 순으로 있음
    private List<String> course;

    public static HistoryDto from(History history) {
        return HistoryDto.builder()
                .number(history.getNumber())
                .course(history.getCourse())
                .build();
    }
}
