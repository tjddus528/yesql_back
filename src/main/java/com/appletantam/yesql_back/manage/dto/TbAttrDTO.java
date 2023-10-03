package com.appletantam.yesql_back.manage.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class TbAttrDTO {
    private String attrName;
    private long attrCd;
    private String attrType;
    private long tableCd;
}
