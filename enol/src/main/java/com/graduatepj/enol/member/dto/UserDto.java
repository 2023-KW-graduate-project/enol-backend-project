package com.graduatepj.enol.member.dto;

import com.graduatepj.enol.member.vo.User;
import com.graduatepj.enol.member.vo.UserPreference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDto {
    private String id;
    private String name;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
