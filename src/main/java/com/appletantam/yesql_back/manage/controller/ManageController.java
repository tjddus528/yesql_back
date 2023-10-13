package com.appletantam.yesql_back.manage.controller;

import com.appletantam.yesql_back.manage.dao.ManageDAO;
import com.appletantam.yesql_back.manage.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    private ManageService manageService;
}
