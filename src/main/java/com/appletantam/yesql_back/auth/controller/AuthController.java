package com.appletantam.yesql_back.auth.controller;

import com.appletantam.config.response.BaseException;
import com.appletantam.config.response.BaseResponse;
import com.appletantam.yesql_back.auth.dto.UserDTO;
import com.appletantam.yesql_back.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    private UserDTO userDTO;
    private Model model;

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
    public String register (UserDTO userDTO, Model model) {
        UserDTO newUser = authService.addUser(userDTO);
        model.addAttribute("message", "회원가입 완료");
        model.addAttribute("userId", userDTO.getUserId());

        return "newUser";
    }

    @PostMapping("/register2")
    public BaseResponse<UserDTO> register2(@ModelAttribute UserDTO userDTO) {
        UserDTO newUser = authService.addUser(userDTO);
        return new BaseResponse<>(newUser);
    }

    //중복아이디 검사
    @PostMapping("/checkDuplicatedId")
    public String checkDuplicatedId(@RequestParam("userId") String userId){
        return authService.checkDuplicatedId(userId);
    }

    //로그인
    @PostMapping("login")
    public BaseResponse<String> login (@ModelAttribute UserDTO userDTO){
        String userId = userDTO.getUserId();
        if (authService.login(userDTO)){
            return new BaseResponse<>(userId);
        }
        else{
            return null;
            //return new BaseResponse<>(2003);
        }
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
