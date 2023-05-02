package com.graduatepj.enol.makeCourse.dao;


import com.graduatepj.enol.member.vo.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query(value = "SELECT * FROM member WHERE id=:id", nativeQuery = true)
    Member findMembersByMemberId(@Param("id") double memberId);
}
