package com.graduatepj.enol.member.service;

import com.graduatepj.enol.makeCourse.dao.CourseV2Repository;
import com.graduatepj.enol.makeCourse.dao.PlaceRepository;
import com.graduatepj.enol.makeCourse.dao.RestaurantRepository;
import com.graduatepj.enol.makeCourse.dto.PlaceDto;
import com.graduatepj.enol.makeCourse.vo.CourseV2;
import com.graduatepj.enol.makeCourse.vo.Restaurant;
import com.graduatepj.enol.member.dao.*;
import com.graduatepj.enol.member.dto.*;
import com.graduatepj.enol.member.vo.History;
import com.graduatepj.enol.member.vo.User;
import com.graduatepj.enol.member.vo.UserMark;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service("memberService")
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

//    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl1.class); // 로거 띄우기 위해

    private final MemberRepository memberRepository; // jpa 사용할 repository

    private final UserHistoryRepository userHistoryRepository;

    private final UserMarkRepository userMarkRepository;

    private final UserRepository userRepository;

    private final CourseV2Repository courseV2Repository;

    private final PlaceRepository placeRepository;

    private final RestaurantRepository restaurantRepository;


    /**
     * 회원가입 메서드 - 아이디 중복 확인은 프론트에서 진행해서 중복 없을 것이라 생각
     * @param userDto
     * @return
     */
    @Override
    public User joinUser(UserDto userDto) {
        log.info("--- joinUser Serviceimpl START ---");

        // 입력된 정보 확인
        log.info("--- Show userRequest START ---");
        log.info("userDto.getMemberId = {}", userDto.getId());
        log.info("userDto.getpw = {}", userDto.getPw());
        log.info("userDto.getName = {}", userDto.getName());
        log.info("userDto.getEmail = {}", userDto.getEmail());
        log.info("userDto.getAddressName = {}", userDto.getAddressName());
        log.info("userDto.getBirthDate = {}", userDto.getBirthDate());
        log.info("userDto.getGender = {}", userDto.getGender());
        log.info("userDto.getPrefFatigue = {}", userDto.getPrefFatigue());
        log.info("userDto.getPrefUnique = {}", userDto.getPrefUnique());
        log.info("userDto.getPrefActivity = {}", userDto.getPrefActivity());
        log.info("--- Show userRequest END ---");

        User newUser = new User();
        newUser.setId(userDto.getId());
        newUser.setPw(userDto.getPw());
        newUser.setName(userDto.getName());
//        newUser.setAddressName(userDto.getAddressName());
        newUser.setAddressName(""); // 입력 안받기로 했으므로 빈 문자열 넣기로 수정
        newUser.setEmail(userDto.getEmail());
        newUser.setGender(userDto.getGender());
        newUser.setBirthDate(userDto.getBirthDate());
        newUser.setPrefFatigue(userDto.getPrefFatigue());
        newUser.setPrefUnique(userDto.getPrefUnique());
        newUser.setPrefActivity(userDto.getPrefActivity());

        String userCode = makeUserCode(userDto.getName()); // userCode 중복되지 않게 만드는 메서드
        newUser.setUserCode(userCode);

        LocalDate localDate = LocalDate.now(); // 현재 날짜 생성
        String nowDate = localDate.toString(); // String으로 형 변환
        newUser.setJoinDate(nowDate);
        newUser.setLastDate(nowDate); // lastDate도 null 안주기 위해 일단 joinDate와 같도록

        // save 할 정보 확인
        log.info("--- Show newUser START ---");
        log.info("newUser.getUserCode = {}", newUser.getUserCode());
        log.info("newUser.getMemberId = {}", newUser.getId());
        log.info("newUser.getPw = {}", newUser.getPw());
        log.info("newUser.getName = {}", newUser.getName());
        log.info("newUser.getEmail = {}", newUser.getEmail());
        log.info("newUser.getAddressName = {}", newUser.getAddressName());
        log.info("newUser.getGender = {}", newUser.getGender());
        log.info("newUser.getBirthDate = {}", newUser.getBirthDate());
        log.info("newUser.getJoinDate = {}", newUser.getJoinDate()); // 현재 날짜 추가
        log.info("newUser.getLastDate = {}", newUser.getLastDate()); // 현재 날짜 추가
        log.info("newUser.getPrefFatigue = {}", newUser.getPrefFatigue());
        log.info("newUser.getPrefUnique = {}", newUser.getPrefUnique());
        log.info("newUser.getPrefActivity = {}", newUser.getPrefActivity());
        log.info("--- Show newUser END ---");

        userRepository.save(newUser); // user 테이블에 저장

        // userMark 테이블에 저장 - userCode만 넣고 나머지는 빈 값
        UserMark userMark = new UserMark();
        List<String> courseIds = new ArrayList<>();
        List<Long> placeIds = new ArrayList<>();
        List<String> friendCodes = new ArrayList<>();
        userMark.setUserCode(newUser.getUserCode());

        userMark.setCourseIds(courseIds); // 빈 리스트로 채우기
        userMark.setPlaceIds(placeIds); // 빈 리스트로 채우기
        userMark.setFriendCodes(friendCodes); // 빈 리스트로 채우기
        userMarkRepository.save(userMark);

        // userHistory 테이블에 저장 - userCode와 number=0만 넣고 나머지는 빈값
        History history = new History();
        List<History.HistoryCourse> historyCourseList = new ArrayList<>();
        History.HistoryCourse historyCourse = new History.HistoryCourse();
//        String historyCourseCourseId = "";
        List<Long> historyCoursePlaceIds = new ArrayList<>();
        double historyCourseRating = 0.0;


        historyCourse.setCourseId(""); // 빈 string으로 채우기
        historyCourse.setPlaceIds(historyCoursePlaceIds); // 빈 리스트로 채우기
        historyCourse.setRating(historyCourseRating); // 0.0으로 초기화
        historyCourse.setOrder(""); // 빈 string으로 채우기

        historyCourseList.add(historyCourse);

        history.setUserCode(newUser.getUserCode());
        history.setNumber(0);

        history.setCourse(historyCourseList); // HistoryCourse로 채우기
        userHistoryRepository.save(history);

        log.info("--- Save New User Success ---");
        return newUser;
    }

    /**
     * 중복되지 않는 userCode를 만드는 메서드
     * @param userName
     * @return
     */
