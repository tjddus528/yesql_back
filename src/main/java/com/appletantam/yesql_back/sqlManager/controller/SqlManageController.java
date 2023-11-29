package com.appletantam.yesql_back.sqlManager.controller;


import com.appletantam.yesql_back.manage.dto.SchemaDTO;
import com.appletantam.yesql_back.manage.service.ManageServiceImpl;
import com.appletantam.yesql_back.sqlManager.dto.ColumnInfo;
import com.appletantam.yesql_back.sqlManager.dto.StepComponent;
import com.appletantam.yesql_back.sqlManager.dto.TableInfo;
import com.appletantam.yesql_back.sqlManager.service.AntlrService;
import com.appletantam.yesql_back.config.response.BaseResponse;
import com.appletantam.yesql_back.sqlManager.SqlConnector;
import com.appletantam.yesql_back.sqlManager.dto.SqlResultDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Table;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.appletantam.yesql_back.config.response.BaseResponseStatus.*;

@RestController
@RequestMapping("/sql")
public class SqlManageController {

    @Autowired
    AntlrService antlrService;

    @Autowired
    ManageServiceImpl manageService;
    @Value("${antlr.url}")
    String url;

    SqlConnector sqlConnector;
    @Value("${mysql.url}")
    public String mysqlUrl;
    @Value("${mysql.user}")
    public String user;
    @Value("${mysql.password}")
    public String password;


    public SchemaDTO sqlResultTableData(String dbName, String sql) throws Exception {

        ArrayList sqlInfo;
        SqlResultDataDTO sqlResultDataDTO;
        int step = 0;
        boolean haveReturn = false;
        ArrayList<String> columns = new ArrayList();
        ArrayList<Map<String, String>> rows = new ArrayList();

        // 1) 해당 유저의 DB 정보로 SqlConnector 생성 (USE dbName;)
        sqlConnector = new SqlConnector(mysqlUrl, user, password);
        sqlConnector.useDB(dbName);

        // 2) sql문을 실행시킨다.
        // 반환값이 있는 경우
        if (sqlConnector.executeSql(sql)) {
            ResultSet resultSet = sqlConnector.rs;
            ResultSetMetaData rsmd = resultSet.getMetaData();

            int cnt = rsmd.getColumnCount();
            for (int j = 1; j <= cnt; j++) {
                String col = rsmd.getColumnLabel(j);
                columns.add(col);
            }

            while (resultSet.next()) {
                Map<String, String> oneRow = new LinkedHashMap<>();
                for (int j = 1; j <= cnt; j++) {
                    String col_data = resultSet.getString(columns.get(j - 1));
                    oneRow.put(columns.get(j - 1), col_data);
                }
                rows.add(oneRow);
            }
        }
        SchemaDTO schemaDTO = new SchemaDTO(sql, columns, rows);

        return schemaDTO;
    }

