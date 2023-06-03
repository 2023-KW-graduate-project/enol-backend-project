package com.graduatepj.enol.login.service;

import com.graduatepj.enol.member.dto.UserDto;
import com.graduatepj.enol.member.vo.User;

public interface LoginService<Member> {
    /**
     * 로그인 페이지
     */
//    public abstract boolean Login(Member member);
    public abstract User Login(UserDto userDto);

}
