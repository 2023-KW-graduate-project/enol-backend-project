package com.graduatepj.enol.member.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Filter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_history")
public class History {
    @Id
    private String _id;

    @Field("user_code")
    private String userCode;

    @Field("number")
    private int number;

    // 안에는 courseId, categoryCode, rating 순으로 있음
//    @Field("course")
//    private List<String> course;

    @Field("course")
    private List<HistoryCourse> course;

    @Getter
    public class HistoryCourse {
        @Field("course_id")
        private List<String> courseId;

        @Field("place_ids")
        private List<String> placeIds;

        @Field("rating")
        private List<Double> rating;
    }
}