    // 1. 과정별 시각화 정보 API (오른쪽 창)
    @GetMapping("/runByStep")
    public BaseResponse<ArrayList<StepComponent>> sqlRunByStep(@RequestParam("sql") String sql, @RequestParam("dbName") String dbName) throws Exception {

        ArrayList antlrComponent;
        ArrayList<StepComponent> stepComponentsList = new ArrayList<>();


        // 1) antlr를 통해 각 과정별 sql문의 정보를 받아온다.
        try {
            antlrComponent = antlrService.getData(url+sql);
        } catch (Exception ex) {
            return new BaseResponse<>(ANTLR_API_ERROR);
        }

        // 2) 해당 유저의 DB 정보로 SqlConnector 생성 (USE dbName;)
        try {
            sqlConnector = new SqlConnector(mysqlUrl, user, password);
            sqlConnector.useDB(dbName);
        } catch (Exception ex) {
            return new BaseResponse<>(DATABASE_ERROR);
        }


        // 3) select일 경우 table정보를
        // 4) union일 경우 앞 뒤 쿼리 정보를 받아온다.
        for (int i = 0; i < antlrComponent.size(); i++) {
            LinkedHashMap sqlInfoByStep = (LinkedHashMap) antlrComponent.get(i);
            StepComponent stepComponent = new StepComponent();

            int step  = (int) sqlInfoByStep.get("step");
            String keyword = (String) sqlInfoByStep.get("keyword");
            String sqlStatement  = (String) sqlInfoByStep.get("sqlStatement");
            ArrayList<TableInfo> tables = (ArrayList<TableInfo>) sqlInfoByStep.get("tables");
            ArrayList<ColumnInfo> selectedColumns = (ArrayList<ColumnInfo>) sqlInfoByStep.get("selectedColumns");
            ArrayList<ColumnInfo> conditionColumns = (ArrayList<ColumnInfo>) sqlInfoByStep.get("conditionColumns");
            ArrayList<String> conditions = (ArrayList<String>) sqlInfoByStep.get("conditions");
            Boolean joinExists = (Boolean) sqlInfoByStep.get("joinExists");
            ArrayList<ColumnInfo> joinedColumns = (ArrayList<ColumnInfo>) sqlInfoByStep.get("joinedColumns");
            ArrayList<String> on = (ArrayList<String>) sqlInfoByStep.get("on");
            String queryA = (String) sqlInfoByStep.get("queryA");
            String queryB = (String) sqlInfoByStep.get("queryB");

            stepComponent.setStep(step);
            stepComponent.setKeyword(keyword);
            stepComponent.setSqlStatement(sqlStatement);
            stepComponent.setTables(tables);
            stepComponent.setConditions(conditions);
            stepComponent.setJoinExists(joinExists);
            stepComponent.setOn(on);
            stepComponent.setQueryA(queryA);
            stepComponent.setQueryB(queryB);

            stepComponent.setSelectedColumns(selectedColumns);
            // 테이블명, Alias 정리하기
            if(selectedColumns != null){

                ArrayList columns = (ArrayList) sqlInfoByStep.get("selectedColumns");
                ArrayList<ColumnInfo> newSelectedColumns = new ArrayList<>();


                for(int j=0; j<columns.size(); j++) {
                    boolean tableNameIsAlias = false;

                    LinkedHashMap columnsInfo = (LinkedHashMap) columns.get(j);
                    ColumnInfo columnInfo = new ColumnInfo();

                    // 컬럼의 테이블 이름과 alias 받아오기
                    String tableNameOfColumn = (String) columnsInfo.get("tableName");
                    String columnLabel = (String) columnsInfo.get("columnLabel");
                    String aliasOfColumn = (String) columnsInfo.get("alias");

                    if(tableNameOfColumn == null) {
                        columnInfo.setTableName(tableNameOfColumn);
                        columnInfo.setColumnLabel(columnLabel);
                        columnInfo.setAlias(aliasOfColumn);
                        newSelectedColumns.add(columnInfo);
                        continue;
                    }

                    String tableName = null;
                    String alias = null;
                            // 테이블 이름이 alias인 경우찾기
                    ArrayList tableInfo = (ArrayList) sqlInfoByStep.get("tables");
                    for(int k=0; k<tableInfo.size(); k++) {
                        LinkedHashMap tableNameInfo = (LinkedHashMap) tableInfo.get(k);

                        // 테이블 이름과 alias 받아오기
                        tableName = (String) tableNameInfo.get("tableName");
                        alias = (String) tableNameInfo.get("alias");


                        if(alias == null) alias = "";

                        if (tableNameOfColumn.equals(alias)) {
                            tableNameIsAlias = true;
                            break;
                        }
                        tableNameIsAlias = false;

                    }

                    if(tableNameIsAlias) {
                        columnInfo.setTableName(tableName);
                        columnInfo.setColumnLabel(columnLabel);
                        columnInfo.setAlias(alias);
                    }
                    else {
                        columnInfo.setTableName(tableNameOfColumn);
                        columnInfo.setColumnLabel(columnLabel);
                        columnInfo.setAlias(aliasOfColumn);
                    }
                    newSelectedColumns.add(columnInfo);


                }
                stepComponent.setSelectedColumns(newSelectedColumns);
            }


            stepComponent.setConditionColumns(conditionColumns);
            if(conditionColumns != null){

                ArrayList columns = (ArrayList) sqlInfoByStep.get("conditionColumns");
                ArrayList<ColumnInfo> newConditionColumns = new ArrayList<>();


                for(int j=0; j<columns.size(); j++) {
                    boolean tableNameIsAlias = false;

                    LinkedHashMap columnsInfo = (LinkedHashMap) columns.get(j);
                    ColumnInfo columnInfo = new ColumnInfo();

                    // 컬럼의 테이블 이름과 alias 받아오기
                    String tableNameOfColumn = (String) columnsInfo.get("tableName");
                    String columnLabel = (String) columnsInfo.get("columnLabel");
                    String aliasOfColumn = (String) columnsInfo.get("alias");
                    if(tableNameOfColumn == null) {
                        columnInfo.setTableName(tableNameOfColumn);
                        columnInfo.setColumnLabel(columnLabel);
                        columnInfo.setAlias(aliasOfColumn);
                        newConditionColumns.add(columnInfo);
                        continue;
                    }

                    String tableName = null;
                    String alias = null;
                    // 테이블 이름이 alias인 경우찾기
                    ArrayList tableInfo = (ArrayList) sqlInfoByStep.get("tables");
                    for(int k=0; k<tableInfo.size(); k++) {
                        LinkedHashMap tableNameInfo = (LinkedHashMap) tableInfo.get(k);

                        // 테이블 이름과 alias 받아오기
                        tableName = (String) tableNameInfo.get("tableName");
                        alias = (String) tableNameInfo.get("alias");

                        if(alias == null) alias = "";

                        if (tableNameOfColumn.equals(alias)) {
                            tableNameIsAlias = true;
                            break;
                        }
                        tableNameIsAlias = false;
                    }

                    if(tableNameIsAlias) {
                        columnInfo.setTableName(tableName);
                        columnInfo.setColumnLabel(columnLabel);
                        columnInfo.setAlias(alias);
                    }
                    else {
                        columnInfo.setTableName(tableNameOfColumn);
                        columnInfo.setColumnLabel(columnLabel);
                        columnInfo.setAlias(aliasOfColumn);
                    }
                    newConditionColumns.add(columnInfo);


                }
                stepComponent.setConditionColumns(newConditionColumns);
            }

            stepComponent.setJoinedColumns(joinedColumns);
            if(joinedColumns != null){

                ArrayList columns = (ArrayList) sqlInfoByStep.get("joinedColumns");
                ArrayList<ColumnInfo> newJoinedColumns = new ArrayList<>();


                for(int j=0; j<columns.size(); j++) {
                    boolean tableNameIsAlias = false;

                    LinkedHashMap columnsInfo = (LinkedHashMap) columns.get(j);
                    ColumnInfo columnInfo = new ColumnInfo();

                    // 컬럼의 테이블 이름과 alias 받아오기
                    String tableNameOfColumn = (String) columnsInfo.get("tableName");
                    String columnLabel = (String) columnsInfo.get("columnLabel");
                    String aliasOfColumn = (String) columnsInfo.get("alias");

                    if(tableNameOfColumn == null) {
                        columnInfo.setTableName(tableNameOfColumn);
                        columnInfo.setColumnLabel(columnLabel);
                        columnInfo.setAlias(aliasOfColumn);
                        newJoinedColumns.add(columnInfo);
                        continue;
                    }

                    String tableName = null;
                    String alias = null;
                    // 테이블 이름이 alias인 경우찾기
                    ArrayList tableInfo = (ArrayList) sqlInfoByStep.get("tables");
                    for(int k=0; k<tableInfo.size(); k++) {
                        LinkedHashMap tableNameInfo = (LinkedHashMap) tableInfo.get(k);

                        // 테이블 이름과 alias 받아오기
                        tableName = (String) tableNameInfo.get("tableName");
                        alias = (String) tableNameInfo.get("alias");

                        if(alias == null) alias = "";

                        if (tableNameOfColumn.equals(alias)) {
                            tableNameIsAlias = true;
                            break;
                        }
                        tableNameIsAlias = false;

                    }

                    if(tableNameIsAlias) {
                        columnInfo.setTableName(tableName);
                        columnInfo.setColumnLabel(columnLabel);
                        columnInfo.setAlias(alias);
                    }
                    else {
                        columnInfo.setTableName(tableNameOfColumn);
                        columnInfo.setColumnLabel(columnLabel);
                        columnInfo.setAlias(aliasOfColumn);
                    }
                    newJoinedColumns.add(columnInfo);


                }
                stepComponent.setJoinedColumns(newJoinedColumns);
            }


            // 테이블 데이터 가져오기
            ArrayList<SchemaDTO> tableDataList = new ArrayList<>();
            // SELECT일 경우 테이블명에 따른 SQL결과데이터 리스트에 저장
            if(keyword.equals("SELECT")) {
                ArrayList tableInfo = (ArrayList) sqlInfoByStep.get("tables");

                for(int j=0; j<tableInfo.size(); j++) {
                    LinkedHashMap tableNameInfo = (LinkedHashMap) tableInfo.get(j);

                    // 테이블 이름 받아오기
                    String tableName = (String) tableNameInfo.get("tableName");


                    // SQL문 실행 -> 데이터 받아서 스키마리스트에 저장
                    try {
                        if(tableName.startsWith("SELECT")) {
                            tableDataList.add(sqlResultTableData(dbName, tableName));
                        }
                        else
                            tableDataList.add(manageService.simpleTableData(dbName, tableName));
                    } catch (Exception exception) {
                        return new BaseResponse<>(TABLE_ERROR);
                    }
//                    System.out.println(tableName);
                }
            }
            // UNION일 경우 쿼리A와 쿼리B의 SQL결과데이터 리스트에 저장
            else if (keyword.equals("UNION")) {
                String A = (String) sqlInfoByStep.get("queryA");
                String B = (String) sqlInfoByStep.get("queryB");

                // SQL문 실행 -> 데이터 받아서 스키마리스트에 저장
                tableDataList.add(sqlResultTableData(dbName, A));
                tableDataList.add(sqlResultTableData(dbName, B));

//                System.out.println(A);
//                System.out.println(B);
            }


            // 스키마리스트를 해당 과정 정보에 저장(step별로)
            stepComponent.setTableData(tableDataList);
            stepComponentsList.add(stepComponent);

        }



        // 2) 해당 정보를 프론트에서 시각화할 수 있게 전달
        return new BaseResponse<>(stepComponentsList);
    }

