package com.graduatepj.enol.member.vo;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "user")
public class User {
    @Id
    private String _id;
    @Field("user_code")
    private String userCode;
    @Field("id")
    private String id;
    @Field("pw")
    private String pw;
    @Field("name")
    private String name;
    @Field("address_name")
    private String addressName;
    @Field("email")
    private String email;
    @Field("birth_date")
    private String birthDate;
    @Field("join_date")
    private String joinDate;
    @Field("last_date")
    private String lastDate;

    @Field("pref_fatigue")
    private double prefFatigue;
    @Field("pref_unique")
    private double prefUnique;
    @Field("pref_activity")
    private double prefActivity;
    @Field("gender")
    private String gender;
}
