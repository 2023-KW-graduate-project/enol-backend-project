package com.graduatepj.enol.login.service;

import com.graduatepj.enol.member.dao.UserRepository;
import com.graduatepj.enol.member.dto.UserDto;
import com.graduatepj.enol.member.vo.Member;
import com.graduatepj.enol.member.dao.MemberRepository;
import com.graduatepj.enol.member.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("loginService")
@Slf4j
public class LoginServiceImpl implements LoginService<Member>{

    @Autowired
    private MemberRepository memberRepository; // jpa 사용할 repository

    @Autowired
    private UserRepository userRepository;


    /**
     * 로그인 메서드
     * @param userDto
     * @return
     */
    @Override
    public User Login(UserDto userDto) { // 로그인 성공한 유저의 userCode만 넘기기
        log.info("--- login serviceImpl START ---");

        log.info("userCode = {}", userDto.getUserCode());
        log.info("input memberId={}", userDto.getId());
        log.info("input password={}", userDto.getPw());

        List<User> userList = userRepository.findAllById(userDto.getId()); // id가 같은 모든 리스트 가져오기 - 있으면 1개만 나올 것
        log.info("userList.size = {}", userList.size());
        if (userList.size() == 1) { // 1개만 발견했으면 정상 작동
            if (userList.get(0).getPw().equals(userDto.getPw())) { // 비밀번호까지 맞으면 로그인
                log.info("--- login User ---");
                log.info("userCode = {}", userList.get(0).getUserCode());
                log.info("userID = {}", userList.get(0).getId());
                log.info("userPW = {}", userList.get(0).getPw());
                log.info("user Fatigue = {}", userList.get(0).getPrefFatigue());
                log.info("user Unique = {}", userList.get(0).getPrefUnique());
                log.info("user Activity = {}", userList.get(0).getPrefActivity());
                log.info("-------------------");
                log.info("--- login success in LoginServiceimpl ---");
                log.info("--- return User ---");
                return userList.get(0);
            } else { // 비밀번호 틀렸어도 null 반환
                log.info("--- login password fail in LoginServiceimpl ---");
                return null;
            }
        }
        // 아이디가 1개 이상인 경우 - 이런 경우는 프론트에서 회원가입시 중복 확인 하기 때문에 없을 것
        log.info("--- loginServiceImpl END - ERROR ---");
        return null;
    }
}
