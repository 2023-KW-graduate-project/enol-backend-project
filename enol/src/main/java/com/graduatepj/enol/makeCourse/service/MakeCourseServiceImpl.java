package com.graduatepj.enol.makeCourse.service;

import com.graduatepj.enol.makeCourse.vo.*;
import com.graduatepj.enol.member.vo.Member;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MakeCourseServiceImpl implements MakeCourseService{

    @Override
    public FirstCourse firstCourseFiltering(ArrayList<Member> memberList, int startTime, int finishTime, boolean[] courseKeyword, boolean[] goal, String wantedCategory, boolean mealCheck) {
        // C열 카테고리로 이루어진 코스 생성
        log.info("firstCourseFiltering Start!");

        // 입력받은 정보들을 모두 firstCourse에 넣는 과정
        FirstCourse firstCourse = new FirstCourse();
        firstCourse.setMemberList(memberList);  // memberList의 첫번째 원소는 항상 본인이 되도록 프론트에서 함
        firstCourse.setStartTime(startTime);
        firstCourse.setFinishTime(finishTime);
        firstCourse.setCourseKeyword(courseKeyword);
        firstCourse.setGoal(goal);
        firstCourse.setWantedCategory(wantedCategory);
        firstCourse.setMealCheck(mealCheck);

        // firstCourse에 내용 확인
        log.info("firstCourse.getMemberList = {}", firstCourse.getMemberList());
        log.info("firstCourse.getStartTime = {}", firstCourse.getStartTime());
        log.info("firstCourse.getFinishTime = {}", firstCourse.getFinishTime());
        log.info("firstCourse.getCourseKeyword = {}", firstCourse.getCourseKeyword());
        log.info("firstCourse.getGoal = {}", firstCourse.getGoal());
        log.info("firstCourse.getWantedCategory = {}", firstCourse.getWantedCategory());
        log.info("firstCourse.isMealCheck = {}", firstCourse.isMealCheck());

        // 개수 제한으로 가능한 모든 코스 받아오기
        List<BigCourseOutline> bigCourse = new ArrayList<BigCourseOutline>(); // 가능한 코스틀만 담을 리스트

        BigCourseOutline[] bigCourseOutlines = new BigCourseOutline[3219]; // 코스틀 전체
        // 코스 틀 전체인 bigCourseOutlines에 코스틀 넣어주는 메서드? 연산? 필요

        // 시간에 따라 코스에 속할 카테고리 개수 정하기 - 최대 main 4개나 sub 4개
        if(((firstCourse.getFinishTime() - firstCourse.getStartTime()) <= 1)) { // 1시간 이하이면 서브(1시간)1개

            for (BigCourseOutline bigCourseOutline : bigCourseOutlines) { // 코스틀 전체에서 코스 하나하나 가져오기

                String CourseOutline = bigCourseOutline.getCourseOutline(); // 객체에서 코스만 뽑아오기
                String[] Courses = CourseOutline.split(","); // 코스에서 ,로 분리 -> 놀기Main스포츠 / 관계Sub테마카페

                if(Courses.length == 1) { // 1개 짜리
                    if(Courses[0].contains("Sub")) { // 즉, sub 1개짜리인 것을 의미
                            bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                            // 매개변수로 받은 모든 정보들이 담기게 해야하나?
                    }
                }
            }
            // 시간에 따른 개수로 필터링 한 결과만 남음
            // sub 1개짜리로 이루어진 모든 코스틀이 bigCourse에 저장되어 있을 것

            // 코스 목적과 키워드에 따라 한 개의 C열로 구성된 코스 틀 뽑아내기

            // 코스 목적으로 포함되어야 하는 bigCategory 들어가도록 필터링
            bigCourse = firstCourseGoalFiltering(firstCourse, bigCourse);
            // 목적으로 까지 필터링한 것만 bigCourse에 남음

            // 코스 키워드, 순서대로 /이색, 일상/체력높음,체력낮음/액티비티, 앉아서놀기/
            int N = 0; // N보다 높거나 낮은것을 따지므로
            bigCourse = firstCourseKeywordFiltering(firstCourse, bigCourse, N);
            // 코스 키워드인 수치로까지 필터링



        }
        else if ((firstCourse.getFinishTime() - firstCourse.getStartTime()) <= 2) { // 2시간 이하이면 서브(1시간)2개, 메인(2시간) 1개

            for (BigCourseOutline bigCourseOutline : bigCourseOutlines) { // 코스틀 전체에서 코스 하나하나 가져오기

                String CourseOutline = bigCourseOutline.getCourseOutline(); // 객체에서 코스만 뽑아오기
                String[] Courses = CourseOutline.split(","); // 코스에서 ,로 분리 -> 놀기Main스포츠 / 관계sub테마카페

                if(Courses.length == 1) { // 1개 짜리 - Main(2)
                        if((  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  )
                        ) { // 즉, 2시간짜리 Main 1개짜리인 것을 의미
                            bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                            // 매개변수로 받은 모든 정보들이 담기게 해야하나?
                        }
                }
                else if(Courses.length == 2) { // 2개 짜리 - Sub(1)+Sub(1)
                    if(Courses[0].contains("Sub") && Courses[1].contains("Sub")) { // 둘 다 Sub인 경우
                        bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                    }
                }
            }

            // 시간에 따른 개수로 필터링 한 결과만 남음
            // Main 1개나, Sub 2개짜리로 이루어진 모든 코스틀이 bigCourse에 저장되어 있을 것

            // 코스 목적과 키워드에 따라 한 개의 C열로 구성된 코스 틀 뽑아내기

            // 코스 목적으로 포함되어야 하는 bigCategory 들어가도록 필터링
            bigCourse = firstCourseGoalFiltering(firstCourse, bigCourse);
            // 목적으로 까지 필터링한 것만 bigCourse에 남음

            // 코스 키워드, 순서대로 /이색, 일상/체력높음,체력낮음/액티비티, 앉아서놀기/
            int N = 0; // N보다 높거나 낮은것을 따지므로
            bigCourse = firstCourseKeywordFiltering(firstCourse, bigCourse, N);
            // 코스 키워드인 수치로까지 필터링

            if(!wantedCategory.equals("없음")) { // 가고 싶은 카테고리 입력한 경우
                firstCourse.setMainBigCategory(wantedCategory); // wantedCategory를 MainBigCategory로 지정
            }
            else { // 가고 싶은 카테고리 입력 안 한 경우
                // C열의 카테고리들의 우선순위에 따라 main 중 하나를 MainBigCategory로 지정
                //for

            }
        }

        else if ((firstCourse.getFinishTime() - firstCourse.getStartTime()) <= 3) { // 3시간 이하이면 메인(3시간)1개 OR 서브(1시간) 3개 OR 메인(2시간)1개, 서브(1시간)1개

            for (BigCourseOutline bigCourseOutline : bigCourseOutlines) { // 코스틀 전체에서 코스 하나하나 가져오기

                String CourseOutline = bigCourseOutline.getCourseOutline(); // 객체에서 코스만 뽑아오기
                String[] Courses = CourseOutline.split(","); // 코스에서 ,로 분리 -> 놀기Main스포츠 / 관계sub테마카페

                if(Courses.length == 1) { // 1개 짜리 - Main(3)
                    if(Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )) { // 즉, 3시간짜리 Main 1개짜리인 것을 의미
                        bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                        // 매개변수로 받은 모든 정보들이 담기게 해야하나?
                    }
                }
                else if(Courses.length == 2) { // 2개 짜리 - Main(2)+Sub(1)
                    if(
                               ( (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub")) // Main(2)+Sub(1)
                            || (Courses[0].contains("Sub") && (  Courses[1].contains("Main") && ( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) ) // Sub(1)+Main(2)
                    ) {
                        bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                    }
                }
                else if(Courses.length == 3) { // 3개 짜리 - Sub(1)+Sub(1)+Sub(1)
                    if( Courses[0].contains("Sub") && Courses[1].contains("Sub") && Courses[2].contains("Sub") // Sub(1)+Sub(1)+Sub(1)
                    ) {
                        bigCourse.add(bigCourseOutline);
                    }
                }
            }

            // 시간에 따른 개수로 필터링 한 결과만 남음
            // Main 1개나, Sub 2개짜리로 이루어진 모든 코스틀이 bigCourse에 저장되어 있을 것

            // 코스 목적과 키워드에 따라 한 개의 C열로 구성된 코스 틀 뽑아내기

            // 코스 목적으로 포함되어야 하는 bigCategory 들어가도록 필터링
            bigCourse = firstCourseGoalFiltering(firstCourse, bigCourse);
            // 목적으로 까지 필터링한 것만 bigCourse에 남음

            // 코스 키워드, 순서대로 /이색, 일상/체력높음,체력낮음/액티비티, 앉아서놀기/
            int N = 0; // N보다 높거나 낮은것을 따지므로
            bigCourse = firstCourseKeywordFiltering(firstCourse, bigCourse, N);
            // 코스 키워드인 수치로까지 필터링

            if(!wantedCategory.equals("없음")) { // 가고 싶은 카테고리 입력한 경우
                firstCourse.setMainBigCategory(wantedCategory); // wantedCategory를 MainBigCategory로 지정
            }
            else { // 가고 싶은 카테고리 입력 안 한 경우
                // C열의 카테고리들의 우선순위에 따라 main 중 하나를 MainBigCategory로 지정


            }
        }

        else if ((firstCourse.getFinishTime() - firstCourse.getStartTime()) <= 4) { // 4시간 이하이면 메인(3시간)1개, 서브(1시간)1개 OR 메인(2시간)1개, 서브(1시간)2개 OR 서브(1시간)4개

            for (BigCourseOutline bigCourseOutline : bigCourseOutlines) { // 코스틀 전체에서 코스 하나하나 가져오기

                String CourseOutline = bigCourseOutline.getCourseOutline(); // 객체에서 코스만 뽑아오기
                String[] Courses = CourseOutline.split(","); // 코스에서 ,로 분리 -> 놀기Main스포츠 / 관계sub테마카페

                if(Courses.length == 2) { // 2개 짜리 - Main(3)+Sub(1)
                    if (
                               ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub")) // Main(3)+Sub(1)
                            || (Courses[0].contains("Sub") && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) ) // Sub(1)+Main(3)
                    ) {
                        bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                    }
                }

                else if(Courses.length == 3) { // 3개 짜리 - Main(2)+Sub(1)+Sub(1)
                    if(
                               ( (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub") && Courses[2].contains("Sub")) // Main(2)+Sub(1)+Sub(1)
                            || (Courses[0].contains("Sub") && (  Courses[1].contains("Main") && ( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") ) // Sub(1)+Main(2)+Sub(1)
                            || (Courses[0].contains("Sub") && Courses[1].contains("Sub") && (  Courses[2].contains("Main") && ( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) ) // Sub(1)+Sub(1)+Main(2)
                    ) {
                        bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                    }
                }
                else if(Courses.length == 4) { // 4개 짜리 - Sub(1)+Sub(1)+Sub(1)+Sub(1)
                    if( Courses[0].contains("Sub") && Courses[1].contains("Sub") && Courses[2].contains("Sub") && Courses[3].contains("Sub") ) { // Sub(1시간)3개
                        bigCourse.add(bigCourseOutline);
                    }
                }
            }

            // 시간에 따른 개수로 필터링 한 결과만 남음
            // Main 1개나, Sub 2개짜리로 이루어진 모든 코스틀이 bigCourse에 저장되어 있을 것

            // 코스 목적과 키워드에 따라 한 개의 C열로 구성된 코스 틀 뽑아내기

            // 코스 목적으로 포함되어야 하는 bigCategory 들어가도록 필터링
            bigCourse = firstCourseGoalFiltering(firstCourse, bigCourse);
            // 목적으로 까지 필터링한 것만 bigCourse에 남음

            // 코스 키워드, 순서대로 /이색, 일상/체력높음,체력낮음/액티비티, 앉아서놀기/
            int N = 0; // N보다 높거나 낮은것을 따지므로
            bigCourse = firstCourseKeywordFiltering(firstCourse, bigCourse, N);
            // 코스 키워드인 수치로까지 필터링

            if(!wantedCategory.equals("없음")) { // 가고 싶은 카테고리 입력한 경우
                firstCourse.setMainBigCategory(wantedCategory); // wantedCategory를 MainBigCategory로 지정
            }
            else { // 가고 싶은 카테고리 입력 안 한 경우
                // C열의 카테고리들의 우선순위에 따라 main 중 하나를 MainBigCategory로 지정
                //for

            }
        }

        else if ((firstCourse.getFinishTime() - firstCourse.getStartTime()) <= 5) { // 5시간 이하이면 메인(3시간) 1개, 메인(2시간) 1개 OR 메인(3시간)1개, 서브(1시간)2개 OR 메인(2시간), 서브(1시간)3개

            for (BigCourseOutline bigCourseOutline : bigCourseOutlines) { // 코스틀 전체에서 코스 하나하나 가져오기

                String CourseOutline = bigCourseOutline.getCourseOutline(); // 객체에서 코스만 뽑아오기
                String[] Courses = CourseOutline.split(","); // 코스에서 ,로 분리 -> 놀기Main스포츠 / 관계sub테마카페

                if(Courses.length == 2) { // 2개 짜리 - Main(3)+Main(2)
                    if (
                               ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && ( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) ) // Main(3)+Main(2)
                            || ( (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) ) // Main(2)+Main(3)
                    ) {
                        bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                    }
                }

                else if(Courses.length == 3) { // 3개 짜리 - Main(3)+Sub(1)+Sub(1)
                    if(
                               ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub") && Courses[2].contains("Sub") ) // Main(3)+Sub(1)+Sub(1)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") ) // Sub(1)+Main(3)+Sub(1)
                            || ( Courses[0].contains("Sub") && Courses[1].contains("Sub") && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) ) // Sub(1)+Sub(1)+Main(3)
                    ) {
                        bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                    }
                }
                else if(Courses.length == 4) { // 4개 짜리 - Main(2)+Sub(1)+Sub(1)+Sub(1)
                    if(
                               ( (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub") && Courses[2].contains("Sub") && Courses[3].contains("Sub") ) // Main(2)+Sub(1)+Sub(1)+Sub(1)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && ( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") && Courses[3].contains("Sub") ) // Sub(1)+Main(2)+Sub(1)+Sub(1)
                            || ( Courses[0].contains("Sub") && Courses[1].contains("Sub") && (  Courses[2].contains("Main") && ( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && Courses[3].contains("Sub") ) // Sub(1)+Sub(1)+Main(2)+Sub(1)
                            || ( Courses[0].contains("Sub") && Courses[1].contains("Sub") && Courses[2].contains("Sub") && (  Courses[3].contains("Main") && ( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Sub(1)+Sub(1)+Sub(1)+Main(2)
                    ) {
                        bigCourse.add(bigCourseOutline);
                    }
                }
            }

            // 시간에 따른 개수로 필터링 한 결과만 남음
            // Main 1개나, Sub 2개짜리로 이루어진 모든 코스틀이 bigCourse에 저장되어 있을 것

            // 코스 목적과 키워드에 따라 한 개의 C열로 구성된 코스 틀 뽑아내기

            // 코스 목적으로 포함되어야 하는 bigCategory 들어가도록 필터링
            bigCourse = firstCourseGoalFiltering(firstCourse, bigCourse);
            // 목적으로 까지 필터링한 것만 bigCourse에 남음

            // 코스 키워드, 순서대로 /이색, 일상/체력높음,체력낮음/액티비티, 앉아서놀기/
            int N = 0; // N보다 높거나 낮은것을 따지므로
            bigCourse = firstCourseKeywordFiltering(firstCourse, bigCourse, N);
            // 코스 키워드인 수치로까지 필터링

            if(!wantedCategory.equals("없음")) { // 가고 싶은 카테고리 입력한 경우
                firstCourse.setMainBigCategory(wantedCategory); // wantedCategory를 MainBigCategory로 지정
            }
            else { // 가고 싶은 카테고리 입력 안 한 경우
                // C열의 카테고리들의 우선순위에 따라 main 중 하나를 MainBigCategory로 지정
                //for

            }
        }

        else if ((firstCourse.getFinishTime() - firstCourse.getStartTime()) <= 6) { // 6시간 이하이면 메인(3시간) 2개 OR 메인(3시간)1개, 메인(2시간) 1개, 서브(1시간)1개 OR 메인(3시간)1개, 서브(1시간)3개

            for (BigCourseOutline bigCourseOutline : bigCourseOutlines) { // 코스틀 전체에서 코스 하나하나 가져오기

                String CourseOutline = bigCourseOutline.getCourseOutline(); // 객체에서 코스만 뽑아오기
                String[] Courses = CourseOutline.split(","); // 코스에서 ,로 분리 -> 놀기Main스포츠 / 관계sub테마카페

                if(Courses.length == 2) { // 2개 짜리 - Main(3)+Main(3)
                    if (
                            ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) ) // Main(3)+Main(3)
                    ) {
                        bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                    }
                }

                else if(Courses.length == 3) { // 3개 짜리 - Main(3)+Main(2)+Sub(1)
                    if(
                               ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && ( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") ) // Main(3)+Main(2)+Sub(1)
                            || ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub") && (  Courses[2].contains("Main") && ( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) ) // Main(3)+Sub(1)+Main(2)
                            || ( (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") ) // Main(2)+Main(3)+Sub(1)
                            || ( (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[2].contains("Sub") && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) ) // Main(2)+Sub(1)+Main(3)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && ( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) ) // Sub(1)+Main(3)+Main(2)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && ( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) ) // Sub(1)+Main(2)+Main(3)
                    ) {
                        bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                    }
                }
                else if(Courses.length == 4) { // 4개 짜리 - Main(3)+Sub(1)+Sub(1)+Sub(1)
                    if(
                               ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub") && Courses[2].contains("Sub") && Courses[3].contains("Sub") ) // Main(3)+Sub(1)+Sub(1)+Sub(1)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") && Courses[3].contains("Sub") ) // Sub(1)+Main(3)+Sub(1)+Sub(1)
                            || ( Courses[0].contains("Sub") && Courses[1].contains("Sub") && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && Courses[3].contains("Sub") ) // Sub(1)+Sub(1)+Main(3)+Sub(1)
                            || ( Courses[0].contains("Sub") && Courses[1].contains("Sub") && Courses[2].contains("Sub") && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Sub(1)+Sub(1)+Sub(1)+Main(3)
                    ) {
                        bigCourse.add(bigCourseOutline);
                    }
                }
            }

            // 시간에 따른 개수로 필터링 한 결과만 남음
            // Main 1개나, Sub 2개짜리로 이루어진 모든 코스틀이 bigCourse에 저장되어 있을 것

            // 코스 목적과 키워드에 따라 한 개의 C열로 구성된 코스 틀 뽑아내기

            // 코스 목적으로 포함되어야 하는 bigCategory 들어가도록 필터링
            bigCourse = firstCourseGoalFiltering(firstCourse, bigCourse);
            // 목적으로 까지 필터링한 것만 bigCourse에 남음

            // 코스 키워드, 순서대로 /이색, 일상/체력높음,체력낮음/액티비티, 앉아서놀기/
            int N = 0; // N보다 높거나 낮은것을 따지므로
            bigCourse = firstCourseKeywordFiltering(firstCourse, bigCourse, N);
            // 코스 키워드인 수치로까지 필터링

            if(!wantedCategory.equals("없음")) { // 가고 싶은 카테고리 입력한 경우
                firstCourse.setMainBigCategory(wantedCategory); // wantedCategory를 MainBigCategory로 지정
            }
            else { // 가고 싶은 카테고리 입력 안 한 경우
                // C열의 카테고리들의 우선순위에 따라 main 중 하나를 MainBigCategory로 지정
                //for

            }
        }

        else if ((firstCourse.getFinishTime() - firstCourse.getStartTime()) <= 7) { // 7시간 이하이면 메인(3시간)2개, 서브(1시간)1개 OR 메인(3시간) 1개, 메인(2시간)1개, 서브(1시간)2개

            for (BigCourseOutline bigCourseOutline : bigCourseOutlines) { // 코스틀 전체에서 코스 하나하나 가져오기

                String CourseOutline = bigCourseOutline.getCourseOutline(); // 객체에서 코스만 뽑아오기
                String[] Courses = CourseOutline.split(","); // 코스에서 ,로 분리 -> 놀기Main스포츠 / 관계sub테마카페

                if(Courses.length == 3) { // 3개 짜리 - Main(3)+Main(3)+Sub(1)
                    if(
                               ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub")) // Main(3)+Main(3)+Sub(1)
                            || ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub") && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) ) // Main(3)+Sub(1)+Main(3)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) ) // Sub(1)+Main(3)+Main(3)
                    ) {
                        bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                    }
                }
                else if(Courses.length == 4) { // 4개 짜리 - Main(3)+Main(2)+Sub(1)+Sub(1) 해야함
                    if(
                               ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && ( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") && Courses[3].contains("Sub") ) // Main(3)+Main(2)+Sub(1)+Sub(1)
                            || ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub") && (  Courses[2].contains("Main") && ( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && Courses[3].contains("Sub") ) // Main(3)+Sub(1)+Main(2)+Sub(1)
                            || ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub") && Courses[2].contains("Sub") && (  Courses[3].contains("Main") && ( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Main(3)+Sub(1)+Sub(1)+Main(2)
                            || ( (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") && Courses[3].contains("Sub") ) // Main(2)+Main(3)+Sub(1)+Sub(1)
                            || ( (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub") && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && Courses[3].contains("Sub") ) // Main(2)+Sub(1)+Main(3)+Sub(1)
                            || ( (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub") && Courses[2].contains("Sub") && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Main(2)+Sub(1)+Sub(1)+Main(3)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && ( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && Courses[3].contains("Sub") ) // Sub(1)+Main(3)+Main(2)+Sub(1)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") && (  Courses[3].contains("Main") && ( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Sub(1)+Main(3)+Sub(1)+Main(2)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && ( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && Courses[3].contains("Sub") ) // Sub(1)+Main(2)+Main(3)+Sub(1)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && ( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Sub(1)+Main(2)+Sub(1)+Main(3)
                    ) {
                        bigCourse.add(bigCourseOutline);
                    }
                }
            }

            // 시간에 따른 개수로 필터링 한 결과만 남음
            // Main 1개나, Sub 2개짜리로 이루어진 모든 코스틀이 bigCourse에 저장되어 있을 것

            // 코스 목적과 키워드에 따라 한 개의 C열로 구성된 코스 틀 뽑아내기

            // 코스 목적으로 포함되어야 하는 bigCategory 들어가도록 필터링
            bigCourse = firstCourseGoalFiltering(firstCourse, bigCourse);
            // 목적으로 까지 필터링한 것만 bigCourse에 남음

            // 코스 키워드, 순서대로 /이색, 일상/체력높음,체력낮음/액티비티, 앉아서놀기/
            int N = 0; // N보다 높거나 낮은것을 따지므로
            bigCourse = firstCourseKeywordFiltering(firstCourse, bigCourse, N);
            // 코스 키워드인 수치로까지 필터링

            if(!wantedCategory.equals("없음")) { // 가고 싶은 카테고리 입력한 경우
                firstCourse.setMainBigCategory(wantedCategory); // wantedCategory를 MainBigCategory로 지정
            }
            else { // 가고 싶은 카테고리 입력 안 한 경우
                // C열의 카테고리들의 우선순위에 따라 main 중 하나를 MainBigCategory로 지정
                //for

            }
        }

        else if ((firstCourse.getFinishTime() - firstCourse.getStartTime()) <= 8) { // 8시간 이하이면 메인(3시간)2개, 서브(1시간)2개 OR 메인(3시간) 2개, 메인(2시간)1개

            for (BigCourseOutline bigCourseOutline : bigCourseOutlines) { // 코스틀 전체에서 코스 하나하나 가져오기

                String CourseOutline = bigCourseOutline.getCourseOutline(); // 객체에서 코스만 뽑아오기
                String[] Courses = CourseOutline.split(","); // 코스에서 ,로 분리 -> 놀기Main스포츠 / 관계sub테마카페

                if(Courses.length == 3) { // 3개 짜리 - Main(3)+Main(3)+Main(2)
                    if(        ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && ( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) ) // Main(3)+Main(3)+Main(2)
                            || ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && ( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) ) // Main(3)+Main(2)+Main(3)
                            || ( (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) ) // Main(2)+Main(3)+Main(3)
                    ) {
                        bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                    }
                }
                else if(Courses.length == 4) { // 4개 짜리 - Main(3)+Main(3)+Sub(1)+Sub(1)
                    if(
                               ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") && Courses[3].contains("Sub") ) // Main(3)+Main(3)+Sub(1)+Sub(1)
                            || ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub") && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && Courses[3].contains("Sub") ) // Main(3)+Sub(1)+Main(3)+Sub(1)
                            || ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub") && Courses[2].contains("Sub") && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Main(3)+Sub(1)+Sub(1)+Main(3)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && Courses[3].contains("Sub") ) // Sub(1)+Main(3)+Main(3)+Sub(1)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Sub(1)+Main(3)+Sub(1)+Main(3)
                            || ( Courses[0].contains("Sub") && Courses[1].contains("Sub") && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Sub(1)+Sub(1)+Main(3)+Main(3)
                    ) {
                        bigCourse.add(bigCourseOutline);
                    }
                }
            }

            // 시간에 따른 개수로 필터링 한 결과만 남음
            // Main 1개나, Sub 2개짜리로 이루어진 모든 코스틀이 bigCourse에 저장되어 있을 것

            // 코스 목적과 키워드에 따라 한 개의 C열로 구성된 코스 틀 뽑아내기

            // 코스 목적으로 포함되어야 하는 bigCategory 들어가도록 필터링
            bigCourse = firstCourseGoalFiltering(firstCourse, bigCourse);
            // 목적으로 까지 필터링한 것만 bigCourse에 남음

            // 코스 키워드, 순서대로 /이색, 일상/체력높음,체력낮음/액티비티, 앉아서놀기/
            int N = 0; // N보다 높거나 낮은것을 따지므로
            bigCourse = firstCourseKeywordFiltering(firstCourse, bigCourse, N);
            // 코스 키워드인 수치로까지 필터링

            if(!wantedCategory.equals("없음")) { // 가고 싶은 카테고리 입력한 경우
                firstCourse.setMainBigCategory(wantedCategory); // wantedCategory를 MainBigCategory로 지정
            }
            else { // 가고 싶은 카테고리 입력 안 한 경우
                // C열의 카테고리들의 우선순위에 따라 main 중 하나를 MainBigCategory로 지정
                //for

            }
        }

        else if ((firstCourse.getFinishTime() - firstCourse.getStartTime()) <= 9) { // 9시간 이하이면 메인(3시간)3개 OR 메인(3시간) 2개, 메인(2시간)1개, 서브(1시간)1개

            for (BigCourseOutline bigCourseOutline : bigCourseOutlines) { // 코스틀 전체에서 코스 하나하나 가져오기

                String CourseOutline = bigCourseOutline.getCourseOutline(); // 객체에서 코스만 뽑아오기
                String[] Courses = CourseOutline.split(","); // 코스에서 ,로 분리 -> 놀기Main스포츠 / 관계sub테마카페

                if(Courses.length == 3) { // 3개 짜리 - Main(3)+Main(3)+Main(3)
                    if(
                            (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) &&(  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) // Main(3)+Main(3)+Main(3)
                      ) {
                        bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                    }
                }
                else if(Courses.length == 4) { // 4개 짜리 - Main(3)+Main(3)+Main(2)+Sub(1)
                    if(
                               ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && ( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && Courses[3].contains("Sub") ) // Main(3)+Main(3)+Main(2)+Sub(1)
                            || ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") && (  Courses[3].contains("Main") && ( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Main(3)+Main(3)+Sub(1)+Main(2)
                            || ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && ( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && Courses[3].contains("Sub") ) // Main(3)+Main(2)+Main(3)+Sub(1)
                            || ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && ( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Main(3)+Main(2)+Sub(1)+Main(3)
                            || ( (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && Courses[3].contains("Sub") ) // Main(2)+Main(3)+Main(3)+Sub(1)
                            || ( (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Main(2)+Main(3)+Sub(1)+Main(3)
                            || ( (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub") && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Main(2)+Sub(1)+MAin(3)+Main(3)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) ) // Sub(1)+Main(3)+Main(3)+Main(2)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && ( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Sub(1)+Main(3)+Main(2)+Main(3)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && ( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Sub(1)+Main(2)+Main(3)+Main(3)
                    ) {
                        bigCourse.add(bigCourseOutline);
                    }
                }
            }

            // 시간에 따른 개수로 필터링 한 결과만 남음
            // Main 1개나, Sub 2개짜리로 이루어진 모든 코스틀이 bigCourse에 저장되어 있을 것

            // 코스 목적과 키워드에 따라 한 개의 C열로 구성된 코스 틀 뽑아내기

            // 코스 목적으로 포함되어야 하는 bigCategory 들어가도록 필터링
            bigCourse = firstCourseGoalFiltering(firstCourse, bigCourse);
            // 목적으로 까지 필터링한 것만 bigCourse에 남음

            // 코스 키워드, 순서대로 /이색, 일상/체력높음,체력낮음/액티비티, 앉아서놀기/
            int N = 0; // N보다 높거나 낮은것을 따지므로
            bigCourse = firstCourseKeywordFiltering(firstCourse, bigCourse, N);
            // 코스 키워드인 수치로까지 필터링

            if(!wantedCategory.equals("없음")) { // 가고 싶은 카테고리 입력한 경우
                firstCourse.setMainBigCategory(wantedCategory); // wantedCategory를 MainBigCategory로 지정
            }
            else { // 가고 싶은 카테고리 입력 안 한 경우
                // C열의 카테고리들의 우선순위에 따라 main 중 하나를 MainBigCategory로 지정
                //for

            }
        }

        else if ((firstCourse.getFinishTime() - firstCourse.getStartTime()) <= 10) { // 10시간 이하이면 메인(3시간)3개, 서브(1시간)1개

            for (BigCourseOutline bigCourseOutline : bigCourseOutlines) { // 코스틀 전체에서 코스 하나하나 가져오기

                String CourseOutline = bigCourseOutline.getCourseOutline(); // 객체에서 코스만 뽑아오기
                String[] Courses = CourseOutline.split(","); // 코스에서 ,로 분리 -> 놀기Main스포츠 / 관계sub테마카페

                if(Courses.length == 4) { // 4개 짜리 - Main(3)+Main(3)+Main(3)+Sub(1)
                    if(
                               ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && Courses[3].contains("Sub") ) // Main(3)+Main(3)+Main(3)+Sub(1)
                            || ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && Courses[2].contains("Sub") && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Main(3)+Main(3)+Sub(1)+Main(3)
                            || ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && Courses[1].contains("Sub") && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Main(3)+Sub(1)+Main(3)+Main(3)
                            || ( Courses[0].contains("Sub") && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Sub(1)+Main(3)+Main(3)+Main(3)
                    ) {
                        bigCourse.add(bigCourseOutline);
                    }
                }
            }

            // 시간에 따른 개수로 필터링 한 결과만 남음
            // Main 1개나, Sub 2개짜리로 이루어진 모든 코스틀이 bigCourse에 저장되어 있을 것

            // 코스 목적과 키워드에 따라 한 개의 C열로 구성된 코스 틀 뽑아내기

            // 코스 목적으로 포함되어야 하는 bigCategory 들어가도록 필터링
            bigCourse = firstCourseGoalFiltering(firstCourse, bigCourse);
            // 목적으로 까지 필터링한 것만 bigCourse에 남음

            // 코스 키워드, 순서대로 /이색, 일상/체력높음,체력낮음/액티비티, 앉아서놀기/
            int N = 0; // N보다 높거나 낮은것을 따지므로
            bigCourse = firstCourseKeywordFiltering(firstCourse, bigCourse, N);
            // 코스 키워드인 수치로까지 필터링

            if(!wantedCategory.equals("없음")) { // 가고 싶은 카테고리 입력한 경우
                firstCourse.setMainBigCategory(wantedCategory); // wantedCategory를 MainBigCategory로 지정
            }
            else { // 가고 싶은 카테고리 입력 안 한 경우
                // C열의 카테고리들의 우선순위에 따라 main 중 하나를 MainBigCategory로 지정
                //for

            }
        }

        else if ((firstCourse.getFinishTime() - firstCourse.getStartTime()) <= 11) { // 11시간 이하이면 메인(3시간)3개, 메인(2시간)1개

            for (BigCourseOutline bigCourseOutline : bigCourseOutlines) { // 코스틀 전체에서 코스 하나하나 가져오기

                String CourseOutline = bigCourseOutline.getCourseOutline(); // 객체에서 코스만 뽑아오기
                String[] Courses = CourseOutline.split(","); // 코스에서 ,로 분리 -> 놀기Main스포츠 / 관계sub테마카페

                if(Courses.length == 4) { // 4개 짜리 - Main(3)+Main(3)+Main(3)+Main(2)
                    if(
                               ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && (  Courses[3].contains("Main") && ( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Main(3)+Main(3)+Main(3)+Main(2)
                            || ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && ( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Main(3)+Main(3)+Main(2)+Main(3)
                            || ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && ( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Main(3)+Main(2)+Main(3)+Main(3)
                            || ( (  Courses[0].contains("Main") && ( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Main(2)+Main(3)+Main(3)+Main(3)
                    ) {
                        bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                    }
                }

            }

            // 시간에 따른 개수로 필터링 한 결과만 남음
            // Main 1개나, Sub 2개짜리로 이루어진 모든 코스틀이 bigCourse에 저장되어 있을 것

            // 코스 목적과 키워드에 따라 한 개의 C열로 구성된 코스 틀 뽑아내기

            // 코스 목적으로 포함되어야 하는 bigCategory 들어가도록 필터링
            bigCourse = firstCourseGoalFiltering(firstCourse, bigCourse);
            // 목적으로 까지 필터링한 것만 bigCourse에 남음

            // 코스 키워드, 순서대로 /이색, 일상/체력높음,체력낮음/액티비티, 앉아서놀기/
            int N = 0; // N보다 높거나 낮은것을 따지므로
            bigCourse = firstCourseKeywordFiltering(firstCourse, bigCourse, N);
            // 코스 키워드인 수치로까지 필터링

            if(!wantedCategory.equals("없음")) { // 가고 싶은 카테고리 입력한 경우
                firstCourse.setMainBigCategory(wantedCategory); // wantedCategory를 MainBigCategory로 지정
            }
            else { // 가고 싶은 카테고리 입력 안 한 경우
                // C열의 카테고리들의 우선순위에 따라 main 중 하나를 MainBigCategory로 지정
                //for

            }
        }

        else if ((firstCourse.getFinishTime() - firstCourse.getStartTime()) <= 12) { // 21시간 이하이면 메인(3시간)4개

            for (BigCourseOutline bigCourseOutline : bigCourseOutlines) { // 코스틀 전체에서 코스 하나하나 가져오기

                String CourseOutline = bigCourseOutline.getCourseOutline(); // 객체에서 코스만 뽑아오기
                String[] Courses = CourseOutline.split(","); // 코스에서 ,로 분리 -> 놀기Main스포츠 / 관계sub테마카페

                if(Courses.length == 4) { // 4개 짜리 - Main(3)+Main(3)+Main(3)+Main(3)
                    if( ( (  Courses[0].contains("Main") && !( Courses[0].contains("관광명소") || Courses[0].contains("테마거리") || Courses[0].contains("풍경") )  ) && (  Courses[1].contains("Main") && !( Courses[1].contains("관광명소") || Courses[1].contains("테마거리") || Courses[1].contains("풍경") )  ) && (  Courses[2].contains("Main") && !( Courses[2].contains("관광명소") || Courses[2].contains("테마거리") || Courses[2].contains("풍경") )  ) && (  Courses[3].contains("Main") && !( Courses[3].contains("관광명소") || Courses[3].contains("테마거리") || Courses[3].contains("풍경") )  ) ) // Main(3)+Main(3)+Main(3)+Main(3)
                    ) {
                        bigCourse.add(bigCourseOutline); // 조건 만족하는 bigCourseOutline 전부를 bigCourse에 대입
                    }
                }

            }

            // 시간에 따른 개수로 필터링 한 결과만 남음
            // Main 1개나, Sub 2개짜리로 이루어진 모든 코스틀이 bigCourse에 저장되어 있을 것

            // 코스 목적과 키워드에 따라 한 개의 C열로 구성된 코스 틀 뽑아내기

            // 코스 목적으로 포함되어야 하는 bigCategory 들어가도록 필터링
            bigCourse = firstCourseGoalFiltering(firstCourse, bigCourse);
            // 목적으로 까지 필터링한 것만 bigCourse에 남음

            // 코스 키워드, 순서대로 /이색, 일상/체력높음,체력낮음/액티비티, 앉아서놀기/
            int N = 0; // N보다 높거나 낮은것을 따지므로
            bigCourse = firstCourseKeywordFiltering(firstCourse, bigCourse, N);
            // 코스 키워드인 수치로까지 필터링

            if(!wantedCategory.equals("없음")) { // 가고 싶은 카테고리 입력한 경우
                firstCourse.setMainBigCategory(wantedCategory); // wantedCategory를 MainBigCategory로 지정
            }
            else { // 가고 싶은 카테고리 입력 안 한 경우
                // C열의 카테고리들의 우선순위에 따라 main 중 하나를 MainBigCategory로 지정
                //for

            }
        }




        return null;
    }

    @Override
    public SecondCourse secondCourseFiltering(FirstCourse firstCourse) {
        return null;
    }

    @Override
    public FinalCourse finalCourseFiltering(SecondCourse secondCourse) {
        return null;
    }


    /**
     * 코스 목적에 따른 필터링 메서드 - FirstCourse에서 사용
     * @param firstCourse
     * @param bigCourse
     * @return
     */
    private List<BigCourseOutline> firstCourseGoalFiltering(FirstCourse firstCourse, List<BigCourseOutline> bigCourse) {
        List<Integer> removeIdx = new ArrayList<>(); // 여기에 제거할거 인덱스만 모아두기
        for(int i=0; i<5; i++) { // 신첵, 음주, 체험, 동물, 힐링
            if(firstCourse.getGoal(i)) { // 목적 하나씩 가져와서 있는 경우만 필터링 진행
                switch (i) {
                    case 0: // 산책
                        for(int j=0; j< bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
                            if(!bigCourse.get(j).getFeature().equals("산책")) { // 산책을 가지지 않는다면
                                removeIdx.add(j);
                            }
                        }

                        while(removeIdx.size() == 0) {
                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
                        }
                        break;

                    case 1: // 음주
                        for(int j=0; j< bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
                            if(!bigCourse.get(j).getFeature().equals("음주")) { // 음주를 가지지 않는다면
                                removeIdx.add(j);
                            }
                        }

                        while(removeIdx.size() == 0) {
                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
                        }
                        break;

                    case 2: // 체험
                        for(int j=0; j< bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
                            if(!bigCourse.get(j).getFeature().equals("체험")) { // 체험을 가지지 않는다면
                                removeIdx.add(j);
                            }
                        }

                        while(removeIdx.size() == 0) {
                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
                        }
                        break;

                    case 3: // 동물
                        for(int j=0; j< bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
                            if(!bigCourse.get(j).getFeature().equals("동물")) { // 동물을 가지지 않는다면
                                removeIdx.add(j);
                            }
                        }

                        while(removeIdx.size() == 0) {
                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
                        }
                        break;

                    case 4: // 힐링
                        for(int j=0; j< bigCourse.size(); j++) { // 시간에 따른 개수로 필터링 한 것들
                            if(!bigCourse.get(j).getFeature().equals("힐링")) { // 힐링을 가지지 않는다면
                                removeIdx.add(j);
                            }
                        }

                        while(removeIdx.size() == 0) {
                            bigCourse.remove(removeIdx.get(removeIdx.size() - 1));//제거
                        }
                        break;
                }
            }
        }
        return bigCourse;
    }

    /**
     * 코스 키워드 필터링
     * @param firstCourse
     * @param bigCourse
     * @param N
     * @return
     */
    private List<BigCourseOutline> firstCourseKeywordFiltering(FirstCourse firstCourse, List<BigCourseOutline> bigCourse, int N) {
        // N보다 높거나 낮은것을 따지므로
        List<Integer> keywordRemoveIdx = new ArrayList<>(); // 여기에 제거할거 인덱스만 모아두기
        for(int i=0; i<8; i++) { // 신첵, 음주, 체험, 동물, 힐링
            if (firstCourse.getCourseKeyword(i)) { // 목적 하나씩 가져와서 있는 경우만 필터링 진행
                switch (i) {
                    case 0: // 이색
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
                            if (!(bigCourse.get(j).getSpecification() > N)) { // N보다 특이도가 높다면 코스 키워드는 이색
                                keywordRemoveIdx.add(j); //키워드가 이색인 경우인데 코스의 특이도가 N보다 크지 않으면 제거
                            }
                        }

                        while (keywordRemoveIdx.size() == 0) {
                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
                        }
                        break;

                    case 1: // 일상
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
                            if (!(bigCourse.get(j).getSpecification() <= N)) { // N보다 특이도가 낮거나 같다면 코스 키워드는 일상
                                keywordRemoveIdx.add(j); //키워드가 일상인 경우인데 코스의 특이도가 N보다 낮거나 같지 않으면 제거
                            }
                        }

                        while (keywordRemoveIdx.size() == 0) {
                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
                        }
                        break;

                    case 2: // 체력높음
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
                            if (!(bigCourse.get(j).getFatigability() > N)) { // N보다 피로도가 높다면 코스 키워드는 체력높음
                                keywordRemoveIdx.add(j); //키워드가 체력높음인 경우인데 코스의 특이도가 N보다 크지 않으면 제거
                            }
                        }

                        while (keywordRemoveIdx.size() == 0) {
                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
                        }
                        break;

                    case 3: // 체력낮음
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
                            if (!(bigCourse.get(j).getFatigability() <= N)) { // N보다 피로도가 낮거나 같다면 코스 키워드는 체력낮음
                                keywordRemoveIdx.add(j); //키워드가 체력낮음인 경우인데 코스의 특이도가 N보다 낮거나 같지 않으면 제거
                            }
                        }

                        while (keywordRemoveIdx.size() == 0) {
                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
                        }
                        break;

                    case 4: // 액티비티
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
                            if (!(bigCourse.get(j).getActivity() > N)) { // N보다 활동성이 높다면 코스 키워드는 액티비티
                                keywordRemoveIdx.add(j); //키워드가 액티비티인 경우인데 코스의 활동성이 N보다 높지 않으면 제거
                            }
                        }

                        while (keywordRemoveIdx.size() == 0) {
                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
                        }
                        break;

                    case 5: // 앉아서 놀기
                        for (int j = 0; j < bigCourse.size(); j++) { // 시간, 목적에 따른 개수로 필터링 한 것들
                            if (!(bigCourse.get(j).getActivity() <= N)) { // N보다 활동성이 높다면 코스 키워드는 앉아서놀기
                                keywordRemoveIdx.add(j); //키워드가 앉아서놀기인 경우인데 코스의 활동성이 N보다 낮거나 같지 않으면 제거
                            }
                        }

                        while (keywordRemoveIdx.size() == 0) {
                            bigCourse.remove(keywordRemoveIdx.get(keywordRemoveIdx.size() - 1));//제거
                        }
                        break;
                }
            }
        }
        return bigCourse;
    }
}
