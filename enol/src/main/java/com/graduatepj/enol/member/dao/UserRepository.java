package com.graduatepj.enol.member.dao;

import com.graduatepj.enol.member.vo.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}
