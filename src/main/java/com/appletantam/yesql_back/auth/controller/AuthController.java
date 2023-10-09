package com.appletantam.yesql_back.auth.controller;

import com.appletantam.config.response.BaseException;
import com.appletantam.config.response.BaseResponse;
import com.appletantam.yesql_back.auth.dto.UserDTO;
import com.appletantam.yesql_back.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<UserDTO> register(@RequestBody UserDTO userDTO) {

        // 입력값 검사
        if(userDTO.getUserId() == null || userDTO.getUserPassword() == null) {
            return new BaseResponse<>(REQUEST_ERROR);
        }

        try{
            UserDTO newUser = authService.addUser(userDTO);
            return new BaseResponse<>(newUser);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    //중복아이디 검사
    @PostMapping("/checkDuplicatedId")
    public String checkDuplicatedId(@RequestParam("userId") String userId){
        return authService.checkDuplicatedId(userId);
    }



}
