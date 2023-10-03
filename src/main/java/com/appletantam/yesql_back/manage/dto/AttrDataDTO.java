package com.appletantam.yesql_back.manage.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class AttrDataDTO {

    private long attrCd;
    private long dataCd;
    private String dataInfo;
}
