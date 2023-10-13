package com.appletantam.yesql_back.src.manage.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserDatabaseDTO {
    private long dbCd;
    private long userCd;
    private String dbName;
}
