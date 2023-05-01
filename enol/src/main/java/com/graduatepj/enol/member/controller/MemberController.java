package com.graduatepj.enol.member.controller;

import com.graduatepj.enol.member.service.MemberService;
import com.graduatepj.enol.member.vo.Member;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     */
    @GetMapping("/users")
    public List<Member> showMemberList() {
        log.info("MemberController.showMemberList START");
        List<Member> memberList = memberService.showMembers();
        return memberList;
    }

    /**
     * 회원 가입 페이지
     * @return
     */
    @PostMapping("/join")
    public Member joinMember(@RequestBody Member member) {
        log.info("MemberController.join START");

        log.info("memberId = {}", member.getMemberId());
        log.info("memberPassword = {}", member.getPassword());
        log.info("memberName = {}", member.getMemberName());
        log.info("memberEmail = {}", member.getEmail());
        log.info("memberBirthDay = {}", member.getBirthday());
        log.info("memberGender = {}", member.getGender());
        Member joinMember = memberService.joinUser(member);

        log.info("MemberController.join Success!!!");
        return joinMember;
    }

//    /**
//     * 중복 ID 확인 버튼
//     * @param member
//     * @return
//     */
//    @PostMapping("duplicationCheck")
//    public String duplicationCheckMember(Member member) { // 프론트에서 가능할듯?, boolean으로 중복이면 false, 아니면 true식으로 반환하면 될듯
//        log.info("MemberController.dupChecker START");
//
//        log.info("memberId = {}", member.getMemberId());
//        log.info("memberPassword = {}", member.getPassword());
//        log.info("memberName = {}", member.getMemberName());
//        log.info("memberEmail = {}", member.getEMail());
//        log.info("memberBirthDay = {}", member.getBDay());
//
//        boolean dupChecker = memberService.checkDupUser(member.getMemberId());
//
//        if(dupChecker) { // true이면 중복 없음
//            log.info("MemberController.dupChecker No Duplicate END");
//            return "중복된 아이디 아님";
//        }
//        else {
//            log.info("MemberController.dupChecker Duplicate Id END");
//            return "중복된 아이디입니다.";
//        }
//    }

    /**
     * 아이디 찾기
     * @param member
     * @return
     */
    @PostMapping("checkId")
    public Member checkId(@RequestBody Member member) {
        log.info("MemberController.checkId START");

        log.info("memberName = {}", member.getMemberName());
        log.info("eMail = {}", member.getEmail());
        log.info("BDay = {}", member.getBirthday());
        log.info("gender = {}", member.getGender());

        Member checkMember = new Member();
        checkMember.setMemberName(member.getMemberName());
        checkMember.setEmail(member.getEmail());
        checkMember.setBirthday(member.getBirthday());
        checkMember.setGender(member.getGender());

        Member checkedMember = memberService.checkUserId(checkMember);

        log.info("MemberController.checkId END");
        return checkedMember;
    }

    /**
     * 비밀 번호 찾기 페이지
     * 해당 member 객체를 리턴하면 비밀번호 변경 창에서 변경한 비밀번호대로 다시 저장
     * @param member
     * @return
     */
    @PostMapping("findPW")
    public Member findPW(@RequestBody Member member) {
//    public Member findPW(@RequestBody String memberId, @RequestBody String memberName, @RequestParam String eMail, @RequestParam String BDay) {
        log.info("MemberController.findPassword START");

        log.info("memberId = {}", member.getMemberId());
        log.info("memberName = {}", member.getMemberName());
        log.info("eMail = {}", member.getEmail());
        log.info("BDay = {}", member.getBirthday());
        log.info("gender = {}", member.getGender());

        Member changeMember = new Member();
        changeMember.setMemberId(member.getMemberId());
        changeMember.setMemberName(member.getMemberName());
        changeMember.setEmail(member.getEmail());
        changeMember.setBirthday(member.getBirthday());
        changeMember.setGender(member.getGender());

        Member changedMember = memberService.findUserPassword(changeMember); // 비밀번호 변경할 member 객체 OR null

        return changedMember;
    }

    /**
     * 입력한 비밀번호로 비밀번호 변경 - 파라미터 수정해야할 듯?
     * @param member
     * @param password
     * @return
     */
    @PostMapping("changePW")
    public Member changePW(@RequestBody Member member, @RequestParam String password) {
        log.info("MemberController.changePassword START");

        log.info("memberId = {}", member.getMemberId());
        log.info("memberName = {}", member.getMemberName());
        log.info("eMail = {}", member.getEmail());
        log.info("BDay = {}", member.getBirthday());

        Member changeMember = memberService.ChangeUserPassword(member, password);

        return changeMember;
    }

    /**
     * 사용자가 입력했던 취향 변경
     * @return
     */
    @PostMapping("modifyMember")
    public Member modifyMember() {
        log.info("MemberController.modifyMember START");

        Member member = new Member();
        return member;
    }

    /**
     * 계정 삭제
     * @return
     */
    @PostMapping("deleteMember")
    public boolean deleteMember(@RequestBody Member member) {
        log.info("MemberController.deleteMember START");
        log.info("memberId = {}", member.getMemberId());
        log.info("memberPassword = {}", member.getPassword());
        log.info("memberName = {}", member.getMemberName());
        log.info("eMail = {}", member.getEmail());
        log.info("BDay = {}", member.getBirthday());

        boolean deleteMemberSuccess = memberService.deleteMember(member);

        return deleteMemberSuccess;
    }

}
