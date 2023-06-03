package com.graduatepj.enol.member.vo;

import lombok.*;
import org.hibernate.annotations.Filter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

@ToString
@Getter
@Setter
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
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    public static class HistoryCourse {

        @Field("order")
        private String order;
        @Field("course_id")
        private String courseId;

        @Field("place_ids")
        private List<Long> placeIds;

        @Field("rating")
        private Double rating;
    }
}
