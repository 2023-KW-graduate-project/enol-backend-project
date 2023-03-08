package com.graduatepj.enol.member.controller;

import com.graduatepj.enol.member.service.MemberService;
import com.graduatepj.enol.member.vo.Member;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {
    // 고객 정보 수정, 비밀번호 변경, 회원가입, 상세정보 조회, 사용자 삭제
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class); // 로거 띄우기 위해

    @Resource(name = "memberService")
    private MemberService memberService;

    /**
     * 회원 가입 페이지
     * @return
     */
    @PostMapping("/join")
    public String joinMember(Member member) {
        logger.info("MemberController.join START");

        logger.info("memberId = {}", member.getMemberId());
        logger.info("memberPassword = {}", member.getPassword());
        logger.info("memberName = {}", member.getMemberName());
        boolean joinSuccess = memberService.joinUser(member);
        if(joinSuccess) {
            logger.info("MemberController.join Success!!!");
            return String.format("아이디: %s, 비밀번호 %s, 이름: %s", member.getMemberId(), member.getPassword(), member.getMemberName());
        }
        else {
            return "중복된 ID입니다. 다른 ID로 회원가입 하세요.";
        }
    }


}