    // 2. 결과 데이터 API
    @GetMapping("/resultData")
    public BaseResponse<SqlResultDataDTO> sqlResultData(@RequestParam("sql") String sql, @RequestParam("dbName") String dbName) {

        ArrayList sqlInfo;
        SqlResultDataDTO sqlResultDataDTO;
        int step = 0;
        boolean haveReturn = false;
        ArrayList<String> columns = new ArrayList();
        ArrayList<Map<String, String>> rows = new ArrayList();


        // 1) antlr를 통해 각 과정별 sql문의 정보를 받아온다.
//        try {
//            sqlInfo = antlrService.getData(url + sql);
//        } catch (Exception ex) {
//            return new BaseResponse<>(ANTLR_API_ERROR);
//        }


        // 1) 해당 유저의 DB 정보로 SqlConnector 생성 (USE dbName;)
        try {
            sqlConnector = new SqlConnector(mysqlUrl, user, password);
            sqlConnector.useDB(dbName);
        } catch (Exception ex) {
            return new BaseResponse<>(DATABASE_ERROR);
        }


        // 2) sql문을 실행시킨다.
        try {

            // 반환값이 있는 경우
            if (sqlConnector.executeSql(sql)) {
                haveReturn = true;
                ResultSet resultSet = sqlConnector.rs;
                ResultSetMetaData rsmd = resultSet.getMetaData();

                int cnt = rsmd.getColumnCount();
                for (int j = 1; j <= cnt; j++) {
                    String col = rsmd.getColumnLabel(j);
                    columns.add(col);
                }

                while (resultSet.next()) {
                    Map<String, String> oneRow = new HashMap<>();
                    for (int j = 1; j <= cnt; j++) {
                        String col_data = resultSet.getString(columns.get(j - 1));
                        oneRow.put(columns.get(j - 1), col_data);
                    }
                    rows.add(oneRow);
                }
            } else {
                haveReturn = false;
            }

        } catch (Exception ex) {
            return new BaseResponse<>(EXECUTE_SQL_ERROR);
        }

        sqlResultDataDTO = new SqlResultDataDTO(step, haveReturn, columns, rows);

        return new BaseResponse<>(sqlResultDataDTO);
    }

