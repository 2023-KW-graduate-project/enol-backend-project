package com.graduatepj.enol.member.dao;

import com.graduatepj.enol.member.vo.History;
import com.graduatepj.enol.member.vo.UserMark;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserMarkRepository extends MongoRepository<UserMark, String> {
    @Query(value = "{'userCode': ?0}", fields = "{'friendCodes': 1}")
    List<String> findFriendCodesById(String userCode);

    List<String> findFriendCodesByUserCode(String userCode);

    @Query(value = "{'userCode': ?0}", fields = "{'placeIds': 1}")
    List<Long> findplacesById(String userCode);

    List<String> findPlaceIdsByUserCode(String userCode);

    @Query(value = "{'userCode': ?0}", fields = "{'courseIds': 1}")
    List<Long> findCoursesById(String userCode);

    List<String> findCourseIdsByUserCode(String userCode);
}
