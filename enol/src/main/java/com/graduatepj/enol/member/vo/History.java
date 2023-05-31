package com.graduatepj.enol.member.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "history")
public class History {
    @Id
    @Column(name = "user_code")
    private String userCode;

    @Column(name = "number")
    private int number;

    // 안에는 courseId, categoryCode, rating 순으로 있음
    @Column(name = "course")
    private List<String> course;
}
