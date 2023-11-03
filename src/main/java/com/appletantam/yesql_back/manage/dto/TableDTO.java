package com.appletantam.yesql_back.manage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonPropertyOrder({"dbCd", "tableCd", "tableName","Info"})
public class TableDTO<T> {
    private long dbCd;
    private long tableCd;
    private String tableName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DataDTO> info;

}
