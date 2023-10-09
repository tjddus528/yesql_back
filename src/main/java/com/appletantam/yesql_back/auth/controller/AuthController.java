package com.appletantam.yesql_back.auth.controller;

import com.appletantam.config.response.BaseException;
import com.appletantam.config.response.BaseResponse;
import com.appletantam.yesql_back.auth.dto.UserDTO;
import com.appletantam.yesql_back.auth.service.AuthService;
import com.appletantam.yesql_back.manage.dto.UserDatabaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.appletantam.config.response.BaseResponseStatus.REQUEST_ERROR;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/test")
    public String signUp() {
        return "done.";
    }

    /*
     *  1) @ModelAttribute
     *
     *  - @ModelAttribute 어노테이션을 사용하여 HTML element name 과 DTO property가 일치된 경우에
     *     DTO 형식으로 바인딩(매핑) 된 전달받을 수 있다.
     *
     *  - @ModelAttribute 의 경우 내부적으로 검증(Validation) 작업을 진행하기 때문에 setter 메서드를 이용하여 값을 바인딩하려고 시도하다가
     *   예외를 만날때(데이터 타입 불일치) 작업이 중단되면서 Http 400 Bad Request가 발생한다.
     *
     *  - String to Date 데이터 형식의 바인딩은 DTO클래스 property위에 @DateTimeFormat(pattern = "yyyy-MM-dd")을 추가하여 매핑한다.
     */

    //회원가입
    @PostMapping("/register")
    public BaseResponse<UserDTO> register(@ModelAttribute UserDTO userDTO) {

        /*
        // 입력값 검사 -> 이 입력값 검사는 제이쿼리로 프론트에서 submit하기 전에 실시에서 controller로 안넘어오게 할 예정
        if(userDTO.getUserId() == null || userDTO.getUserPassword() == null) {
            return new BaseResponse<>(REQUEST_ERROR);
        }

        try{
            UserDTO newUser = authService.addUser(userDTO);
            return new BaseResponse<>(newUser);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }*/

        return null;
    }

    @PostMapping("/register2")
    public UserDTO register2 (UserDTO userDTO) throws BaseException {
        return authService.addUser(userDTO);
    }

    //중복아이디 검사
    @PostMapping("/checkDuplicatedId")
    public String checkDuplicatedId(@RequestParam("userId") String userId){
        return authService.checkDuplicatedId(userId);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<Object> login(HttpServletRequest request, @ModelAttribute UserDTO userDTO){

        String jsScript = "";

        //로그인 성공한 경우
        if ( authService.login(userDTO) ){
            // 세션에 로그인 정보 (유저아이디) 박아두기
            HttpSession session = request.getSession();
            session.setAttribute("userId", userDTO.getUserId());

            // 로그인 성공 팝업창 생성
            jsScript += "<script>";
            jsScript += "alert('login success!');";
            jsScript += "location.href='" + request.getContextPath() + "/LandingPage';";
            jsScript += "</script>";

        }
        // 로그인 실패한 경우
        else {

            // 로그인 실패 팝업창 생성
            jsScript += "<script>";
            jsScript += "alert('ID or password is incorrect :(');";
            jsScript += "location.href='" + request.getContextPath() + "/LoginPage';";
            jsScript += "</script>";

        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=utf-8");

        return new ResponseEntity<Object>(jsScript, responseHeaders, HttpStatus.OK);

    }


    //로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request){

        //세션 invalidate
        HttpSession session = request.getSession();
        session.invalidate();

        String jsScript = "<script>";
        jsScript += " alert('로그아웃 되었습니다.');";
        jsScript += "location.href='" + request.getContextPath() + "/';";
        jsScript += " </script>";

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=utf-8");

        return new ResponseEntity<Object>(jsScript, responseHeaders, HttpStatus.OK);

    }

    //유저 데이터베이스 생성
    @PostMapping("/createDB")
    public String createDB(@RequestParam("dbName") String dbName, @RequestParam("userId") String userId){
        //유저가 로그인 되어있는 경우에만 등록이 가능하니까.. userId가 존재하는지 안하는지 확인할 필요 없음
        //dbName이 다른 유저와 중복되도 상관없음 userCd로 분류하기 때문

        // 유저 데이터베이스의 이름과, 유저 아이디를 통해 데이터베이스 만들기
        authService.createDB(dbName, userId);

        //dbName도 세션에 박아둬야됨 -> 그에따른 데이터 정보를 main_scroll view에 보여줘야하기 때문
        return "success";
    }



}
