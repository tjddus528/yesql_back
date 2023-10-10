package com.appletantam.config.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000 :요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    LOGIN_INCORRECT(false, 2003,"아이디 혹은 비밀번호를 다시 확인해주세요"),

    /**
     * 2000 : Request오류
     */

    /**
     * 1. 입력값에 오류가 있을 때 (2000~)
     */
    // 계정관련(2000~)
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_ID(false, 2001, "아이디를 입력해주세요."),
    EMPTY_PASSWORD(false, 2002, "비밀번호를 입력해주세요."),

    /**
     * 2. 이미 존재하는 리소스와 중복된 값일 때(2100~)
     * EXIST_목적어
     */
    EXISTS_ID(false, 2100,"중복된 아이디입니다."),
    EXISTS_PASSWORD(false, 2101,"기존 비밀번호와 동일한 비밀번호입니다."),


    /**
     * 3. 접근이 유효하지 않거나 : INVALID_ACCESS_목적어
     * 존재하지 않는 리소스에 접근시도할 때 : NOT_EXIST_목적어
     * (2200~)
     */
    NOT_EXIST_USER(false, 2200, "유저가 존재하지 않습니다."),
    NOT_EXIST_ID(false, 2201,"해당 아이디는 존재하지 않습니다."),
    INVALID_ACCESS_PASSWORD(false, 2202,"잘못된 비밀번호입니다."),



    /**
     * 3000 : Response오류
     */
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
    FAILED_TO_LOGIN(false, 3001,"로그인이 실패하였습니다."),
    PASSWORD_ENCRYPTION_ERROR(false, 3010, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 3011, "비밀번호 복호화에 실패하였습니다."),



    /**
     * 4000 : Database, Server오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다.");



    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message){
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}