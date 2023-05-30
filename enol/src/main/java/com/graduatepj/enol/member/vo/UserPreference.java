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
@Document(collection = "user_preference")
public class UserPreference {
    @Id
    private String userCode;
    private double preferFatigue;
    private double preferSpecificity;
    private double preferActivity;
}
