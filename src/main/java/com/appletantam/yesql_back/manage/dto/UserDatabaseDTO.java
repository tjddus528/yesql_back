package com.appletantam.yesql_back.manage.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserDatabaseDTO {
    private String dbName;
    private long dbCd;
    private long userCd;
}
