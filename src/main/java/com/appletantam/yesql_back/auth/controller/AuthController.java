package com.appletantam.yesql_back.auth.controller;

import com.appletantam.yesql_back.config.response.BaseResponse;
import com.appletantam.yesql_back.config.response.BaseResponseStatus;
import com.appletantam.yesql_back.auth.dto.UserDTO;
import com.appletantam.yesql_back.auth.service.AuthService;
import com.appletantam.yesql_back.manage.dto.UserDatabaseDTO;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    private UserDTO userDTO;

    @GetMapping("/test")
    public String signUp() {
        return "test success";
    }

    /*
     *  @ModelAttribute
     *
     *  - @ModelAttribute 어노테이션을 사용하여 HTML element name 과 DTO property가 일치된 경우에
     *     DTO 형식으로 바인딩(매핑) 된 전달받을 수 있다.
     *
     *  - @ModelAttribute 의 경우 내부적으로 검증(Validation) 작업을 진행하기 때문에 setter 메서드를 이용하여 값을 바인딩하려고 시도하다가
     *   예외를 만날때(데이터 타입 불일치) 작업이 중단되면서 Http 400 Bad Request가 발생한다.
     *
     *  - String to Date 데이터 형식의 바인딩은 DTO클래스 property위에 @DateTimeFormat(pattern = "yyyy-MM-dd")을 추가하여 매핑한다.
     */

    /*
    * BaseResponse 값에 들어갈 result 값이 <K,V> 형식으로 들어가기 위해서는 무조건 Map 이나 DTO 객체여야지 변수명까지 같이 넘어간다
    * success의 종류도 message를 통해 다양하게 해야 전달된 값으로 뭘 할지 표기를 해줄 수 있을 것 같음
    * isSuccess를 통해 값 전달의 실패 성공 여부를 따지고, code와 message로 종류를 세밀하게 할 수 있지 않을까 ?
    *
    * ajax를 통해 이용할 비동기 api는 단순 값 전달, 그 외는 BaseResponse 객체를 이용하는 것을 주축으로 둬야할듯
    */

    //회원가입
    @PostMapping("/register")
    public BaseResponse<UserDTO> register(@ModelAttribute UserDTO userDTO) {
        UserDTO newUser = authService.addUser(userDTO);
        if ( checkDuplicatedId(userDTO.getUserId()) ){
            return new BaseResponse<>(BaseResponseStatus.REGISTER_SUCCESS, newUser);
        }
        else {
            return new BaseResponse<>(BaseResponseStatus.REGISTER_FAILED);
        }
    }

    //중복아이디 검사 (ajax)
    @PostMapping("/checkDuplicatedId")
    public boolean checkDuplicatedId(@RequestParam("userId") String userId){
        return authService.checkDuplicatedId(userId);
    }

    //로그인
    @PostMapping("/login")
    public BaseResponse<Map> login (@ModelAttribute UserDTO userDTO){
        String userId = userDTO.getUserId();

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        if (authService.login(userDTO)){
            return new BaseResponse<>(BaseResponseStatus.LOGIN_SUCCESS, map);
        }
        else{
            return new BaseResponse<>(BaseResponseStatus.LOGIN_INCORRECT);
        }

    }

    //유저 데이터베이스 찾기
    @GetMapping("/findDB")
    public BaseResponse<UserDatabaseDTO> findDatabase(@RequestParam String userId){

        UserDatabaseDTO userDatabaseDTO = authService.findDatabase(userId);
        if ( userDatabaseDTO.getDbName() != null ){ //유저의 데이터 베이스가 존재하는 경우
            return new BaseResponse<>(BaseResponseStatus.FIND_DATABASE, userDatabaseDTO);
        }
        else { // 존재하지 않는 경우 null을 출력하고 프론트에서 welcome 창으로 이동해주기 ~
            return new BaseResponse<>(BaseResponseStatus.NO_DATABASE);
        }

    }

    //유저 데이터베이스 생성
    @PostMapping("/createDB")
    public BaseResponse<UserDatabaseDTO> createDB(@RequestParam("dbName") String dbName, @RequestParam("userId") String userId){
        //유저가 로그인 되어있는 경우에만 등록이 가능하니까.. userId가 존재하는지 안하는지 확인할 필요 없음
        //dbName이 다른 유저와 중복되도 상관없음 userCd로 분류하기 때문

        // 유저 데이터베이스의 이름과, 유저 아이디를 통해 데이터베이스 만들기
        authService.createDB(dbName, userId);

        //dbCd를 세션에 박아둬야됨 -> 그에따른 데이터 정보를 main_scroll view에 보여줘야하기 때문
        return findDatabase(userId);
    }

    //로그아웃
    @GetMapping("/logout")
    public void logout(HttpServletRequest request){

        //세션 invalidate (프론트에서? 백에서?)
        HttpSession session = request.getSession();
        session.invalidate();

    }
}
