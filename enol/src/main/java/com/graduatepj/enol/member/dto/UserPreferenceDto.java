package com.graduatepj.enol.member.dto;

import com.graduatepj.enol.makeCourse.dto.CourseDto;
import com.graduatepj.enol.makeCourse.vo.CourseV2;
import com.graduatepj.enol.member.vo.User;
import com.graduatepj.enol.member.vo.UserPreference;
import lombok.*;

import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceDto {
    private double prefFatigue;
    private double prefSpecificity;
    private double prefActivity;

    public static UserPreferenceDto from(User userPreference) {
        return UserPreferenceDto.builder()
                .prefFatigue(userPreference.getPrefFatigue())
                .prefSpecificity(userPreference.getPrefUnique())
                .prefActivity(userPreference.getPrefActivity())
                .build();
    }
}
