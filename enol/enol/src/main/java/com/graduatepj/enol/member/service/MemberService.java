package com.graduatepj.enol.member.service;

public interface MemberService<Member> {

    /**
     * 회원 가입 service
     * @param member
     * @return
     */
    public abstract boolean joinUser(Member member);


}

