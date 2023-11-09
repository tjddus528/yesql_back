package com.appletantam.yesql_back.manage.controller;

import com.appletantam.yesql_back.config.response.BaseResponse;
import com.appletantam.yesql_back.config.response.BaseResponseStatus;
import com.appletantam.yesql_back.manage.dto.DataDTO;
import com.appletantam.yesql_back.manage.dto.TableDTO;
import com.appletantam.yesql_back.manage.dto.UserDatabaseDTO;
import com.appletantam.yesql_back.manage.service.ManageService;
import com.appletantam.yesql_back.config.antlr.AntlrService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    private ManageService manageService;

    @Autowired
    AntlrService antlrService;

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

    @GetMapping("/getDataList")
    public BaseResponse<List<TableDTO<List<DataDTO>>>> showDB(@RequestParam("dbCd") long dbCd){
        //main_scroll 페이지에서 전체 스키마 및 데이터를 보여줄때 사용하는 api

        // 유저의 데이터 베이스에 존재하는 모든 정보 가져오기
        List<DataDTO> dataDTOList = manageService.getDataList(dbCd);

        // Table단위로 리스트 만들어주기
        List<TableDTO<List<DataDTO>>> tableDTOList = new ArrayList<>();
        List<Long> listupTableCd = new ArrayList<>();

        //모든 정보 훑기
        for ( DataDTO dataDTO : dataDTOList ) {
            if ( !listupTableCd.contains(dataDTO.getTableCd()) ){
                listupTableCd.add(dataDTO.getTableCd());

                TableDTO<List<DataDTO>> tableDTO = new TableDTO<>();
                List<DataDTO> temp = new ArrayList<>();
                long tableCd = dataDTO.getTableCd();

                //테이블 코드에 해당하는 DataDTO를 찾으면 temp list에 넣어주기
                for ( DataDTO i : dataDTOList ){
                    if ( i.getTableCd() == tableCd ){
                        temp.add(i);
                    }
                }

                //테이블 DTO 만들어주기
                tableDTO.setDbCd(dataDTO.getDbCd());
                tableDTO.setTableCd(dataDTO.getTableCd());
                tableDTO.setTableName(dataDTO.getTableName());
                tableDTO.setInfo(temp);

                //리스트에 삽입
                tableDTOList.add(tableDTO);
            }

        }
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, tableDTOList);
    }

    @PostMapping("/import")
    public void dataImport(){
        // 테이블 단위의 데이터를 받는다. 이때 형식은 <"attrName", "dataInfo"> 야할 것
        // 필요한 입력값 >>
        // 파일 업로드시 어느 테이블에 import 하고 싶은 건지! 테이블 이름
        // 임포트를 할떄 기존에 있던 테이블을 truncate 할지 말지 여부
    }

    /* SQL 쿼리문 입력에 대한 프론트, antlr, 백 순서를 어떻게 해야할까
    * 프론트 <-> 백 <-> antlr로 해야할지..
    * 프론트 -> antlr -> 백 -> 프론트로 해야할지
    * -> 결론 : 백에서 먼저 받고, antlr를 외부 api 취급해서 사용하기
    * */

    public BaseResponse<String> insertData(){
        // antlr 를 통한 구별이 데이터를 insert하는 것인 경우
        return null;
    }

    public BaseResponse<String> createTable(){
        return null;
    }
    @PostMapping("/submitQuery")
    public void submitQuery(@RequestParam("query") String query){
        // 쿼리문을 antlr에 전송해 map타입으로 들고 오기
    }


    @GetMapping("/run")
    public BaseResponse<ArrayList> antlr(@RequestParam("sql") String sql) throws IOException{

        String url = "http://antlr-api.shop:9000/antlr/run?sql=";
        ArrayList arrayList = antlrService.getData(url+sql);

        return new BaseResponse<>(arrayList);
    }

}
