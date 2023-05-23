package com.graduatepj.enol.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "MEMBER")
@Entity
public class Member {
    /** 회원가입때 사용자에게 받을 정보 */
    /**
     * 사용자 ID
     */
    @Id
    @Column(name = "MEMBERID")
    private String memberId;

    /**
     * 사용자 이름
     */
    @Column(name = "MEMBERNAME")
    private String memberName;

    /**
     * 비밀번호
     */
    @Column(name = "PASSWORD")
    private String password;

    /**
     * 사용자 이메일
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * 사용자 생일
     */
    @Column(name = "BDAY")
    private String birthday;

    /**
     * 사용자 성별
     */
    @Column(name = "GENDER")
    private String gender;

    /**
     * 피로도
     */
    @Column(name = "피로도")
    private int fatigability;

    /**
     * 특이도
     */
    @Column(name = "특이도")
    private int specification;

    /**
     * 활동도
     */
    @Column(name = "활동도")
    private int activity;

}
