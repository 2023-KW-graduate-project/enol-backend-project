package com.graduatepj.enol.member.service;

import com.graduatepj.enol.member.dao.MemberRepository;
import com.graduatepj.enol.member.vo.Member;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Member joinUser(Member joinMember) {
        log.info("joinUser Serviceimpl START");

        log.info("joinMember.getMemberId = {}", joinMember.getMemberId());
        log.info("joinMember.getPassword = {}", joinMember.getPassword());
        log.info("joinMember.getMemberName = {}", joinMember.getMemberName());
        log.info("joinMember.getMemberEmail = {}", joinMember.getEmail());
        log.info("joinMember.getMemberBirthday = {}", joinMember.getBirthday());
//        log.info("joinMember.getNickName = {}", joinMember.getNickName());
        log.info("joinMember.getGender = {}", joinMember.getGender());

        // 중복되지 않은 경우 memberRepository에 저장하고 반환
//        String encPassword = passwordEncoder.encode(password); // 암호화한 패스워드

        Member newMember = new Member();
        newMember.setMemberId(joinMember.getMemberId());
        newMember.setPassword(joinMember.getPassword());
        newMember.setMemberName(joinMember.getMemberName());
        newMember.setEmail(joinMember.getEmail());
        newMember.setBirthday(joinMember.getBirthday());
//        newMember.setNickName(joinMember.getNickName());
        newMember.setGender(joinMember.getGender());

        memberRepository.save(newMember);
        logger.info("save memberRepository");
        return newMember;

    }

