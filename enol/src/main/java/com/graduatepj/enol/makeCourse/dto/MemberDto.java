package com.graduatepj.enol.makeCourse.dto;

import com.graduatepj.enol.member.vo.Member;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {
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
     * 활동성
     */
    @Column(name = "활동도")
    private int activity;

    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .birthday(member.getBirthday())
                .gender(member.getGender())
                .fatigability(member.getFatigability())
                .specification(member.getSpecification())
                .activity(member.getActivity())
                .build();
    }

}
