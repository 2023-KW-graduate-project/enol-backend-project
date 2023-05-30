package com.graduatepj.enol.member.dao;

import com.graduatepj.enol.member.vo.UserPreference;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserPreferenceRepository extends MongoRepository<UserPreference, String> {

}
