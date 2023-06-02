package com.graduatepj.enol.member.dto;

import com.graduatepj.enol.member.vo.User;
import com.graduatepj.enol.member.vo.UserMark;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMarkDto {
    private String userCode;
    private List<String> friendCodes;
    private List<Long> placeIds;
    private List<String> courseIds;

    public static UserMarkDto from(UserMark userMark) {
        return UserMarkDto.builder()
                .userCode(userMark.getUserCode())
                .friendCodes(userMark.getFriendCodes())
                .placeIds(userMark.getPlaceIds())
                .courseIds(userMark.getCourseIds())
                .build();
    }
}
