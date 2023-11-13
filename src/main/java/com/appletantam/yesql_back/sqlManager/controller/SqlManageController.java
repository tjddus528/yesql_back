package com.appletantam.yesql_back.sqlManager.controller;


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

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
    @Value("${antlr.url}")
    String url;

    SqlConnector sqlConnector;
    @Value("${mysql.url}")
    public String mysqlUrl;
    @Value("${mysql.user}")
    public String user;
    @Value("${mysql.password}")
    public String password;

    // 1. 과정별 시각화 정보 API (오른쪽 창)
    @GetMapping("/runByStep")
    public BaseResponse<ArrayList> sqlRunByStep(@RequestParam("sql") String sql) throws IOException {

        // 1) antlr를 통해 각 과정별 sql문의 정보를 받아온다.
        ArrayList arrayList = antlrService.getData(url+sql);

        // 2) 해당 정보를 프론트에서 시각화할 수 있게 전달
        return new BaseResponse<>(arrayList);
    }

    // 2. 과정별 결과 데이터 API (왼쪽아래 창)
    @GetMapping("/resultData")
    public BaseResponse<SqlResultDataDTO> sqlResultData(@RequestParam("sql") String sql, @RequestParam("dbName") String dbName) {

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

        // 3) 각 과정별 sql문을 실행시킨다.
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
