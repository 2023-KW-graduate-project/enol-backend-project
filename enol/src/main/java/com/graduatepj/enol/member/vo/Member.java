package com.graduatepj.enol.member.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter @Setter
@Table(name="MEMBER")
@Entity
public class Member {
    /** 회원가입때 사용자에게 받을 정보 */
    /** 사용자 ID */
    @Id
    @GeneratedValue
    @Column(name="MEMBERID")
    private String memberId;

    /** 사용자 이름 */
    @Column(name = "MEMBERNAME")
    private String memberName;

    /** 비밀번호 */
    @Column(name="PASSWORD")
    private String password;

//    /** 암호화 비밀번호 */
//    @Column(name="ENPASSWORD")
//    private String enpassword;

    /** 사용자 이메일 */
    @Column(name = "EMAIL")
    private String eMail;

    /** 사용자 생일 */
    @Column(name = "BDAY")
    private String BDay;

//    /** 사용자 닉네임 */
//    @Column(name = "NICKNAME")
//    private String nickName;

    /** 사용자 성별 */
    @Column(name = "GENDER")
    private String gender;


//
//    // 실행 중 필요한 사용자의 정보
//    /** 현 위치 */
//    private String address;
//
//    /** 출발 위치 */
//    private String startAddress;
//
//    /** 종료 위치 */
//    private String endAddress;

//    // 가고싶다고 미리 체크한 경우 체크한
////    /** 음식점 리스트 */
////    private List<FoodDVO> foodList;
////
////    /** 카페 리스트 */
////    private List<CafeDVO> cafeList;
////
////    /** 노래방 리스트 */
////    private List<SingDVO> singList;
////
////    /** 놀거리 리스트 */
////    private List<PlayDVO> playList;

}
