package com.graduatepj.enol.member.dto;

import com.graduatepj.enol.makeCourse.dto.CourseDto;
import com.graduatepj.enol.makeCourse.vo.CourseV2;
import com.graduatepj.enol.member.vo.UserPreference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserPreferenceDto {
    private double preferFatigue;
    private double preferSpecificity;
    private double preferActivity;

    public static UserPreferenceDto from(UserPreference userPreference) {
        return UserPreferenceDto.builder()
                .preferFatigue(userPreference.getPreferFatigue())
                .preferSpecificity(userPreference.getPreferSpecificity())
                .preferActivity(userPreference.getPreferActivity())
                .build();
    }
}
