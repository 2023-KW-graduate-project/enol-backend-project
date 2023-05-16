package com.graduatepj.enol.member.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.springframework.boot.autoconfigure.web.WebProperties;

import javax.persistence.*;

@Getter @Setter
@Table(name="MEMBER")
@Entity
public class Member {
    /** 회원가입때 사용자에게 받을 정보 */
    /** 사용자 ID */
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="MEMBERID")
    private String memberId;

    /** 사용자 이름 */
    @Column(name = "MEMBERNAME")
    private String memberName;

    /** 비밀번호 */
    @Column(name="PASSWORD")
    private String password;

    /** 사용자 이메일 */
    @Column(name = "EMAIL")
    private String email;

    /** 사용자 생일 */
    @Column(name = "BDAY")
    private String birthday;

    /** 사용자 성별 */
    @Column(name = "GENDER")
    private String gender;

    /**
     * 피로도
     */
    @Column(name = "피로도")
    private int fatigability;

    /**
     * 특이도
     */
    @Column(name = "특이도")
    private int specification;

    /**
     * 활동도
     */
    @Column(name = "활동도")
    private int activity;

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
