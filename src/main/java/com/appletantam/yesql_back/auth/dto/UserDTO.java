package com.appletantam.yesql_back.auth.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserDTO {
    private long userCd;
    private String userId;
    private String userPassword;
    private String dbName;
}