//    /**
//     * ID 중복 확인 메서드
//     * @param memberId
//     * @return
//     */
//    @Override
//    public boolean checkDupUser(String memberId) {
//        log.info("checkDupUser Serviceimpl START");
//
//        log.info("MemberId = {}", memberId);
//
//        log.info("memberRepository.existsById(memberId) = {}", memberRepository.existsById(memberId));
//        //회원 ID 중복 확인
//        if(memberRepository.existsById(memberId)){ // 중복인 경우
//            log.info("this is duplicated id - ERROR");
//            return false;
//        }
//        else {
//            log.info("No duplicated id");
//            return true;
//        }
//    }

    /**
     * 아이디 찾기
     * @param member
     * @return
     */
    @Override
    public Member checkUserId(Member member) {
        log.info("checkUserId memberServiceImpl START");

        log.info("member.getMemberName = {}", member.getMemberName());
        log.info("member.getMemberEMail = {}", member.getEmail());
        log.info("member.getMemberBDay = {}", member.getBirthday());
//        log.info("member.getMemberNickName = {}", member.getNickName());
        log.info("member.getMemberGender = {}", member.getGender());

        List<Member> memberList = memberRepository.findAll();
        for (Member checkMember : memberList) {
            log.info("Before IF checkMember.getMemberName = {}", checkMember.getMemberName());
            if(checkMember.getMemberName().equals(member.getMemberName())) { // 이름 같으면 다음거 다 같은지 보기
                log.info("checkMember.getMemberName = {}", checkMember.getMemberName());
                if(checkMember.getEmail().equals(member.getEmail())) { // email같으면 생일까지 같은지 확인
                    log.info("Name same");
                    log.info("checkMember.getMemberEmail = {}", checkMember.getEmail());
                    if(checkMember.getBirthday().equals(member.getBirthday())) {
                        log.info("Email same");
                        log.info("checkMember.getMemberBirthDay = {}", checkMember.getBirthday());
//                        if(checkMember.getNickName().equals(member.getNickName())) {
                            log.info("nickName same");
//                            log.info("checkMember.getMemberNickName = {}", checkMember.getNickName());
                            if(checkMember.getGender().equals(member.getGender())) {
                                log.info("gender same");
                                log.info("checkMember.getMemberGender = {}", checkMember.getGender());
                                return checkMember; // 다 같으면 해당 멤버 객체 반환
                            }
//                        }
                    }
                }
            }
        }
        log.info("checkUserId memberServiceImpl END - ERROR");
        return null; // 같은게 없으면 null 반환
    }

    /**
     * 비밀번호 찾기
     * @param member
     * @return
     */
    @Override
    public Member findUserPassword(Member member) {
        log.info("findUserPassword memberServiceImpl START");

        log.info("member.getMemberId() = {}", member.getMemberId());
        log.info("member.getMemberName = {}", member.getMemberName());
        log.info("member.getMemberEMail = {}", member.getEmail());
        log.info("member.getMemberBDay = {}", member.getBirthday());
//        log.info("member.getMemberNickName = {}", member.getNickName());
        log.info("member.getMemberGender = {}", member.getGender());

        List<Member> memberList = memberRepository.findAll();
        for (Member checkMember : memberList) {
            log.info("Before IF checkMember.getMemberId = {}", checkMember.getMemberId());
            if(checkMember.getMemberId().equals(member.getMemberId())) {
                log.info("checkMember.getMemberId = {}", checkMember.getMemberId());
                log.info("ID same");
                if(checkMember.getMemberName().equals(member.getMemberName()) &&
                checkMember.getEmail().equals(member.getEmail()) &&
                checkMember.getBirthday().equals(member.getBirthday()) &&
//                checkMember.getNickName().equals(member.getNickName()) &&
                checkMember.getGender().equals(member.getGender())) { // 입력한 정보가 다 맞은 경우 해당 멤버 객체 반환
                    log.info("All info same");
                    return checkMember; // ID가 같은 해당 멤버 객체 반환
                }
                else {
                    log.info("Wrong info");
                    return null; // 입력 정보가 다 맞지 않으면 null 반환
                }
            }
        }
        log.info("findPassword memberServiceImpl END - ERROR");
        return null; // 같은게 없으면 null 반환
    }

    /**
     * 비밀번호 바꾸기
     * @param member
     * @param changePW
     * @return
     */
    @Override
    public Member ChangeUserPassword(Member member, String changePW) {
        log.info("changeUserPassword memberServiceImpl START");

        log.info("member.getMemberId() = {}", member.getMemberId());
        log.info("member.getMemberName = {}", member.getMemberName());
        log.info("member.getMemberEMail = {}", member.getEmail());
        log.info("member.getMemberBDay = {}", member.getBirthday());
//        log.info("member.getMemberNickName = {}", member.getNickName());
        log.info("member.getMemberGender = {}", member.getGender());

        List<Member> memberList = memberRepository.findAll();
        for (Member checkMember : memberList) {
            log.info("Before IF checkMember.getMemberId = {}", checkMember.getMemberId());
            if (checkMember.getMemberId().equals(member.getMemberId())) { // 해당 회원 정보 찾으면 비밀 번호 변경
                Member changeMember = new Member();
                changeMember.setMemberId(member.getMemberId());
                changeMember.setMemberName(member.getMemberName());
                changeMember.setPassword(member.getPassword());
//                changeMember.setEnpassword(member.getEnpassword());
                changeMember.setEmail(member.getEmail());
                changeMember.setBirthday(member.getBirthday());
//                changeMember.setNickName(member.getNickName());
                changeMember.setGender(member.getGender());

                memberRepository.save(changeMember);

                log.info("changePassword success");
                return changeMember;
            }

        }
        log.info("changePassword memberServiceImpl END - ERROR");
        return null;
    }

    @Override
    public Member modifyMemberInfo(Member member) { // 비밀번호 변경, 취향 변경 -
        return null;
    }

    @Override
    public boolean deleteMember(Member member) { // 계정 삭제
        log.info("changeUserPassword memberServiceImpl START");

        log.info("member.getMemberId() = {}", member.getMemberId());
        log.info("member.getPassword() = {}", member.getPassword());
        log.info("member.getMemberName = {}", member.getMemberName());
        log.info("member.getMemberEMail = {}", member.getEmail());
        log.info("member.getMemberBDay = {}", member.getBirthday());
//        log.info("member.getMemberNickName = {}", member.getNickName());
        log.info("member.getMemberGender = {}", member.getGender());

        List<Member> memberList = memberRepository.findAll();
        for (Member checkMember : memberList) {
            log.info("Before IF checkMember.getMemberId = {}", checkMember.getMemberId());
            if (checkMember.getMemberId().equals(member.getMemberId())) { // 해당 회원 정보 찾으면 계정 삭제
                // 다른 정보들 맞는지 추가?
                memberRepository.delete(member);

                log.info("deleteMember success");
                return true;
            }

        }
        return false;
    }

    @Override
    public List<Member> showMembers() {
        log.info("showMembers memberServiceImpl start");
        List<Member> members = memberRepository.findAll();
        log.info("members = {}", members);
        return members;
    }
}
