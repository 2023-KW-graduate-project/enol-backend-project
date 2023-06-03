package com.graduatepj.enol.member.dao;

import com.graduatepj.enol.member.dto.UserDto;
import com.graduatepj.enol.member.vo.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> { // 여기 쿼리 맞는지 확인 부탁

    // 회원가입에서 usercode 부여할 때 겹치는지 확인하기 위한 쿼리문
//    @Query(value = "select u.user_code from user u where u.user_code=:userCode", nativeQuery = true)
    @Query(value = "{'userCode' : ?0}", fields = "{'userCode' : 1}")
    List<String> findUserCode(@Param("userCode") String userCode);

    // 쿼리문(1)
    List<User> findUserCodeByName(String userName);

    // 아이디 찾기에서 같은 아이디만 가져오는 쿼리문
//    @Query(value = "select * from user u where u.email = :email AND u.name=:name AND u.birth_date=:birthDate AND u.gender=:gender", nativeQuery = true)
    @Query(value = "{'email' : ?0, 'name': ?1, 'birthDate' : ?2, 'gender' : ?3}")
    List<User> findId(@Param("email") String email, @Param("name") String name, @Param("birthDate") String birthDate, @Param("gender") String gender);

    // 쿼리문(2)
    List<User> findByEmailAndNameAndBirthDateAndGender(String email, String name, String birthDate, String gender);

    // 비밀번호 찾기에서 아이디 같은 것만 가져오는 쿼리문
//    @Query(value = "select * from user u where u.id=:id AND u.email=:email AND u.name=:name AND u.birth_date=:birthDate AND u.gender=:gender", nativeQuery = true)
    @Query(value = "{'id' : ?0, 'email' : ?1, 'name' : ?2, 'birthDate' : ?3, 'gender' : ?4}")
    List<User> findPw(@Param("id") String id, @Param("email") String email, @Param("name") String name, @Param("birthDate") String birthDate, @Param("gender") String gender);

    // 쿼리문(3)
    List<User> findByIdAndEmailAndNameAndBirthDateAndGender(String id, String email, String name, String birthDate, String gender);

    // 아이디가 같은 User 객체를 가져오는 쿼리문
//    @Query(value = "SELECT * FROM user u WHERE u.id=:id", nativeQuery = true)
    @Query(value = "{'id' : ?0}")
    List<User> findByUserId(@Param("id") String id);

    // 쿼리문(4)
    List<User> findAllById(String id);

    Optional<User> findByUserCode(String userCode);
}
