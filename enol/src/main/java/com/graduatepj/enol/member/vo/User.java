package com.graduatepj.enol.member.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "user")
public class User {
    @Id
    private ObjectId mongoId;
//    @Column(name = "user_code")
    private String userCode;
//    @Column(name = "id")
    private String Id;
//    @Column(name = "pw")
    private String pw;
//    @Column(name = "name")
    @Column(name = "name")
    private String name;
//    @Column(name = "address_name")
    private String addressName;
//    @Column(name = "email")
    private String email;
//    @Column(name = "birth_Date")
    private String birthDate;
//    @Column(name = "join_date")
    private String joinDate;
//    @Column(name = "last_date")
    private String lastDate;

//    @Column(name = "pref_fatigue")
    private double prefFatigue;
//    @Column(name = "pref_activity")
    private double prefUnique;
//    @Column(name = "gender")
    private String gender;

//    @Column(name = "pref_activity")
    private double prefActivity;
}
