package com.graduatepj.enol.member.dao;

import com.graduatepj.enol.member.vo.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

}


