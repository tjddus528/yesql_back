package com.appletantam.yesql_back.sqlManager.dto;

import com.appletantam.yesql_back.manage.dto.SchemaDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class StepComponent {
    int step;
    String keyword;
    String sqlStatement;
    ArrayList<SchemaDTO> tableData;
    ArrayList<ColumnInfo> selectedColumns;
    ArrayList<ColumnInfo> conditionColumns;
    ArrayList<String> conditions;

    Boolean joinExists;
    ArrayList<ColumnInfo> joinedColumns;
    ArrayList<String> on;

    String queryA;
    String queryB;

    public StepComponent() {
        step = 0;
        keyword = null;
        sqlStatement = null;
        tableData = null;
        selectedColumns = null;
        conditionColumns = null;
        conditions = null;

        joinExists = null;
        joinedColumns = null;
        on = null;

        queryA = null;
        queryB = null;
    }
    public StepComponent(int step, String keyword, String sqlStatement) {
        this.step = step;
        this.keyword = keyword;
        this.sqlStatement = sqlStatement;
        this.tableData = null;
        this.selectedColumns = null;
        this.conditionColumns = null;
        this.conditions = null;

        this.joinExists = null;
        this.joinedColumns = null;
        this.on = null;

        this.queryA = null;
        this.queryB = null;
    }
}