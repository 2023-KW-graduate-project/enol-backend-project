package com.graduatepj.enol.member.service;

import com.graduatepj.enol.member.dao.HistoryRepository;
import com.graduatepj.enol.member.dao.MemberRepository;
import com.graduatepj.enol.member.dao.UserMarkRepository;
import com.graduatepj.enol.member.dao.UserRepository;
import com.graduatepj.enol.member.vo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceImplTest {
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private UserMarkRepository userMarkRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberService memberService;

    @Test
    void historyRepositoryTest(){
        //given(어떤 데이터가 있을때)
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)
    }

    @Test
    void memberRepositoryTest(){
        //given(어떤 데이터가 있을때)
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)
    }

    @Test
    void userMarkRepositoryTest(){
        //given(어떤 데이터가 있을때)
        //when(어떤 동작을 하게되면)
        //then(어떤 결과가 나와야한다)
    }

    @Test
    void userRepositoryTest(){
        List<User> userList = userRepository.findAll();
        for(User user: userList){
            System.out.println(user.toString());
        }
    }


}