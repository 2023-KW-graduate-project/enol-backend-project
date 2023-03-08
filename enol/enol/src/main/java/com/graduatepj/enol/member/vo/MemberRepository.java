package com.graduatepj.enol.member.vo;

import com.graduatepj.enol.member.vo.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

}


