package com.graduatepj.enol.member.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "history")
public class History {
    @Id
    private String userCode;
    private int number;
    private String course;
    private String courseId;
    private List<String> categoryCode;
    private double rating;
}
