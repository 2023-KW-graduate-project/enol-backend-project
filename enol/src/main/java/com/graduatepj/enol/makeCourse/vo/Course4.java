package com.graduatepj.enol.makeCourse.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "course_4")
public class Course4 {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private String id; // Course_id
//    @ElementCollection
//    private List<String> categories; // category_group1,2,3,4??

    /** C열 카테고리 1 */
    @Column(name = "category_group_code_1")
    private String categoryGroupCode1;

    /** C열 카테고리 2 */
    @Column(name = "category_group_code_2")
    private String categoryGroupCode2;

    /** C열 카테고리 3 */
    @Column(name = "category_group_code_3")
    private String categoryGroupCode3;

    /** C열 카테고리 4*/
    @Column(name = "category_group_code_4")
    private String categoryGroupCode4;

    /** 피로도 */
    @Column(name = "total_피로도")
    private int fatigability;

    /** 특이도 */
    @Column(name = "total_특이도")
    private int specification;

    /** 활동성 */
    @Column(name = "total_활동성")
    private int activity;

    /** 시간 */
    @Column(name = "total_time")
    private int time;

}
