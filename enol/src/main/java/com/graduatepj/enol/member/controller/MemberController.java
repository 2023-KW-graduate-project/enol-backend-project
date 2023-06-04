package com.graduatepj.enol.member.controller;

import com.graduatepj.enol.makeCourse.dto.PlaceDto;
import com.graduatepj.enol.member.dto.*;
import com.graduatepj.enol.member.service.MemberService;
import com.graduatepj.enol.member.vo.Member;
import com.graduatepj.enol.member.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {
    // 회원 가입, 중복 ID 확인, 아이디 확인, 비밀번호 변경, 고객 정보 수정(비밀번호, 취향), 사용자 삭제
    @Resource(name = "memberService")
    private MemberService memberService;


    /**
     * 유저 리스트 보이기
     * @return
     */
    @GetMapping("/users")
    public List<User> showMemberList() {
        log.info("--- MemberController.showMemberList START ---");
        List<User> userList = memberService.showMembers();
        log.info("--- MemberController.showMemberList END ---");
        return userList;
    }

    /**
     * 회원 가입 페이지
     * @param userDto
     * @return
     */
    @PostMapping("/join")
    public User joinMember(@RequestBody UserDto userDto) {
        log.info("--- MemberController.join START ---");

        log.info("userId = {}", userDto.getId());
        log.info("userPw = {}", userDto.getPw());
        log.info("userName = {}", userDto.getName());
        log.info("userBirthDate = {}", userDto.getBirthDate());
        log.info("userGender = {}", userDto.getGender());

        User joinUser = memberService.joinUser(userDto);
        log.info("--- MemberController.join END ---");
        return joinUser;
    }

    /**
     * 아이디 찾기
     * @param userDto
     * @return
     */
    @PostMapping("/checkId")
    public User checkId(@RequestBody UserDto userDto) {
        log.info("--- MemberController.checkId START ---");

        log.info("userName = {}", userDto.getName());
        log.info("userEmail = {}", userDto.getEmail());
        log.info("userBirthDate = {}", userDto.getBirthDate());
        log.info("userGender = {}", userDto.getGender());

        User checkedUser = memberService.checkUserId(userDto);
        log.info("--- MemberController.checkId END ---");
        return checkedUser;

    }

    /**
     * 비밀 번호 찾기 페이지
     * 해당 member 객체를 리턴하면 비밀번호 변경 창에서 변경한 비밀번호대로 다시 저장
     * @param userDto
     * @return
     */
    @PostMapping("/findPW")
    public User findPW(@RequestBody UserDto userDto) {
        log.info("--- MemberController.findPassword START ---");

        log.info("usreId = {}", userDto.getId());
        log.info("userName = {}", userDto.getName());
        log.info("userEmail = {}", userDto.getEmail());
        log.info("userBrithDate = {}", userDto.getBirthDate());
        log.info("userGender = {}", userDto.getGender());

        User changedUser = memberService.findUserPassword(userDto); // 비밀번호 변경할 member 객체 OR null
        log.info("--- MemberController.findPassword END ---");
        return changedUser;
    }

    /***
     * 입력한 비밀번호로 비밀번호 변경
     * @param userDto
     * @param password
     * @return
     */
    @PostMapping("/changePW")
    public User changePW(@RequestBody UserDto userDto, @RequestParam("password") String password) {
        log.info("--- MemberController.changePassword START ---");

        log.info("userId = {}", userDto.getId());
        log.info("userName = {}", userDto.getName());
        log.info("userEmail = {}", userDto.getEmail());
        log.info("userBirthDate = {}", userDto.getBirthDate());

        User changePWUser = memberService.ChangeUserPassword(userDto, password);
        log.info("--- MemberController.changePassword END ---");
        return changePWUser;
    }

    /**
     * 사용자가 입력했던 취향 변경
     * @return
     */
    @PostMapping("/modifyMember")
    public User modifyMember(@RequestBody UserDto userDto) {
        log.info("--- MemberController.modifyMember START ---");

        log.info("userId = {}", userDto.getId());
        log.info("userPW = {}", userDto.getPw());
        log.info("userName = {}", userDto.getName());
        log.info("userEmail = {}", userDto.getEmail());
        log.info("userBirthDate = {}", userDto.getBirthDate());
        log.info("userGender = {}", userDto.getGender());
        log.info("userFatigue = {}", userDto.getPrefFatigue());
        log.info("userUnique = {}", userDto.getPrefUnique());
        log.info("userActivity = {}", userDto.getPrefActivity());

        User user = memberService.modifyMemberInfo(userDto);
        log.info("--- MemberController.modifyMember END ---");
        return user;
    }

    /**
     * 계정 삭제
     * @return
     */
    @PostMapping("/deleteMember")
    public boolean deleteMember(@RequestBody UserDto userDto) {
        log.info("--- MemberController.deleteMember START ---");

        log.info("userId = {}", userDto.getId());
        log.info("userPw = {}", userDto.getPw());
        log.info("userName = {}", userDto.getName());
        log.info("userEmail = {}", userDto.getEmail());
        log.info("userBirthDate = {}", userDto.getBirthDate());

        boolean deleteMemberSuccess = memberService.deleteMember(userDto);
        log.info("--- MemberController.deleteMember END ---");
        return deleteMemberSuccess;
    }

    // 히스토리 보여주기
    @PostMapping("/history")
    public ResponseEntity<HistoryDto> showHistory(@RequestBody UserCodeDto userCode) {
        return ResponseEntity.ok(memberService.getHistoryById(userCode.getUserCode()));
    }

    // 친구목록 보여주기
    @PostMapping("/friendlist")
    public ResponseEntity<List<FriendDto>> showFriendList(@RequestBody UserCodeDto userCode) {
        return ResponseEntity.ok(memberService.getFriendsList(userCode.getUserCode()));
    }

    // 친구 삭제
    @PutMapping("/friend/delete")
    public ResponseEntity<String> deleteFriend(@RequestBody FriendRequestDto friendRequestDto){
        return ResponseEntity.ok(memberService.deleteFriend(friendRequestDto));
    }

    // 친구 추가
    @PutMapping("/friend/add")
    public ResponseEntity<String> addFriend(@RequestBody FriendRequestDto friendRequestDto){
        return ResponseEntity.ok(memberService.addFriend(friendRequestDto));
    }

    // 친구 검색
    @PostMapping("/friend/search")
    public ResponseEntity<FriendDto> searchFriend(@RequestBody UserCodeDto friendCode){
        return ResponseEntity.ok(memberService.searchFriend(friendCode.getUserCode()));
    }

    // 즐겨찾기 장소 보여주기
    @PostMapping("/bookmark/place")
    public ResponseEntity<List<PlaceDto>> showBookmarkPlace(@RequestBody UserCodeDto userCode) {
        return ResponseEntity.ok(memberService.getBookmarkPlaceById(userCode.getUserCode()));
    }

    // 즐겨찾기 장소 추가하기
    @PutMapping("/bookmark/place/add")
    public ResponseEntity<String> addBookmarkPlace(@RequestBody BookmarkPlaceDto bookmarkPlaceDto){
        return ResponseEntity.ok(memberService.addBookmarkPlace(bookmarkPlaceDto));
    }

    // 즐겨찾기 코스 보여주기
    @PostMapping("/bookmark/course")
    public ResponseEntity<List<List<PlaceDto>>> showBookmarkCourse(@RequestBody UserCodeDto userCode) {
        return ResponseEntity.ok(memberService.getBookmarkCourseById(userCode.getUserCode()));
    }
}
