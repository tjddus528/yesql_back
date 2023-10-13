package com.appletantam.yesql_back.src.manage.service;

import com.appletantam.yesql_back.src.manage.dao.ManageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManageServiceImpl implements ManageService{

    @Autowired
    private ManageDAO manageDAO;
}
