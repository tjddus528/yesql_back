package com.appletantam.yesql_back.manage.service;

import com.appletantam.yesql_back.manage.dao.ManageDAO;
import com.appletantam.yesql_back.manage.dto.UserDatabaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ManageServiceImpl implements ManageService{

    @Autowired
    private ManageDAO manageDAO;

    @Override
    public UserDatabaseDTO renameDatabase(String name, long dbCd) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("dbCd", dbCd);
        return manageDAO.renameDB(map);
    }
}
