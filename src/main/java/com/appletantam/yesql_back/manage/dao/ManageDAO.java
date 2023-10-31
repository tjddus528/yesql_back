package com.appletantam.yesql_back.manage.dao;

import com.appletantam.yesql_back.manage.dto.UserDatabaseDTO;

import java.util.Map;

public interface ManageDAO {

    public UserDatabaseDTO renameDB(Map<String, Object> map);
}
