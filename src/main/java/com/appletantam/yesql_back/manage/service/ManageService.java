package com.appletantam.yesql_back.manage.service;

import com.appletantam.yesql_back.manage.dto.DataDTO;
import com.appletantam.yesql_back.manage.dto.UserDatabaseDTO;
import java.util.List;

public interface ManageService {

    public UserDatabaseDTO renameDatabase(String name, long dbCd);

    public List<DataDTO> getDataList(long dbCd);
}
