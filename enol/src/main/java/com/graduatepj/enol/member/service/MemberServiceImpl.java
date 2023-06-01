package com.graduatepj.enol.member.service;

import com.graduatepj.enol.makeCourse.dao.CourseV2Repository;
import com.graduatepj.enol.makeCourse.dao.PlaceRepository;
import com.graduatepj.enol.makeCourse.dto.PlaceDto;
import com.graduatepj.enol.member.dao.*;
import com.graduatepj.enol.member.dto.HistoryDto;
import com.graduatepj.enol.member.dto.UserDto;
import com.graduatepj.enol.member.dto.UserPreferenceDto;
import com.graduatepj.enol.member.vo.Member;
import com.graduatepj.enol.member.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

//    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl1.class); // 로거 띄우기 위해

    private final MemberRepository memberRepository; // jpa 사용할 repository

    private final HistoryRepository historyRepository;

    private final UserMarkRepository userMarkRepository;

    private final UserPreferenceRepository userPreferenceRepository;

    private final UserRepository userRepository;

    private final CourseV2Repository courseV2Repository;

    private final PlaceRepository placeRepository;


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
        newUser.setAddressName(userDto.getAddressName());
        newUser.setEmail(userDto.getEmail());
        newUser.setGender(userDto.getGender());
        newUser.setBirthDate(userDto.getBirthDate());
        newUser.setPrefFatigue(userDto.getPrefFatigue());
        newUser.setPrefUnique(userDto.getPrefUnique());
        newUser.setPrefActivity(userDto.getPrefActivity());

        String userCode = makeUserCode(userDto.getName()); // userCode 중복되지 않게 만드는 메서드
        newUser.setUserCode(userCode);

        // save 할 정보 확인
        log.info("--- Show newUser START ---");
        log.info("newUser.getUserCode = {}", newUser.getUserCode());
        log.info("newUser.getMemberId = {}", newUser.getId());
        log.info("newUser.getPw = {}", newUser.getPw());
        log.info("newUser.getName = {}", newUser.getName());
        log.info("newUser.getEmail = {}", newUser.getEmail());
        log.info("newUser.getAddressName = {}", newUser.getAddressName());
        log.info("newUser.getBirthDate = {}", newUser.getBirthDate());
        log.info("newUser.getGender = {}", newUser.getGender());
        log.info("newUser.getPrefFatigue = {}", newUser.getPrefFatigue());
        log.info("newUser.getPrefUnique = {}", newUser.getPrefUnique());
        log.info("newUser.getPrefActivity = {}", newUser.getPrefActivity());
        log.info("--- Show newUser END ---");

        userRepository.save(newUser);
        log.info("--- Save New User Success ---");
        return newUser;
    }

    /**
     * 중복되지 않는 userCode를 만드는 메서드
     * @param userName
     * @return
     */
    private String makeUserCode(String userName) {
        int idx = 1;
        String userCode = userName+"#0"+idx;
        List<String> sameUserCode = userRepository.findUserCode(userCode);
        if(sameUserCode.size()==0) {
            log.info("In makeUserCode 1 - userCode = {}", userCode);
        }
        else {
            idx = 2;
            while(sameUserCode.size()!=0) {
                userCode = userName+"#0"+idx;
                sameUserCode = userRepository.findUserCode(userCode);
                idx++;
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

        List<User> userList = userRepository.findId(userDto.getEmail(), userDto.getName(), userDto.getBirthDate(), userDto.getGender());
        log.info("userList.size = {}", userList.size());
        if(userList.size()==0) { // 1개만 있는 경우 해당 id 반환
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

        List<User> userList = userRepository.findPw(userDto.getId(), userDto.getEmail(), userDto.getName(), userDto.getBirthDate(), userDto.getGender());

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

        List<User> userList = userRepository.findByUserId(userDto.getId());
        log.info("userList.size = {}", userList.size());
        if(userList.size()==1) { // id에 해당하는 User가 한개이면 거기 비밀번호만 수정해주면 됨
            User changeUser = new User();
            changeUser.setId(userDto.getId());
            changeUser.setPw(changePW);
            changeUser.setName(userDto.getName());
            changeUser.setAddressName(userDto.getAddressName());
            changeUser.setEmail(userDto.getEmail());
            changeUser.setBirthDate(userDto.getBirthDate());
            changeUser.setGender(userDto.getGender());
            changeUser.setPrefFatigue(userDto.getPrefFatigue());
            changeUser.setPrefUnique(userDto.getPrefUnique());
            changeUser.setPrefActivity(userDto.getPrefActivity());

            userRepository.save(changeUser);
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

        List<User> userList = userRepository.findByUserId(userDto.getId()); // id로 회원 정보 가져오기
        log.info("userList.size = {}", userList.size());
        if(userList.size()==1) {
            User modifyUser = new User();
            modifyUser.setId(userDto.getId());
            modifyUser.setPw(userDto.getPw());
            modifyUser.setName(userDto.getName());
            modifyUser.setEmail(userDto.getEmail());
            modifyUser.setGender(userDto.getGender());
            modifyUser.setBirthDate(userDto.getBirthDate());
            modifyUser.setAddressName(userDto.getAddressName());
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

        List<User> userList = userRepository.findByUserId(userDto.getId());
        log.info("userList.size = {}", userList.size());
        if(userList.size()==1) {
            log.info("--- DELETE USER {} ---", userDto.getId());
            userRepository.delete(userList.get(0));
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
        // 뭘 저장하려는건지는 모르겠다마는 여기 수정해야할듯?
        List<Long> courseIds = userMarkRepository.findCoursesById(userCode);
        for(Long id : courseIds){

        }
        return null;
    }

    // 여기도 어떤식으로 저장되어있는지를 잘 모르겠음
    @Override
    public List<PlaceDto> getBookmarkPlaceById(String userCode){
        List<Long> placeIds = userMarkRepository.findplacesById(userCode);
        List<PlaceDto> places=new ArrayList<>();
        for(Long id:placeIds){
            places.add(PlaceDto.fromEntity(
                    placeRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("findById failed"))));
        }
        return places;
    }

    @Override
    public UserPreferenceDto getPreferencesById(String userCode){
        return UserPreferenceDto.from(userRepository.findById(userCode)
                .orElseThrow(() -> new RuntimeException("getPreferencesById method failed")));
    }

    @Override
    public HistoryDto getHistoryById(String userCode){
        return HistoryDto.from(historyRepository.findById(userCode)
                .orElseThrow(() -> new RuntimeException("getHistoryById method failed")));
    }

    @Override
    public List<UserDto> getFriendsList(String userCode){
        List<String> friendsCode = getFriendsById(userCode);
        List<UserDto> friendList=new ArrayList<>();
        for(String code:friendsCode){
            friendList.add(getUserInfo(code));
        }
        return friendList;
    }

    private UserDto getUserInfo(String userCode){
        return UserDto.from(userRepository.findById(userCode)
                .orElseThrow(() -> new RuntimeException("getUserInfo method failed")));
    }

    @Override
    public List<String> getFriendsById(String userCode){
        return userMarkRepository.findFriendCodesById(userCode);
    }
}