    // 3. 과정별 결과 데이터 API (왼쪽아래 창)
    @GetMapping("/resultDataByStep")
    public BaseResponse<SqlResultDataDTO> sqlResultDataByStep(@RequestParam("sql") String sql, @RequestParam("dbName") String dbName) {

        ArrayList sqlInfo;
        SqlResultDataDTO sqlResultDataDTO;
        int step = 1;
        boolean haveReturn = false;
        ArrayList<String> columns = new ArrayList();
        ArrayList<Map<String, String>> rows = new ArrayList();


        // 1) antlr를 통해 각 과정별 sql문의 정보를 받아온다.
        try {
            sqlInfo = antlrService.getData(url + sql);
        } catch (Exception ex) {
            return new BaseResponse<>(ANTLR_API_ERROR);
        }


        // 2) 해당 유저의 DB 정보로 SqlConnector 생성 (USE dbName;)
        try {
            sqlConnector = new SqlConnector(mysqlUrl, user, password);
            sqlConnector.useDB(dbName);
        } catch (Exception ex) {
            return new BaseResponse<>(DATABASE_ERROR);
        }

        // 3) 각  과정별 sql문을 실행시킨다.
        try {
            for (int i = 0; i < sqlInfo.size(); i++) {
                LinkedHashMap sqlInfoByStep = (LinkedHashMap) sqlInfo.get(i);
                step = (int) sqlInfoByStep.get("step");
                String sqlByStep = (String) sqlInfoByStep.get("sql");
                String keyword = (String) sqlInfoByStep.get("keyword");

                // 반환값이 있는 경우
                if (sqlConnector.executeSql(sqlByStep)) {
                    haveReturn = true;
                    ResultSet resultSet = sqlConnector.rs;
                    ResultSetMetaData rsmd = resultSet.getMetaData();

                    int cnt = rsmd.getColumnCount();
                    for (int j = 1; j <= cnt; j++) {
                        String col = rsmd.getColumnLabel(j);
                        columns.add(col);
                    }

                    while (resultSet.next()) {
                        Map<String, String> oneRow = new HashMap<>();
                        for (int j = 1; j <= cnt; j++) {
                            String col_data = resultSet.getString(columns.get(j - 1));
                            oneRow.put(columns.get(j - 1), col_data);
                        }
                        rows.add(oneRow);
                    }
                } else {
                    haveReturn = false;
                }

            }
        } catch (Exception ex) {
            return new BaseResponse<>(EXECUTE_SQL_ERROR);
        }

        sqlResultDataDTO = new SqlResultDataDTO(step, haveReturn, columns, rows);

        return new BaseResponse<>(sqlResultDataDTO);
    }

}
