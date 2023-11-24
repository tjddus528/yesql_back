package com.appletantam.yesql_back.manage.service;

import com.appletantam.yesql_back.manage.dto.SchemaDTO;

import java.util.List;

public interface ManageService {
    public void mysqlConnectInService(String dbName) throws Exception;
    public SchemaDTO simpleTableData(String dbName, String tableName) throws Exception;
}
