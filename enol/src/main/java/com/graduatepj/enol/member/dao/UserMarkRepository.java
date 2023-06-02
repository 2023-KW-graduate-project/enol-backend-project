package com.graduatepj.enol.member.dao;

import com.graduatepj.enol.member.vo.UserMark;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserMarkRepository extends MongoRepository<UserMark, String> {
    @Query(value = "{'userCode': ?0}", fields = "{'friendCodes': 1}")
    List<UserMark> findFriendCodesById(String userCode);

    Optional<UserMark> findUserMarkByUserCode(String userCode);

}