//    private String makeUserCode2(String userName) {
//        int idx = 1;
//        String userCode = userName+"#0"+idx;
//        List<String> sameUserCode = userRepository.findUserCode(userCode);
//        if(sameUserCode.size()==0) {
//            log.info("In makeUserCode 1 - userCode = {}", userCode);
//        }
//        else {
//            idx = 2;
//            while(sameUserCode.size()!=0) {
//                userCode = userName+"#0"+idx;
//                sameUserCode = userRepository.findUserCode(userCode);
//                idx++;
//            }
//            log.info("In makeUserCode 2 - userCode = {}", userCode);
//        }
//        return userCode;
//    }

    private String makeUserCode(String userName) {
        int idx = 1;
        String userCode = userName+"#0"+idx;
        List<User> sameUserCode = userRepository.findUserCodeByName(userName);
        if(sameUserCode.size()==0) {
            log.info("In makeUserCode 1 - userCode = {}", userCode);
        }
        else {
            int maxNumber=0;
            for(User code : sameUserCode){
                String numberStr = code.getUserCode().substring(userCode.lastIndexOf("#") + 1);
                int number = Integer.parseInt(numberStr);

                if (number > maxNumber) {
                    maxNumber = number;
                }
            }
            if(maxNumber<10){
                userCode=userName+"#0"+maxNumber;
            }else{
                userCode=userName+"#"+maxNumber;
            }
            log.info("In makeUserCode 2 - userCode = {}", userCode);
        }
        return userCode;
    }


    /**
     * 아이디 찾기
     * @param userDto
     * @return
     */
    @Override
    public User checkUserId(UserDto userDto) {
        log.info("--- checkUserId memberServiceImpl START ---");

        // 입력된 정보 확인
        log.info("--- Show userRequest START ---");
        log.info("userRequest.getMemberId = {}", userDto.getId());
        log.info("userRequest.getpw = {}", userDto.getPw());
        log.info("userRequest.getName = {}", userDto.getName());
        log.info("userRequest.getEmail = {}", userDto.getEmail());
        log.info("userRequest.getAddressName = {}", userDto.getAddressName());
        log.info("userRequest.getBirthDate = {}", userDto.getBirthDate());
        log.info("userRequest.getGender = {}", userDto.getGender());
        log.info("userRequest.getPrefFatigue = {}", userDto.getPrefFatigue());
        log.info("userRequest.getPrefUnique = {}", userDto.getPrefUnique());
        log.info("userRequest.getPrefActivity = {}", userDto.getPrefActivity());
        log.info("--- Show userRequest END ---");

        List<User> userList = userRepository.findByEmailAndNameAndBirthDateAndGender(userDto.getEmail(), userDto.getName(), userDto.getBirthDate(), userDto.getGender());
        log.info("userList.size = {}, (userList.size()==1) = {}", userList.size(), (userList.size()==1));
        log.info("userList.get(0) = {}", userList.get(0));
        log.info("userList.get(0).getId = {}", userList.get(0).getId());
        if(userList.size()==1) { // 1개만 있는 경우 해당 id 반환
            log.info("find User Id = {}", userList.get(0).getId());
            return userList.get(0);
        }
        log.info("No Same ID - error");
        return null; // 같은 아이디가 없거나 그럼에도 2개 이상인 경우
    }

    /**
     * 비밀번호 찾기
     * @param userDto
     * @return
     */
    @Override
    public User findUserPassword(UserDto userDto) {
        log.info("--- findUserPassword memberServiceImpl START ---");

        // 입력된 정보 확인
        log.info("--- Show userRequest START ---");
        log.info("userRequest.getMemberId = {}", userDto.getId());
        log.info("userRequest.getpw = {}", userDto.getPw());
        log.info("userRequest.getName = {}", userDto.getName());
        log.info("userRequest.getEmail = {}", userDto.getEmail());
        log.info("userRequest.getAddressName = {}", userDto.getAddressName());
        log.info("userRequest.getBirthDate = {}", userDto.getBirthDate());
        log.info("userRequest.getGender = {}", userDto.getGender());
        log.info("userRequest.getPrefFatigue = {}", userDto.getPrefFatigue());
        log.info("userRequest.getPrefUnique = {}", userDto.getPrefUnique());
        log.info("userRequest.getPrefActivity = {}", userDto.getPrefActivity());
        log.info("--- Show userRequest END ---");

        List<User> userList = userRepository.findByIdAndEmailAndNameAndBirthDateAndGender(userDto.getId(), userDto.getEmail(), userDto.getName(), userDto.getBirthDate(), userDto.getGender());

        log.info("userList.size = {}", userList.size());
        if(userList.size()==1) { // 1개 딱 찾으면 해당 객체 반환
            return userList.get(0);
        }

        log.info("No Same Id - ERROR");
        return null;

    }

    /**
     * 비밀번호 바꾸기
     * @param userDto
     * @param changePW
     * @return
     */
    @Override
    public User ChangeUserPassword(UserDto userDto, String changePW) {
        log.info("--- changeUserPassword memberServiceImpl START ---");

        // 입력된 정보 확인
        log.info("--- Show userRequest START ---");
        log.info("userRequest.getUserCode = {}", userDto.getUserCode());
        log.info("userRequest.getMemberId = {}", userDto.getId());
        log.info("userRequest.getpw = {}", userDto.getPw());
        log.info("userRequest.getName = {}", userDto.getName());
        log.info("userRequest.getEmail = {}", userDto.getEmail());
        log.info("userRequest.getAddressName = {}", userDto.getAddressName());
        log.info("userRequest.getBirthDate = {}", userDto.getBirthDate());
        log.info("userRequest.getGender = {}", userDto.getGender());
        log.info("userRequest.getPrefFatigue = {}", userDto.getPrefFatigue());
        log.info("userRequest.getPrefUnique = {}", userDto.getPrefUnique());
        log.info("userRequest.getPrefActivity = {}", userDto.getPrefActivity());
        log.info("--- Show userRequest END ---");

        List<User> userList = userRepository.findAllById(userDto.getId());
        log.info("userList.size = {}", userList.size());
        if(userList.size()==1) { // id에 해당하는 User가 한개이면 거기 비밀번호만 수정해주면 됨
            User changeUser = new User();
            changeUser.set_id(userList.get(0).get_id()); // _id 안넣으면 save에서 바뀌는게 아니라 새로 추가됨
            changeUser.setUserCode(userDto.getUserCode());
            changeUser.setId(userDto.getId());
            changeUser.setPw(changePW);
            changeUser.setName(userDto.getName());
            changeUser.setAddressName(userDto.getAddressName());
            changeUser.setEmail(userDto.getEmail());
            changeUser.setBirthDate(userDto.getBirthDate());
            changeUser.setGender(userDto.getGender());
            changeUser.setJoinDate(userList.get(0).getJoinDate());
            changeUser.setLastDate(userList.get(0).getLastDate());
            changeUser.setPrefFatigue(userDto.getPrefFatigue());
            changeUser.setPrefUnique(userDto.getPrefUnique());
            changeUser.setPrefActivity(userDto.getPrefActivity());

            userRepository.save(changeUser); // id 때문에 새로운 객체가 생김
            log.info("changePassword success");
            return changeUser;
        }

        log.info("changePassword memberServiceImpl END - ERROR");
        return null;
    }

    /**
     * 사용자 취향 변경하는 메서드, 비밀번호나 취향같은 사용자 정보 수정도 가능할 듯
     * @param userDto
     * @return
     */
    @Override
    public User modifyMemberInfo(UserDto userDto) { // 비밀번호 변경, 취향 변경 -

        log.info("--- modifyMemberInfo memberServiceImpl START ---");
        // 입력된 정보 확인
        log.info("--- Show userRequest START ---");
        log.info("userRequest.getUserCode = {}", userDto.getUserCode());
        log.info("userRequest.getMemberId = {}", userDto.getId());
        log.info("userRequest.getpw = {}", userDto.getPw());
        log.info("userRequest.getName = {}", userDto.getName());
        log.info("userRequest.getEmail = {}", userDto.getEmail());
        log.info("userRequest.getAddressName = {}", userDto.getAddressName());
        log.info("userRequest.getBirthDate = {}", userDto.getBirthDate());
        log.info("userRequest.getGender = {}", userDto.getGender());
        log.info("userRequest.getPrefFatigue = {}", userDto.getPrefFatigue());
        log.info("userRequest.getPrefUnique = {}", userDto.getPrefUnique());
        log.info("userRequest.getPrefActivity = {}", userDto.getPrefActivity());
        log.info("--- Show userRequest END ---");

        List<User> userList = userRepository.findAllById(userDto.getId()); // id로 회원 정보 가져오기
        log.info("userList.size = {}", userList.size());
        if(userList.size()==1) {
            User modifyUser = new User();
            modifyUser.set_id(userList.get(0).get_id());
            modifyUser.setUserCode(userDto.getUserCode());
            modifyUser.setId(userDto.getId());
            modifyUser.setPw(userDto.getPw());
            modifyUser.setName(userDto.getName());
            modifyUser.setEmail(userDto.getEmail());
            modifyUser.setAddressName(userDto.getAddressName());
            modifyUser.setGender(userDto.getGender());
            modifyUser.setBirthDate(userDto.getBirthDate());
            modifyUser.setJoinDate(userList.get(0).getJoinDate());
            modifyUser.setLastDate(userList.get(0).getLastDate());
            modifyUser.setPrefFatigue(userDto.getPrefFatigue());
            modifyUser.setPrefUnique(userDto.getPrefUnique());
            modifyUser.setPrefActivity(userDto.getPrefActivity());
            userRepository.save(modifyUser);
            log.info("modify user {}", modifyUser.getId());
            return modifyUser;
        }

        log.info("--- modifyMemberInfo memberServiceImpl END - ERROR ---");
        return null;
    }

    @Override
    public boolean deleteMember(UserDto userDto) { // 계정 삭제
        log.info("--- deleteMember memberServiceImpl START ---");

        // 입력된 정보 확인
        log.info("--- Show userRequest START ---");
        log.info("usreRequest.getUserCode = {}", userDto.getUserCode());
        log.info("userRequest.getMemberId = {}", userDto.getId());
        log.info("userRequest.getpw = {}", userDto.getPw());
        log.info("userRequest.getName = {}", userDto.getName());
        log.info("userRequest.getEmail = {}", userDto.getEmail());
        log.info("userRequest.getAddressName = {}", userDto.getAddressName());
        log.info("userRequest.getBirthDate = {}", userDto.getBirthDate());
        log.info("userRequest.getGender = {}", userDto.getGender());
        log.info("userRequest.getPrefFatigue = {}", userDto.getPrefFatigue());
        log.info("userRequest.getPrefUnique = {}", userDto.getPrefUnique());
        log.info("userRequest.getPrefActivity = {}", userDto.getPrefActivity());
        log.info("--- Show userRequest END ---");

        List<User> userList = userRepository.findAllById(userDto.getId());
        log.info("userList.size = {}", userList.size());
        if(userList.size()==1) {
            log.info("--- DELETE USER {} ---", userDto.getId());
            userRepository.delete(userList.get(0)); // User 에서 제거

            // UserMark에서 제거
            List<UserMark> userMarkList = userMarkRepository.findAllByUserCode(userDto.getUserCode()); // 메서드 만들어야 함
            log.info("userMarkList.size = {}, userDto.userCode = {}", userMarkList.size(), userDto.getUserCode());
            if(userMarkList.size()==1) {
                log.info("--- DELETE USER MARK {} ---", userDto.getId());
                userMarkRepository.delete(userMarkList.get(0));
            }

            // History에서 제거
            List<History> historyList = userHistoryRepository.findAllByUserCode(userDto.getUserCode()); // 메서드 만들어야 함
            log.info("history.size = {}, userDto.userCode = {}", historyList.size(), userDto.getUserCode());
            if(historyList.size()==1) {
                log.info("--- DELETE USER HISTORY {} ---", userDto.getId());
                userHistoryRepository.delete(historyList.get(0));
            }


            return true;
        }

        log.info("--- deleteMember memberServiceImpl END - ERROR ---");
        return false;

    }

    @Override
    public List<User> showMembers() {
        log.info("--- showMembers memberServiceImpl start ---");
        List<User> userList = userRepository.findAll(); // 전체 user 정보 다 불러오기
        for(User user : userList) {
            log.info("user = {}", user.toString());
        }
        return userList;

    }

    @Override
    public List<List<PlaceDto>> getBookmarkCourseById(String userCode){
//        // 뭘 저장하려는건지는 모르겠다마는 여기 수정해야할듯?
//        List<String> courseIds = getUserMarkById(userCode).getCourseIds();
//        List<BookmarkCourseDto> bookmarkCourseList = new ArrayList<>();
//        if(courseIds==null){
//            return bookmarkCourseList;
//        }
//        for(String id : courseIds){
//            CourseV2 course = courseV2Repository.findById(id)
//                    .orElseThrow(() -> new RuntimeException("코스를 찾을 수 없습니다."));
//
//        }
        return null;
    }

    // 장소 즐겨찾기
    @Override
    public List<PlaceDto> getBookmarkPlaceById(String userCode){
        List<Long> placeIds = getUserMarkById(userCode).getPlaceIds();
        List<PlaceDto> places=new ArrayList<>();
        if(placeIds == null){
            return places;
        }
        for(Long id:placeIds){
            places.add(PlaceDto.fromEntity(
                    placeRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("가게정보를 찾을 수 없습니다."))));
        }
        return places;
    }

    @Override
    public String addBookmarkPlace(BookmarkPlaceDto bookmarkPlaceDto){
        UserMark userMark = userMarkRepository.findUserMarkByUserCode(bookmarkPlaceDto.getUserCode())
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));
        userMark.getPlaceIds().add(bookmarkPlaceDto.getPlaceId());
        userMarkRepository.save(userMark);
        return "해당 장소를 즐겨찾기에 추가했습니다.";
    }

    // 속성 가져오기
    @Override
    public UserPreferenceDto getPreferencesById(String userCode){
        return UserPreferenceDto.from(userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("getPreferencesById method failed")));
    }

    // 히스토리 보여주기
    // History에서 course_id는 필요없어 보여서 제외, number와 각 placeId에 해당하는 가게정보 및 코스에 대해 평가했던 평점 반환
    @Override
    public HistoryDto getHistoryById(String userCode){
        HistoryDto historyList = new HistoryDto();
        List<HistoryDto.HistoryCourseDto> course = new ArrayList<>();
        History history = userHistoryRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("getHistoryById method failed"));
        for (int i = 0; i < history.getNumber(); i++) {
            History.HistoryCourse historyCourse = history.getCourse().get(i);
            HistoryDto.HistoryCourseDto historyCourseDto = new HistoryDto.HistoryCourseDto();
            historyCourseDto.setPlaces(new ArrayList<>());
            historyCourseDto.setPlaceIds(new ArrayList<>());
            for(Long placeId : historyCourse.getPlaceIds()){
                PlaceDto placeDto = PlaceDto.fromEntity(placeRepository.findById(placeId).orElse(null));
                if(placeDto == null){
                    Restaurant restaurant = restaurantRepository.findById(placeId)
                            .orElseThrow(() -> new RuntimeException("가게정보를 찾을 수 없습니다."));
                    placeDto = PlaceDto.builder()
                            .id(restaurant.getId())
                            .placeName(restaurant.getPlaceName())
                            .categoryName(restaurant.getCategoryCode())
                            .addressName(restaurant.getAddressName())
                            .x(restaurant.getX())
                            .y(restaurant.getY())
                            .build();
                }
                historyCourseDto.getPlaceIds().add(placeId);
                historyCourseDto.getPlaces().add(placeDto);
            }
            historyCourseDto.setOrder(historyCourse.getOrder());
            historyCourseDto.setRating(historyCourse.getRating());
            course.add(historyCourseDto);
        }
        historyList.setNumber(history.getNumber());
        historyList.setCourse(course);
        return historyList;
    }

    // 친구목록 보여주기
    @Override
    public List<FriendDto> getFriendsList(String userCode){
        List<String> friendsCode = getUserMarkById(userCode)
                .getFriendCodes();
        List<FriendDto> friendList=new ArrayList<>();
        if(friendsCode==null){
            return friendList;
        }
        for(String code:friendsCode){
            UserDto user = getUserInfo(code);
            FriendDto friend = FriendDto.builder()
                    .name(user.getName())
                    .userCode(user.getUserCode())
                    .gender((user.getGender()))
                    .build();
            friendList.add(friend);
        }
        return friendList;
    }

    private UserDto getUserInfo(String userCode){
        return UserDto.from(userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("유저정보를 불러올 수 없습니다.")));
    }

    @Override
    public UserMarkDto getUserMarkById(String userCode){
        return UserMarkDto.from(userMarkRepository.findUserMarkByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("친구목록을 불러올 수 없습니다.")));
    }

    @Override
    public String deleteFriend(FriendRequestDto friendRequestDto){
        UserMark userMark = userMarkRepository.findUserMarkByUserCode(friendRequestDto.getUserCode())
                .orElseThrow(() -> new RuntimeException("해당 유저의 히스토리를 불러올 수 없습니다."));
        userMark.getFriendCodes().remove(friendRequestDto.getFriendCode());
        userMark.getFriendCodes().sort(Collator.getInstance(new Locale("ko", "KOREA")));
        userMarkRepository.save(userMark);
        return "친구삭제를 성공했습니다.";
    }

    @Override
    public String addFriend(FriendRequestDto friendRequestDto){
        if(userMarkRepository.findUserMarkByUserCode(friendRequestDto.getFriendCode()).isEmpty()){
            throw new RuntimeException("해당 친구 유저가 존재하지 않습니다.");
        }else if(friendRequestDto.getUserCode().equals(friendRequestDto.getFriendCode())){
            throw new RuntimeException("자기 자신은 추가할 수 없습니다.");
        }
        UserMark userMark = userMarkRepository.findUserMarkByUserCode(friendRequestDto.getUserCode())
                .orElseThrow(() -> new RuntimeException("해당 유저의 히스토리를 불러올 수 없습니다."));
        if(userMark.getFriendCodes().contains(friendRequestDto.getFriendCode())){
            throw new RuntimeException("해당 유저와 이미 친구입니다.");
        }
        userMark.getFriendCodes().add(friendRequestDto.getFriendCode());
        userMark.getFriendCodes().sort(Collator.getInstance(new Locale("ko", "KOREA")));
        userMarkRepository.save(userMark);
        return "친구추가를 성공했습니다.";
    }

    @Override
    public FriendDto searchFriend(String friendCode){
        return FriendDto.from(userRepository.findByUserCode(friendCode)
                .orElseThrow(() -> new RuntimeException("친구를 찾을 수 없습니다.")));
    }

}
