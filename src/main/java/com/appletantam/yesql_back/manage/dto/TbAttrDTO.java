package com.appletantam.yesql_back.manage.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class TbAttrDTO {
    private long attrCd;
    private long tableCd;
    private String attrType;
    private String attrName;
}
