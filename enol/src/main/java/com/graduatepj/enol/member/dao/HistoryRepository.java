package com.graduatepj.enol.member.dao;

import com.graduatepj.enol.member.vo.History;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistoryRepository extends MongoRepository<History, String> {

}
