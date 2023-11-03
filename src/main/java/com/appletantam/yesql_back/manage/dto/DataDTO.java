package com.appletantam.yesql_back.manage.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class DataDTO {
    private long dbCd;
    private long tableCd;
    private long attrCd;
    private long lowCd;
    private long dataCd;
    private String tableName;
    private String attrName;
    private String attrType;
    private String dataInfo;
}
