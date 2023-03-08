package com.graduatepj.enol.login.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/loginA")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class); // 로거 띄우기 위해

    /**
     * 로그인 페이지
     * @param request
     * @param model
     * @return
     */
    @GetMapping
    public String login(HttpServletRequest request, Model model) {
        logger.debug("LoginController.login START");

        return "hello";
    }

    /**
     * 에러 페이지
     * @param model
     * @return
     */
    @GetMapping("/error")
    public String loginError(Model model) {
        logger.debug("LoginController.ERROR START");
        model.addAttribute("loginError", true);
        return "login";
    }



}
