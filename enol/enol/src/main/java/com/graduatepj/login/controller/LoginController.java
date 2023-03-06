package com.graduatepj.login.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class); // 로거 띄우기 위해

    /**
     * 로그인 페이지
     * @param request
     * @param model
     * @return
     */
    @GetMapping()
    public String login(HttpServletRequest request, Model model) {
        return "login";
    }

    /**
     * 에러 페이지
     * @param model
     * @return
     */
    @GetMapping("/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }



}
