package com.graduatepj.enol.member.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_mark")
public class UserMark {
    @Id
    private String userCode;
    private List<String> friendCodes;
    private List<String> placeIds;
    private List<String> courseIds;
}
