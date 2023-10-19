package com.appletantam.yesql_back.config.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponse<T> {

    private final Boolean isSuccess;
    private final String message;
    private final int code;
    @JsonInclude(JsonInclude.Include.NON_NULL) // NULL값이 아닌 속성만 result에 포함
    private T result;

    // 요청에 성공한 경우
    public BaseResponse(T result){
        this.isSuccess = BaseResponseStatus.SUCCESS.isSuccess();
        this.code = BaseResponseStatus.SUCCESS.getCode();
        this.message = BaseResponseStatus.SUCCESS.getMessage();
        this.result = result;
    }

    // 성공시, 메소드에 따른 메세지 전송
    public BaseResponse(BaseResponseStatus status, T result){
        this.isSuccess = BaseResponseStatus.SUCCESS.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
        this.result = result;
    }

    // 요청에 실패한 경우
    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
    }
}
