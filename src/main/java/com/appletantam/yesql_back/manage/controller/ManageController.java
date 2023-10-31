package com.appletantam.yesql_back.manage.controller;

import com.appletantam.yesql_back.config.response.BaseResponse;
import com.appletantam.yesql_back.config.response.BaseResponseStatus;
import com.appletantam.yesql_back.manage.dto.UserDatabaseDTO;
import com.appletantam.yesql_back.manage.service.ManageService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    private ManageService manageService;

    @GetMapping("/test")
    public String test(){
        return "manage test success";
    }

    //유저 데이터베이스 이름 변경하기
    @PostMapping("/renameDB")
    public BaseResponse<UserDatabaseDTO> renameDB(@RequestParam("rename") String rename, @RequestParam("dbCd") long dbCd){
        UserDatabaseDTO userDatabaseDTO = manageService.renameDatabase(rename, dbCd);
        return new BaseResponse<>(BaseResponseStatus.CHANGED_DATABASE, userDatabaseDTO);
    }

    @PostMapping("/showDB")
    public BaseResponse<UserDatabaseDTO> showDB(@RequestParam("dbCd") long dbCd){
        return null;
    }

    @PostMapping("/import")
    public void dataImport(){

    }

    /* SQL 쿼리문 입력에 대한 프론트, antlr, 백 순서를 어떻게 해야할까
    * 프론트 <-> 백 <-> antlr로 해야할지..
    * 프론트 -> antlr -> 백 -> 프론트로 해야할지
    * */

    @PostMapping("/submitQuery")
    public void submitQuery(@RequestParam("query") String query){
        // 쿼리문을 antlr에 전송해 map타입으로 들고 오기
    }

}
