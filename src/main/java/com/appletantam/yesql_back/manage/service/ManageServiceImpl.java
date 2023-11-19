package com.appletantam.yesql_back.manage.service;

import com.appletantam.yesql_back.manage.dao.ManageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class ManageServiceImpl implements ManageService{

    @Autowired
    private ManageDAO manageDAO;
}
