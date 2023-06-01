package com.graduatepj.enol.member.dto;

import com.graduatepj.enol.member.vo.User;
import com.graduatepj.enol.member.vo.UserPreference;
import lombok.*;

import javax.persistence.Id;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String userCode;
    private String id;
    private String pw;
    private String name;
    private String addressName;
    private String email;
    private String birthDate;
    private String gender;
    private double prefFatigue;
    private double prefUnique;
    private double prefActivity;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

}
