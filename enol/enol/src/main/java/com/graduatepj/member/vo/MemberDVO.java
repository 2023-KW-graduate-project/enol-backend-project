package com.graduatepj.member.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@ToString
public class MemberDVO {
    // 회원가입때 사용자에게 받을 정보
    /** 사용자 ID */
    private String memberId;

    /** 사용자 이름 */
    private String memberName;

    /** 비밀번호 */
    private String password;

    // 카카오맵에 필요한 것
    /** 카카오 id */
    private String kakaoId;

    // 실행 중 필요한 사용자의 정보
    /** 현 위치 */
    private String address;

    /** 출발 위치 */
    private String startAddress;

    /** 종료 위치 */
    private String endAddress;

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
