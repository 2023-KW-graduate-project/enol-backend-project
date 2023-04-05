package com.graduatepj.enol.login.controller;

import com.graduatepj.enol.login.service.LoginService;
import com.graduatepj.enol.member.vo.Member;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @param memberId
     * @param password
     * @return
     */
    @GetMapping("/login")
    public Member login(@RequestParam String memberId, @RequestParam String password) {
        log.info("LoginController.login START");
        log.info("memberId = {}", memberId);
        log.info("memberPassword = {}", password);

        Member loginMember = loginService.Login(memberId, password);

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
