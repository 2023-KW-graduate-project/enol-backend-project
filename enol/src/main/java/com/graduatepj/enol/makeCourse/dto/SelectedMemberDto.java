package com.graduatepj.enol.makeCourse.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectedMemberDto {
    /** 회원가입때 사용자에게 받을 정보 */
    /** 사용자 ID */
    private String memberId;

    /** 사용자 이름 */
    private String memberName;

    /** 사용자 이메일 */
    private String email;

    /** 사용자 생일 */
    private String birthday;

//    /** 사용자 닉네임 */
//    @Column(name = "NICKNAME")
//    private String nickName;

    /** 사용자 성별 */
    private String gender;

}
