package com.appletantam.yesql_back.manage.controller;

import com.appletantam.yesql_back.manage.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    private ManageService manageService;

    //유저 데이터베이스 이름 변경하기
    @PostMapping("/reNameDB")
    public void reNameDB(@RequestParam("rename") String name, @RequestParam("dbCd") String dbCd){

    }

    @PostMapping("/import")
    public void dataImport(){

    }

}
