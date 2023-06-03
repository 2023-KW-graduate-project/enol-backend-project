package com.graduatepj.enol.member.vo;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_mark")
public class UserMark {
    @Id
    private String _id;
    @Field("user_code")
    private String userCode;
    @Field("friend_codes")
    private List<String> friendCodes;
    @Field("place_ids")
    private List<Long> placeIds;
    @Field("course_ids")
    private List<String> courseIds;
}
