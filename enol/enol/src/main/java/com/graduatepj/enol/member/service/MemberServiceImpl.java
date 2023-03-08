package com.graduatepj.enol.member.service;

import com.graduatepj.enol.member.vo.MemberRepository;
import com.graduatepj.enol.member.vo.Member;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("memberService")
@Slf4j
public class MemberServiceImpl implements MemberService<Member>{
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class); // 로거 띄우기 위해

    @Autowired
    private MemberRepository memberRepository; // jpa 사용할 repository

    /**
     * 회원가입 메서드
     * @param joinMember
     */
    @Override
    public boolean joinUser(Member joinMember) {
        logger.info("joinUser Serviceimpl START");

        String memberId = joinMember.getMemberId();
        String password = joinMember.getPassword();
        String memberName = joinMember.getMemberName();

        logger.info("joinMember.getMemberId = {}", joinMember.getMemberId());
        logger.info("joinMember.getPassword = {}", joinMember.getPassword());
        logger.info("joinMember.getMemberName = {}", joinMember.getMemberName());

        logger.info("memberRepository.existsById(memberId) = {}", memberRepository.existsById(memberId));
        //회원 ID 중복 확인
        if(memberRepository.existsById(memberId)){ // 중복인 경우
            logger.info("this is duplicated id - ERROR");
            return false;
        }

        // 중복되지 않은 경우 memberRepository에 저장하고 반환
//        String encPassword = passwordEncoder.encode(password); // 암호화한 패스워드

        Member member = new Member();
        member.setMemberId(memberId);
        member.setPassword(password);
        member.setMemberName(memberName);
        memberRepository.save(member);
        logger.info("No duplicated id, save memberRepository");
        return true;

    }

}
