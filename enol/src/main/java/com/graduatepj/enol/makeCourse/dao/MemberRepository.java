package com.graduatepj.enol.makeCourse.dao;


import com.graduatepj.enol.member.vo.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m WHERE m.memberId IN :memberIdList")
    List<Member> findAllByIdIn(@Param("memberIdList") List<Long> memberIdList);
}