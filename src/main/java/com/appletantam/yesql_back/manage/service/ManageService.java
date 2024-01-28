package com.appletantam.yesql_back.manage.service;

import com.appletantam.yesql_back.manage.dto.SchemaDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public interface ManageService {
    public void mysqlConnectInService(String dbName) throws Exception;
    public SchemaDTO simpleTableData(String dbName, String tableName) throws Exception;
    ArrayList<String> getColumns(JSONArray jsonArray);
    String getColQuery(ArrayList<String> columns, String query);

    void insertData(String query, JSONObject jsonObj, String dbName) throws Exception;

    void truncateTable(String dbName, String tableName) throws Exception;
}
