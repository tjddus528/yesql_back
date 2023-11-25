package com.appletantam.yesql_back.config.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000 :요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    REGISTER_SUCCESS(true, 1001, "register success"),
    LOGIN_SUCCESS(true, 1002, "login success"),

    /**
     * 1. 입력값에 오류가 있을 때 (2000~)
     */
    // 계정관련(2000~)
    ERROR(false, 2000, "요청값을 확인해주세요"),
    LOGIN_INCORRECT(false, 2001,"아이디 혹은 비밀번호를 다시 확인해주세요."),
    NO_DATABASE(true, 2002, "유저의 데이터베이스가 존재하지 않습니다. welcome으로 이동해 데이터베이스를 생성해주세요."),
    REGISTER_FAILED(false,2003, "회원가입 실패! 중복된 아이디입니다."),
    ANTLR_API_ERROR(false, 2004, "ANTLR API 요청이 실패했습니다."),

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
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    EXECUTE_SQL_ERROR(false, 4002, "SQL 실행이 실패하였습니다."),
    FILE_ERROR(false,4003,"파일을 부르는 중 연결 실패했습니다"),
    IMPORT_FAIL(false, 4004, "중복된 값을 삽입했거나, 존재하지 않는 테이블입니다."),

    TABLE_ERROR(false,4005,"테이블 결과를 받아오는데 실패했습니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message){
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}