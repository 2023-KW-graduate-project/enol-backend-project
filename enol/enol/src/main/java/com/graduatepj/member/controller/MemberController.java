package com.graduatepj.member.controller;

import com.graduatepj.login.controller.LoginController;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {
    // 고객 정보 수정, 비밀번호 변경, 회원가입, 상세정보 조회, 사용자 삭제
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class); // 로거 띄우기 위해


    /**
     * 회원 가입 페이지
     * @return
     */
    @PostMapping("/signup")
    public String signup() {
        return "signup";
    }


}
