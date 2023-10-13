package com.appletantam.yesql_back.src.auth.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserDTO {
    private String userId;
    private String userPassword;
    private long userCd;
}
