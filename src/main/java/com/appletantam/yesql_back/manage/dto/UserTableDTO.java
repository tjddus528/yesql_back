package com.appletantam.yesql_back.manage.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserTableDTO {
    private String tableName;
    private long tableCd;
    private long dbCd;
}
