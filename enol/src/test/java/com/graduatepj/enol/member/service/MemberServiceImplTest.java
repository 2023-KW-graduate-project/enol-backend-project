package com.graduatepj.enol.member.service;

import com.graduatepj.enol.login.service.LoginService;
import com.graduatepj.enol.makeCourse.dao.PlaceRepository;
import com.graduatepj.enol.makeCourse.dto.CourseRating;
import com.graduatepj.enol.makeCourse.dto.CourseResponse;
import com.graduatepj.enol.makeCourse.dto.PlaceDto;
import com.graduatepj.enol.makeCourse.service.MakeCourseService;
import com.graduatepj.enol.makeCourse.service.MakeCourseServiceImpl;
import com.graduatepj.enol.member.dao.UserHistoryRepository;
import com.graduatepj.enol.member.dao.UserMarkRepository;
import com.graduatepj.enol.member.dao.UserRepository;
import com.graduatepj.enol.member.dto.FriendRequestDto;
import com.graduatepj.enol.member.dto.UserDto;
import com.graduatepj.enol.member.dto.UserPreferenceDto;
import com.graduatepj.enol.member.vo.History;
import com.graduatepj.enol.member.vo.User;
import com.graduatepj.enol.member.vo.UserMark;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class MemberServiceImplTest {
    @Autowired
    private UserHistoryRepository userHistoryRepository;
    @Autowired
    private UserMarkRepository userMarkRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MakeCourseService makeCourseService;

    @Autowired
    private LoginService loginService;

    @Test
    void historyRepositoryTest(){
        String userCode = "최종혁#01";
        CourseRating courseRating = new CourseRating(userCode, 3.7, "2023-02-02");

        List<History> historyList = userHistoryRepository.findAll();
        for(History history: historyList){
            System.out.println(history.toString());
        }

        System.out.println(memberService.getHistoryById(userCode));
        System.out.println(makeCourseService.courseRating(courseRating));
        historyList = userHistoryRepository.findAll();
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
        String userCode="이영원#01";
        FriendRequestDto friendRequestDto = new FriendRequestDto(userCode, "이영원#02");
        List<UserMark> userMarkList = userMarkRepository.findAll();
        for(UserMark userMark: userMarkList){
            System.out.println(userMark.toString());
        }
        System.out.println(memberService.getFriendsList(userCode));
//        System.out.println(memberService.addFriend(friendRequestDto));
//        System.out.println(memberService.getFriendsList(userCode));
//        System.out.println(memberService.deleteFriend(friendRequestDto));
//        System.out.println(memberService.getFriendsList(userCode));
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

    @Test
    void saveHistoryTest(){
        int placeCount = (int) (Math.random() * 5) + 1;  // 1에서 5 사이의 랜덤한 개수
        List<PlaceDto> testCourseList = placeRepository
                .findRandomPlaces(placeCount)
                .stream()
                .map(PlaceDto::fromEntity)
                .collect(Collectors.toList());
//        CourseResponse courseResponse = new CourseResponse(testCourseList);
//        makeCourseService.saveHistory(courseResponse, "A_1", "이영원#02");
    }

    @Test
    void checkUserTest() {
        UserDto userDto = new UserDto();
        userDto.setName("test2");
        userDto.setEmail("test2@test2.com");
        userDto.setBirthDate("000102");
        userDto.setGender("남성");

        User user = memberService.checkUserId(userDto);
    }

    @Test
    void loginTest() {
        UserDto userDto = new UserDto();
        userDto.setId("test2");
        userDto.setPw("qwer1234!");

        User user = loginService.Login(userDto);
    }

    @Test
    void joinTest() {
        UserDto userDto = new UserDto();
        userDto.setId("kim");
        userDto.setPw("qwer1234!");
        userDto.setName("kjh");
        userDto.setAddressName("");
        userDto.setEmail("kjh@naver.com");
        userDto.setBirthDate("980120");
        userDto.setPrefFatigue(30);
        userDto.setPrefUnique(60);
        userDto.setPrefActivity(50);

        User user = memberService.joinUser(userDto);
    }

    @Test
    void changePwTest() {
        UserDto userDto = new UserDto();
        userDto.setUserCode("kjh#01");
        userDto.setId("kim");
        userDto.setPw("qwer1234!");
        userDto.setName("kjh");
        userDto.setAddressName("");
        userDto.setEmail("kjh@naver.com");
        userDto.setGender("남성");
        userDto.setBirthDate("980120");
        userDto.setPrefFatigue(30);
        userDto.setPrefUnique(60);
        userDto.setPrefActivity(50);

        String changePw = "q1w2e3r4@";
        User user = memberService.ChangeUserPassword(userDto, changePw);
    }


}