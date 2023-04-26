package com.graduatepj.enol.member.service;

import com.graduatepj.enol.member.vo.Member;

import java.util.List;

public interface MemberService<Member> {

    /**
     * 회원 가입 service
     * @param member
     * @return
     */
    public abstract com.graduatepj.enol.member.vo.Member joinUser(Member member);

//    /**
//     * ID 중복 확인 serivce
//     * @param memberId
//     * @return
//     */
//    public abstract boolean checkDupUser(String memberId);

    /**
     * ID 확인 service
     * @param member
     * @return
     */
    public abstract com.graduatepj.enol.member.vo.Member checkUserId(Member member);

    /**
     * password 찾기 service
     * @param member
     * @return
     */
    public abstract com.graduatepj.enol.member.vo.Member findUserPassword(Member member);

    /**
     * password 바꾸기
     * @param member
     * @return
     */
    public abstract com.graduatepj.enol.member.vo.Member ChangeUserPassword(Member member, String changePW);

    /**
     * 회원 정보 수정
     * @param member
     * @return
     */
    public abstract com.graduatepj.enol.member.vo.Member modifyMemberInfo(Member member);


    /**
     * 회원 정보 삭제
     * @param member
     * @return
     */
    public abstract boolean deleteMember(Member member);

    /**
     * 회원들 정보 보이기
     * @return
     */
    public abstract List<com.graduatepj.enol.member.vo.Member> showMembers();

}

