package com.appletantam.yesql_back.manage.service;

import com.appletantam.yesql_back.manage.dao.ManageDAO;
import com.appletantam.yesql_back.manage.dto.SchemaDTO;
import com.appletantam.yesql_back.sqlManager.SqlConnector;
import org.jetbrains.annotations.ApiStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Service
public class ManageServiceImpl implements ManageService{

    @Autowired
    private ManageDAO manageDAO;

    SqlConnector sqlConnector;
    @Value("${mysql.url}")
    public String mysqlUrl;
    @Value("${mysql.user}")
    public String user;
    @Value("${mysql.password}")
    public String password;

    @Override
    public void mysqlConnectInService(String dbName) throws SQLException {
            //ec2_mysql 연결
            sqlConnector = new SqlConnector(mysqlUrl, user, password);
            sqlConnector.useDB(dbName);

    }

    @Override
    public SchemaDTO simpleTableData(String dbName, String tableName) throws SQLException {

        SchemaDTO schemaDTO;
        ArrayList<String> columns;
        ArrayList<Map<String, String>> rows;

        mysqlConnectInService(dbName);

        // 실행
        // 유저 데베 안에 모든 <테이블>을 보여주는 쿼리문 실행
        Statement stmt = sqlConnector.stmt;
        // 테이블 리스트 순회 -> 테이블명에 해당하는 <컬럼> 조회 후 <컬럼 리스트> 생성

        // 각 테이블의 <컬럼>을 보여주는 쿼리문 실행
        String getColumnQuery = "SHOW COLUMNS FROM " + tableName + ";";
        ResultSet rs = stmt.executeQuery(getColumnQuery);

        columns = new ArrayList<>();
        // 각 테이블의 <컬럼 리스트> 생성
        while (rs.next()) {
            columns.add(rs.getString("Field"));
        }

        //해당 테이블의 모든 <데이터> 조회
        String getData = "SELECT * FROM " + tableName + ";";
        rs = stmt.executeQuery(getData);
        ResultSetMetaData rsmd = rs.getMetaData();

        //각 테이블의 <데이터 리스트> 생성
        rows = new ArrayList<>();
        while(rs.next()){
            Map<String, String> oneRow = new LinkedHashMap<>(); // 순서대로 입력하고 싶은 경우 HashMap<>이 아닌 LinkedHashMap<>을 사용해야한다

            for (int j = 1; j <= rsmd.getColumnCount() ; j++) {
                String colName = rsmd.getColumnName(j); // 컬럼명 순서대로 가져오기. rsmd는 순서대로 들고온다
                String colData = rs.getString(colName);
                oneRow.put(colName, colData);
            }

            rows.add(oneRow);
        }

        schemaDTO = new SchemaDTO(tableName, columns, rows);
        sqlConnector.closeConnection();


        return schemaDTO;
    }

    @Override
    public ArrayList<String> getColumns(JSONArray jsonArray) {

        ArrayList<String> columns = new ArrayList<>();

        JSONObject jsonObj = (JSONObject)jsonArray.get(0);
        for (Object object : jsonObj.entrySet()) { // 한 줄 읽기
            Map.Entry<String, String> entry = (Map.Entry<String, String>) object;
            columns.add(entry.getKey());
        }

        return columns;
    }

    @Override
    public String getColQuery(ArrayList<String> columns, String query) {

        for ( int i = 0 ; i < columns.size() ; i++){
            if ( i == (columns.size() - 1) ){ // 마지막 컬럼인 경우
                query += "`" + columns.get(i) + "`) VALUES (";
            }
            else {
                query += "`" + columns.get(i) + "`, ";
            }
        }

        return query;
    }

    @Override
    public void insertData(String query, JSONObject jsonObj, String dbName) throws SQLException {

        // 문장 만들기
        int cnt = 0;
        for ( Object object : jsonObj.entrySet()){
            Map.Entry<String, String> entry = (Map.Entry<String, String>) object;

            if ( cnt == ( jsonObj.size() - 1) ){ // 마지막 컬럼인 경우
                query += "'" + entry.getValue() + "');";
            }
            else {
                query += "'" + entry.getValue() + "', ";
                cnt++;
            }
        }

        // ec2_mysql 연결
        mysqlConnectInService(dbName);

        // 쿼리문(String query) 실행
        Statement stmt = sqlConnector.stmt;
        stmt.executeUpdate(query);


    }

    @Override
    public void truncateTable(String dbName, String tableName) throws SQLException {
        // ec2_mysql 연결
        mysqlConnectInService(dbName);

        // truncate table
        Statement stmt = sqlConnector.stmt;
        String query = "TRUNCATE TABLE " + tableName + ";";
        stmt.executeUpdate(query);

    }


}
