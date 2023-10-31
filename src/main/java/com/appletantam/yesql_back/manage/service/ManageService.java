package com.appletantam.yesql_back.manage.service;

import com.appletantam.yesql_back.manage.dto.UserDatabaseDTO;

public interface ManageService {

    public UserDatabaseDTO renameDatabase(String name, long dbCd);
}
