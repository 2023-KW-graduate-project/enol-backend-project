package com.graduatepj.enol.member.service;

import com.graduatepj.enol.member.dao.UserHistoryRepository;
import com.graduatepj.enol.member.dao.UserMarkRepository;
import com.graduatepj.enol.member.dao.UserRepository;
import com.graduatepj.enol.member.vo.History;
import com.graduatepj.enol.member.vo.User;
import com.graduatepj.enol.member.vo.UserMark;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MemberServiceImplTest {
    @Autowired
    private UserHistoryRepository userHistoryRepository;
    @Autowired
    private UserMarkRepository userMarkRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberService memberService;

    @Test
    void historyRepositoryTest(){
        List<History> historyList = userHistoryRepository.findAll();
        for(History history: historyList){
            System.out.println(history.toString());
        }
    }

    @Test
    void memberRepositoryTest(){
        //given(어떤 데이터가 있을때)
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)
    }

    @Test
    void userMarkRepositoryTest(){
        List<UserMark> userMarkList = userMarkRepository.findAll();
        for(UserMark userMark: userMarkList){
            System.out.println(userMark.toString());
        }
    }

    @Test
    void userRepositoryTest(){
        List<User> userList = userRepository.findAll();
        for(User user: userList){
            System.out.println(user.toString());
        }
    }


}