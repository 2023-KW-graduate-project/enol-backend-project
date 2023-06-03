package com.graduatepj.enol.member.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDto {
    private String userCode;
    private String friendCode;
}
