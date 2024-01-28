package com.appletantam.yesql_back.auth.controller;

import com.appletantam.yesql_back.config.response.BaseResponse;
import com.appletantam.yesql_back.config.response.BaseResponseStatus;
import com.appletantam.yesql_back.auth.dto.UserDTO;
import com.appletantam.yesql_back.auth.service.AuthService;
import com.appletantam.yesql_back.sqlManager.SqlConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    SqlConnector sqlConnector;
    @Value("${mysql.url}")
    public String mysqlUrl;
    @Value("${mysql.user}")
    public String user;
    @Value("${mysql.password}")
    public String password;
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

        if (checkDuplicatedId(userDTO.getUserId())){ // 중복 확인 끝난 경우
            // userId랑 dbName에 userId 값 넣어주기, userPassword에 비밀번호 넣어주기
            UserDTO newUser = authService.addUser(userDTO);
            String dbName = newUser.getUserId();

            // 1) 해당 유저의 DB 정보로 SqlConnector 생성 (USE dbName;)
            try {
                sqlConnector = new SqlConnector(mysqlUrl, user, password);
                sqlConnector.createDB(dbName);
            } catch (SQLException exception) {
                return new BaseResponse(false, exception.getMessage(), 3000,dbName);
            }

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

        // 로그인 성공시 해당 userId를 세션에 넣어주기 ( userId, dbName 동일함 )
        if (authService.login(userDTO)){
            return new BaseResponse<>(BaseResponseStatus.LOGIN_SUCCESS, map);
        }
        else{
            return new BaseResponse<>(BaseResponseStatus.LOGIN_INCORRECT);
        }

    }

    //로그아웃
    @GetMapping("/logout")
    public void logout(HttpServletRequest request){

        //세션 invalidate (프론트에서? 백에서?)
        HttpSession session = request.getSession();
        session.invalidate();

    }


}
