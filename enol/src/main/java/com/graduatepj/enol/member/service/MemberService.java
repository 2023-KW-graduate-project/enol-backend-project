package com.graduatepj.enol.member.service;

import com.graduatepj.enol.makeCourse.dto.PlaceDto;
import com.graduatepj.enol.member.dto.HistoryDto;
import com.graduatepj.enol.member.dto.UserDto;
import com.graduatepj.enol.member.dto.UserPreferenceDto;
import com.graduatepj.enol.member.vo.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("memberService")
public interface MemberService {

    /**
     * 회원 가입 service
     * @param userRequest
     * @return
     */
    public abstract User joinUser(UserDto userRequest);

    /**
     * ID 확인 service
     * @param userDto
     * @return
     */
    public abstract User checkUserId(UserDto userDto);

    /**
     * password 찾기 service
     * @param userDto
     * @return
     */
    public abstract User findUserPassword(UserDto userDto);

    /**
     * password 바꾸기
     * @param userDto
     * @param changePW
     * @return
     */
    public abstract User ChangeUserPassword(UserDto userDto, String changePW);

    /**
     * 회원 정보 수정
     * @param userDto
     * @return
     */
    public abstract User modifyMemberInfo(UserDto userDto);


    /**
     * 회원 정보 삭제
     * @param userDto
     * @return
     */
    public abstract boolean deleteMember(UserDto userDto);

    /**
     * 회원들 정보 보이기
     * @return
     */
    public abstract List<User> showMembers();
    public abstract List<List<PlaceDto>> getBookmarkCourseById(String userCode);
    public abstract List<PlaceDto> getBookmarkPlaceById(String userCode);
    public abstract UserPreferenceDto getPreferencesById(String userCode);
    public abstract HistoryDto getHistoryById(String userCode);
    public abstract List<UserDto> getFriendsList(String userCode);

    public abstract List<String> getFriendsById(String userCode);
}

