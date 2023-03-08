package com.graduatepj.enol.login.service;

import com.graduatepj.enol.member.vo.Member;
import com.graduatepj.enol.member.vo.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("loginService")
@Slf4j
public class LoginServiceImpl implements LoginService<Member>{

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private MemberRepository memberRepository; // jpa 사용할 repository

    @Override
    public boolean Login(Member inputMember) {
        logger.info("login serviceImpl START");

        String memberId = inputMember.getMemberId();
        String password = inputMember.getPassword();
        String memberName = inputMember.getMemberName();

        logger.info("inputMember.getMemberId={}", memberId);
        logger.info("inputMember.getPassword={}", password);
        logger.info("inputMember.getMemberName={}", memberName);

        logger.info("memberRepository.existsById(memberId) = {}", memberRepository.existsById(memberId));
        if(memberRepository.existsById(memberId)) { // 해당 id가 존재하면 password 맞춰봐야 함
            logger.info("inputMember id exist");
            Optional<Member> checkMember = memberRepository.findById(memberId);
            logger.info("checkMember.isPresent() = {}", checkMember.isPresent());

            if(checkMember.isPresent()) {
                Member member = checkMember.get();

                logger.info("memberId={}", member.getMemberId());
                logger.info("Password={}", member.getPassword());
                logger.info("memberName={}", member.getMemberName());

                if(member.getPassword().equals(password)) {
                    logger.info("login success in LoginServiceimpl");
                    return true;
                }
                else {
                    logger.info("login fail in LoginServiceimpl");
                    return false;
                }


            }
        }
        logger.info("login serviceImpl CLOSE");
        return false;

    }
}
