package com.graduatepj.enol.member.service;

import com.graduatepj.enol.member.dao.UserHistoryRepository;
import com.graduatepj.enol.member.dao.UserMarkRepository;
import com.graduatepj.enol.member.dao.UserRepository;
import com.graduatepj.enol.member.dto.UserPreferenceDto;
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
        String userCode = "최종혁#01";

        List<History> historyList = userHistoryRepository.findAll();
        for(History history: historyList){
            System.out.println(history.toString());
        }

        System.out.println(memberService.getHistoryById(userCode));
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

    @Test
    void preferenceTest(){
        String userCode = "이영원#01";
        System.out.println(UserPreferenceDto.from(userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("getPreferencesById method failed"))));
    }

    @Test
    void userRepositoryTest2(){
        String userCode = "이영원#01";
        String userName = "이영원";
        String email = "관리자1@kw.ac.kr";
        String birthDate = "1997-12-12";
        String gender = "여성";
        String id = "youngwon";

        System.out.println("getPreferenceById");
        System.out.println(UserPreferenceDto.from(userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("getPreferencesById method failed"))));

        System.out.println("findUserCodeByName");
        System.out.println(userRepository.findUserCodeByName(userName));

        System.out.println("findByEmailAndNameAndBirthDateAndGender");
        System.out.println(userRepository.findByEmailAndNameAndBirthDateAndGender(email, userName, birthDate, gender));

        System.out.println("findByIdAndEmailAndNameAndBirthDateAndGender");
        System.out.println(userRepository.findByIdAndEmailAndNameAndBirthDateAndGender(id, email, userName, birthDate, gender));

        System.out.println("findAllById");
        System.out.println(userRepository.findAllById(id));
    }

    @Test
    void userMarkRepositoryTest2(){
        String userCode = "이영원#01";
        String userName = "이영원";
        String email = "관리자1@kw.ac.kr";
        String birthDate = "1997-12-12";
        String gender = "여성";
        String id = "youngwon";

        System.out.println("findFriendCodesById");
        System.out.println(userMarkRepository.findFriendCodesById(userCode));
        List<UserMark> haha = userMarkRepository.findFriendCodesById(userCode);
        for(UserMark ha : haha){
            List<String> courseIds = ha.getCourseIds();
            for(String hah : courseIds){
                System.out.println(hah);
            }
            System.out.println(courseIds);
        }

        System.out.println("findFriendCodesByUserCode");
        System.out.println(userMarkRepository.findUserMarkByUserCode(userCode));

//        System.out.println("findplacesById");
//        System.out.println(userMarkRepository.findplacesById(userCode));
//
//        System.out.println("findPlaceIdsByUserCode");
//        System.out.println(userMarkRepository.findPlaceIdsByUserCode(userCode));
//
//        System.out.println("findCoursesById");
//        System.out.println(userMarkRepository.findCoursesById(userCode));
//
//        System.out.println("findCourseIdsByUserCode");
//        System.out.println(userMarkRepository.findCourseIdsByUserCode(userCode));

    }


}