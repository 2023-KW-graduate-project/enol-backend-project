package com.graduatepj.enol.member.dao;

import com.graduatepj.enol.member.vo.History;
import com.graduatepj.enol.member.vo.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserHistoryRepository extends MongoRepository<History, String> {
    Optional<History> findByUserCode(String userCode);
}
