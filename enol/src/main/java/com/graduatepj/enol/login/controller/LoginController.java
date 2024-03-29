package com.graduatepj.enol.login.controller;

import com.graduatepj.enol.login.service.LoginService;
import com.graduatepj.enol.member.vo.Member;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource(name="loginService")
    private LoginService loginService;

    /**
     * 로그인 페이지
     * @param member
     * @return
     */
        @PostMapping("/login")
        public Member login(@RequestBody Member member) {
        log.info("LoginController.login START");
        log.info("memberId = {}", member.getMemberId());
        log.info("memberPassword = {}", member.getPassword());

        Member loginMember = loginService.Login(member);

        return loginMember;
    }

    /**
     * 에러 페이지
     * @param model
     * @return
     */
    @GetMapping("/error")
    public String loginError(Model model) {
        log.info("LoginController.ERROR START");
        model.addAttribute("loginError", true);
        return "login";
    }



}
