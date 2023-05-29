package com.graduatepj.enol.member.dao;

import com.graduatepj.enol.member.vo.UserMark;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserMarkRepository extends MongoRepository<UserMark, String> {

}
