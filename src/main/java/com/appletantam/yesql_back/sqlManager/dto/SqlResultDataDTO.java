package com.appletantam.yesql_back.sqlManager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class SqlResultDataDTO {
    int step;
    boolean haveReturn;
    ArrayList columns;
    ArrayList rows;
}
