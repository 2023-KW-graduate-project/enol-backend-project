package com.graduatepj.enol.login.controller;

import com.graduatepj.enol.login.service.LoginService;
import com.graduatepj.enol.member.vo.Member;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class); // 로거 띄우기 위해

    @Resource(name="loginService")
    private LoginService loginService;


    /**
     * 로그인 페이지
     * @param member
     * @return
     */
    @GetMapping("/login")
    public String login(Member member) {
        logger.info("LoginController.login START");
        logger.info("memberId = {}", member.getMemberId());
        logger.info("memberPassword = {}", member.getPassword());
        logger.info("memberName = {}", member.getMemberName());
        boolean loginSuccess = loginService.Login(member);
        
        if(loginSuccess) {
            logger.info("LoginController.login Success!!!");
            return String.format("아이디: %s, 비밀번호 %s, 이름: %s", member.getMemberId(), member.getPassword(), member.getMemberName());
        }
        else {
            return "로그인 실패";
        }
    }

    /**
     * 에러 페이지
     * @param model
     * @return
     */
    @GetMapping("/error")
    public String loginError(Model model) {
        logger.info("LoginController.ERROR START");
        model.addAttribute("loginError", true);
        return "login";
    }



}
