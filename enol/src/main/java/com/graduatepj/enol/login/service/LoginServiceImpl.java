package com.graduatepj.enol.login.service;

import com.graduatepj.enol.member.vo.Member;
import com.graduatepj.enol.member.dao.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("loginService")
@Slf4j
public class LoginServiceImpl implements LoginService<Member>{

//    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private MemberRepository memberRepository; // jpa 사용할 repository

    /**
     * 로그인 서비스 메서드
     * @param inputMember
     * @return
     */
//    @Override
//    public boolean Login(Member inputMember) {
//        log.info("login serviceImpl START");
//
//        String memberId = inputMember.getMemberId();
//        String password = inputMember.getPassword();
//        String memberName = inputMember.getMemberName();
//
//        log.info("inputMember.getMemberId={}", memberId);
//        log.info("inputMember.getPassword={}", password);
//        log.info("inputMember.getMemberName={}", memberName);
//
//        log.info("memberRepository.existsById(memberId) = {}", memberRepository.existsById(memberId));
//        if(memberRepository.existsById(memberId)) { // 해당 id가 존재하면 password 맞춰봐야 함
//            log.info("inputMember id exist");
//            Optional<Member> checkMember = memberRepository.findById(memberId);
//            log.info("checkMember.isPresent() = {}", checkMember.isPresent());
//
//            if(checkMember.isPresent()) {
//                Member member = checkMember.get();
//
//                log.info("memberId={}", inputMember.getMemberId());
//                log.info("Password={}", inputMember.getPassword());
//                log.info("memberName={}", inputMember.getMemberName());
//
//                if(member.getPassword().equals(password)) {
//                    log.info("login success in LoginServiceimpl");
//                    return true;
//                }
//                else {
//                    log.info("login fail in LoginServiceimpl");
//                    return false;
//                }
//
//
//            }
//        }
//        log.info("login serviceImpl CLOSE");
//        return false;
//
//    }

    /**
     * 로그인 메서드
     * @param member
     * @return
     */
    @Override
    public Member Login(Member member) {
        log.info("login serviceImpl START");

        log.info("input memberId={}", member.getMemberId());
        log.info("input password={}", member.getPassword());

        log.info("memberRepository.existsById(memberId) = {}", memberRepository.existsById(member.getMemberId()));
        if(memberRepository.existsById(member.getMemberId())) { // 해당 id가 존재하면 password 맞춰봐야 함
            log.info("inputMember id exist");
            Optional<Member> checkMember = memberRepository.findById(member.getMemberId());
            log.info("checkMember.isPresent() = {}", checkMember.isPresent());

            if(checkMember.isPresent()) {
                Member loginMember = checkMember.get();

                log.info("memberId={}", loginMember.getMemberId());
                log.info("Password={}", loginMember.getPassword());
//                log.info("EnPassword={}", loginMember.getEnpassword());
                log.info("memberName={}", loginMember.getMemberName());
                log.info("Email={}", loginMember.getEmail());
                log.info("BirthDay={}", loginMember.getBirthday());
                log.info("gender={}", loginMember.getGender());

                if(loginMember.getPassword().equals(member.getPassword())) { // password까지 맞는 경우 로그인 성공 - json으로 객체 던져줌
                    log.info("login success in LoginServiceimpl");
                    return checkMember.get();
                }
                else { // password 틀린 경우 로그인 실패 - null값 던지기
                    log.info("login password fail in LoginServiceimpl");
                    return null;
                }


            }
        }
        else {
            log.info("ID fail in loginServiceImpl");
            return null;

        }

        log.info("loginServiceImpl Close - ERROR");
        return null;
    }
}
