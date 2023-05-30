package com.graduatepj.enol.member.service;

import com.graduatepj.enol.makeCourse.dao.CourseV2Repository;
import com.graduatepj.enol.makeCourse.dao.PlaceRepository;
import com.graduatepj.enol.makeCourse.dto.CourseDto;
import com.graduatepj.enol.makeCourse.dto.PlaceDto;
import com.graduatepj.enol.makeCourse.vo.CourseV2;
import com.graduatepj.enol.member.dao.*;
import com.graduatepj.enol.member.dto.HistoryDto;
import com.graduatepj.enol.member.dto.UserDto;
import com.graduatepj.enol.member.dto.UserPreferenceDto;
import com.graduatepj.enol.member.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("memberService")
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService<Member>{
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class); // 로거 띄우기 위해

    private final MemberRepository memberRepository; // jpa 사용할 repository

    private final HistoryRepository historyRepository;

    private final UserMarkRepository userMarkRepository;

    private final UserPreferenceRepository userPreferenceRepository;

    private final UserRepository userRepository;

    private final CourseV2Repository courseV2Repository;

    private final PlaceRepository placeRepository;

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
        log.info("joinMember.getGender = {}", joinMember.getGender());
        log.info("joinMember.getFatigability = {}", joinMember.getFatigability());
        log.info("joinMember.getSpecification = {}", joinMember.getSpecification());
        log.info("joinMember.getActivity = {}", joinMember.getActivity());

        Member newMember = new Member();
        newMember.setMemberId(joinMember.getMemberId());
        newMember.setPassword(joinMember.getPassword());
        newMember.setMemberName(joinMember.getMemberName());
        newMember.setEmail(joinMember.getEmail());
        newMember.setBirthday(joinMember.getBirthday());
        newMember.setGender(joinMember.getGender());
        newMember.setFatigability(joinMember.getFatigability());
        newMember.setSpecification(joinMember.getSpecification());
        newMember.setActivity(joinMember.getActivity());

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
        log.info("member.getMemberGender = {}", member.getGender());
        log.info("member.getFatigability = {}", member.getFatigability());
        log.info("member.getSpecification = {}", member.getSpecification());
        log.info("member.getActivity = {}", member.getActivity());

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
                        log.info("member.getGender = {}", member.getGender());
                        log.info("checkMember.getGender = {}", checkMember.getGender());
                        if(checkMember.getGender().equals(member.getGender())) {
                            log.info("gender same");
                            log.info("checkMember.getMemberGender = {}", checkMember.getGender());
                            return checkMember; // 다 같으면 해당 멤버 객체 반환
                        }
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
        log.info("member.getMemberGender = {}", member.getGender());
        log.info("member.geMemberFatigability = {}", member.getFatigability());
        log.info("member.getMemberSpecification = {}", member.getSpecification());
        log.info("member.getMemberActivity = {}", member.getActivity());

        List<Member> memberList = memberRepository.findAll();
        for (Member checkMember : memberList) {
            log.info("Before IF checkMember.getMemberId = {}", checkMember.getMemberId());
            if(checkMember.getMemberId().equals(member.getMemberId())) {
                log.info("checkMember.getMemberId = {}", checkMember.getMemberId());
                log.info("ID same");
                if(checkMember.getMemberName().equals(member.getMemberName()) &&
                        checkMember.getEmail().equals(member.getEmail()) &&
                        checkMember.getBirthday().equals(member.getBirthday()) &&
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
        log.info("member.getMemberGender = {}", member.getGender());
        log.info("member.getMemberFatigability = {}", member.getFatigability());
        log.info("member.getMemberSpecification = {}", member.getSpecification());
        log.info("member.getMemberActivity = {}", member.getActivity());

        List<Member> memberList = memberRepository.findAll();
        for (Member checkMember : memberList) {
            log.info("Before IF checkMember.getMemberId = {}", checkMember.getMemberId());
            if (checkMember.getMemberId().equals(member.getMemberId())) { // 해당 회원 정보 찾으면 비밀 번호 변경
                Member changeMember = new Member();
                changeMember.setMemberId(member.getMemberId());
                changeMember.setMemberName(member.getMemberName());
                changeMember.setPassword(changePW); // 바꾼 비밀번호로 비밀번호만 수정
                changeMember.setEmail(member.getEmail());
                changeMember.setBirthday(member.getBirthday());
                changeMember.setGender(member.getGender());
                changeMember.setFatigability(member.getFatigability());
                changeMember.setSpecification(member.getSpecification());
                changeMember.setActivity(member.getActivity());

                memberRepository.save(changeMember);

                log.info("changePassword success");
                return changeMember;
            }

        }
        log.info("changePassword memberServiceImpl END - ERROR");
        return null;
    }

    /**
     * 사용자 취향 변경하는 메서드, 비밀번호나 취향이 아닌 사용자 정보 수정도 가능할 듯
     * @param modifyMember
     * @return
     */
    @Override
    public Member modifyMemberInfo(Member modifyMember) { // 비밀번호 변경, 취향 변경 -
        log.info("joinMember.getMemberId = {}", modifyMember.getMemberId());
        log.info("joinMember.getPassword = {}", modifyMember.getPassword());
        log.info("joinMember.getMemberName = {}", modifyMember.getMemberName());
        log.info("joinMember.getMemberEmail = {}", modifyMember.getEmail());
        log.info("joinMember.getMemberBirthday = {}", modifyMember.getBirthday());
        log.info("joinMember.getGender = {}", modifyMember.getGender());
        log.info("joinMember.getFatigability = {}", modifyMember.getFatigability());
        log.info("joinMember.getSpecification = {}", modifyMember.getSpecification());
        log.info("joinMember.getActivity = {}", modifyMember.getActivity());

        Member newMember = new Member();
        newMember.setMemberId(modifyMember.getMemberId());
        newMember.setPassword(modifyMember.getPassword());
        newMember.setMemberName(modifyMember.getMemberName());
        newMember.setEmail(modifyMember.getEmail());
        newMember.setBirthday(modifyMember.getBirthday());
        newMember.setGender(modifyMember.getGender());
        newMember.setFatigability(modifyMember.getFatigability());
        newMember.setSpecification(modifyMember.getSpecification());
        newMember.setActivity(modifyMember.getActivity());

        memberRepository.save(newMember);
        logger.info("modify memberRepository");
        return newMember;
    }

    @Override
    public boolean deleteMember(Member member) { // 계정 삭제
        log.info("changeUserPassword memberServiceImpl START");

        log.info("member.getMemberId() = {}", member.getMemberId());
        log.info("member.getPassword() = {}", member.getPassword());
        log.info("member.getMemberName = {}", member.getMemberName());
        log.info("member.getMemberEMail = {}", member.getEmail());
        log.info("member.getMemberBDay = {}", member.getBirthday());
        log.info("member.getMemberGender = {}", member.getGender());
        log.info("member.getMemberFatigability = {}", member.getFatigability());
        log.info("member.getMemberSpecification = {}", member.getSpecification());
        log.info("member.getMemberActivity = {}", member.getActivity());


        List<Member> memberList = memberRepository.findAll();
        for (Member checkMember : memberList) {
            log.info("Before IF checkMember.getMemberId = {}", checkMember.getMemberId());
            if (checkMember.getMemberId().equals(member.getMemberId())) { // 해당 회원 정보 찾으면 계정 삭제
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

    @Override
    public List<List<PlaceDto>> getBookmarkCourseById(String userCode){
        // 뭘 저장하려는건지는 모르겠다마는 여기 수정해야할듯?
        List<Long> courseIds = userMarkRepository.findCoursesById(userCode);
        for(Long id : courseIds){

        }
        return null;
    }

    // 여기도 어떤식으로 저장되어있는지를 잘 모르겠음
    @Override
    public List<PlaceDto> getBookmarkPlaceById(String userCode){
        List<Long> placeIds = userMarkRepository.findplacesById(userCode);
        List<PlaceDto> places=new ArrayList<>();
        for(Long id:placeIds){
            places.add(PlaceDto.fromEntity(
                    placeRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("findById failed"))));
        }
        return places;
    }

    @Override
    public UserPreferenceDto getPreferencesById(String userCode){
        return UserPreferenceDto.from(userPreferenceRepository.findById(userCode)
                .orElseThrow(() -> new RuntimeException("getPreferencesById method failed")));
    }

    @Override
    public HistoryDto getHistoryById(String userCode){
        return HistoryDto.from(historyRepository.findById(userCode)
                .orElseThrow(() -> new RuntimeException("getHistoryById method failed")));
    }

    @Override
    public List<UserDto> getFriendsList(String userCode){
        List<String> friendsCode = getFriendsById(userCode);
        List<UserDto> friendList=new ArrayList<>();
        for(String code:friendsCode){
            friendList.add(getUserInfo(code));
        }
        return friendList;
    }

    private UserDto getUserInfo(String userCode){
        return UserDto.from(userRepository.findById(userCode)
                .orElseThrow(() -> new RuntimeException("getUserInfo method failed")));
    }

    private List<String> getFriendsById(String userCode){
        return userMarkRepository.findFriendCodesById(userCode);
    }
}
