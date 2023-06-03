package com.graduatepj.enol.member.dto;

import com.graduatepj.enol.member.vo.User;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FriendDto {
    private String userCode;
    private String name;
    private String gender;

    public static FriendDto from(User user){
        return FriendDto.builder()
                .userCode(user.getUserCode())
                .name(user.getName())
                .gender(user.getGender())
                .build();
    }
}
