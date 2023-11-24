package com.appletantam.yesql_back.manage.controller;

import com.appletantam.yesql_back.config.response.BaseResponse;
import com.appletantam.yesql_back.manage.dto.SchemaDTO;
import com.appletantam.yesql_back.manage.service.ManageService;
import com.appletantam.yesql_back.sqlManager.SqlConnector;

import com.google.gson.JsonElement;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.*;


import static com.appletantam.yesql_back.config.response.BaseResponseStatus.*;

@RestController
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    private ManageService manageService;
    SqlConnector sqlConnector;

    @Value("${mysql.url}")
    public String mysqlUrl;
    @Value("${mysql.user}")
    public String user;
    @Value("${mysql.password}")
    public String password;

    @GetMapping("/test")
    public String test(@RequestParam("str") String str){
        //return "manage test success";
        return str;
    }

    public void mysqlConnect(String dbName) throws Exception {
        try{
            //ec2_mysql 연결
            sqlConnector = new SqlConnector(mysqlUrl, user, password);
            sqlConnector.useDB(dbName);

        } catch(Exception e){
            throw new Exception("데이터베이스 연결 오류 발생");
            //return new BaseResponse<>(DATABASE_ERROR);
        }
    }

    //schema 유저 데이터베이스의 모든 정보 뿌리기 (간단버전)
    @GetMapping("/schemas/simpleData")
    public BaseResponse<?> simpleData(@RequestParam("dbName") String dbName) throws Exception {
        //쿼리문 실행 하는 중에서 다른 쿼리문 실행 안됨 -> 테이블 다 찾고 난 다음에 리스트를 순회하면서 컬렁 등록해주기
        ArrayList<String> tables = new ArrayList<>();
        ArrayList<String> columns;

        SchemaDTO schemaDTO;
        List<SchemaDTO> schemaDTOList = new ArrayList<>();

        mysqlConnect(dbName); // ec2_mysql 연결 (컨트롤러의 sqlConnector 생성)
        // 전체 조회 쿼리 실행
        try{
            // 유저 데베 안에 모든 <테이블>을 보여주는 쿼리문 실행
            Statement stmt = sqlConnector.stmt;
            ResultSet rs = stmt.executeQuery("SHOW TABLES;");

            String getTableQuery = "Tables_in_" + dbName;
            // <테이블 리스트> 생성
            while (rs.next()) {
                String currentTable = rs.getString(getTableQuery);
                tables.add(currentTable);
            }

            // 테이블 리스트 순회 -> 각 테이블명에 해당하는 <컬럼> 조회 후 <컬럼 리스트> 생성
            for (String tableName : tables) {
                // 각 테이블의 <컬럼>을 보여주는 쿼리문 실행
                String getColumnQuery = "SHOW COLUMNS FROM " + tableName + ";";
                rs = stmt.executeQuery(getColumnQuery);

                columns = new ArrayList<>();
                // 각 테이블의 <컬럼 리스트> 생성
                while (rs.next()) {
                    columns.add(rs.getString("Field"));
                }

                schemaDTO = new SchemaDTO(tableName, columns, null);
                schemaDTOList.add(schemaDTO);
            }
            sqlConnector.closeConnection();

        } catch (Exception e) {
            return new BaseResponse<>(EXECUTE_SQL_ERROR);
        }

        return new BaseResponse<>(SUCCESS, schemaDTOList);
    }

    @GetMapping("/schemas/specificData")
    public BaseResponse<?> specificData(@RequestParam("dbName") String dbName) throws Exception {
        //쿼리문 실행 하는 중에서 다른 쿼리문 실행 안됨 -> 테이블 다 찾고 난 다음에 리스트를 순회하면서 컬렁 등록해주기
        ArrayList<String> tables = new ArrayList<>();
        SchemaDTO schemaDTO;
        List<SchemaDTO> schemaDTOList = new ArrayList<>();

        mysqlConnect(dbName); // ec2_mysql 연결 (컨트롤러의 sqlConnector 생성)

        // 전체 조회 쿼리 실행
        try{
            // 유저 데베 안에 모든 <테이블>을 보여주는 쿼리문 실행
            Statement stmt = sqlConnector.stmt;
            ResultSet rs = stmt.executeQuery("SHOW TABLES;");

            String getTableQuery = "Tables_in_" + dbName;

            // <테이블 리스트> 생성
            while (rs.next()) {
                String currentTable = rs.getString(getTableQuery);
                tables.add(currentTable);
            }

            // 테이블 리스트 순회 -> 각 테이블명에 해당하는 <컬럼> 조회 후 <컬럼 리스트> 생성
            for (String tableName : tables) {
                schemaDTO = manageService.simpleTableData(dbName, tableName);
                schemaDTOList.add(schemaDTO);
            }
            sqlConnector.closeConnection();

        } catch (Exception e) {
            return new BaseResponse<>(EXECUTE_SQL_ERROR);
        }

        return new BaseResponse<>(SUCCESS, schemaDTOList);
    }

    /*
    @PostMapping("/import")
    public BaseResponse<?> dataImport(@RequestParam("dbName") String dbName, @RequestParam("tableName") String tableName, @RequestParam("file") MultipartFile file) throws IOException {
        // 테이블 단위의 데이터를 받는다. 이때 형식은 <"attrName", "dataInfo"> 야할 것
        // 필요한 입력값 >>
        // 파일 업로드시 어느 테이블에 import 하고 싶은 건지! 테이블 이름
        // 임포트를 할떄 기존에 있던 테이블을 truncate 할지 말지 여부

        JSONParser parser = new JSONParser();

        try{
            FileReader reader = new FileReader(file);
            Object obj = parser.parse(reader);
            byte[] fileBytes = file.getBytes();
            String fileContent = new String(fileBytes);

        } catch ( IOException e ){
            return new BaseResponse<>(FILE_ERROR);
        }
        return null;
    }*/

    @PostMapping("/import")
    public ResponseEntity<?> importData(@RequestPart MultipartFile uploadFile, @RequestParam("dbName") String dbName, @RequestParam("tableName") String tableName){
        JSONParser parser = new JSONParser();

        //파일이 존재하는 경우 읽기
        if ( !uploadFile.isEmpty() ){
            try{
                InputStreamReader ir = new InputStreamReader(uploadFile.getInputStream());
                //BufferedReader br = new BufferedReader(ir);

                Object obj = parser.parse(ir);
                JSONArray jsonArray = (JSONArray) obj;

                String query = "INSERT INTO " + dbName + "." + tableName + "(";
                boolean check = false;

                if (!jsonArray.isEmpty()){ // 빈 파일이 아닌 경우 한 줄씩 뽑아준다
                    for (Object o : jsonArray) {
                        JSONObject jsonObj = (JSONObject) o; // 배열을 jsonObject로 자료형 변경
                        for (Object object : jsonObj.entrySet()) {
                            Map.Entry<String, String> entry = (Map.Entry<String, String>) object;
                            if ( !check ){

                                check = true;
                            }
                            System.out.println(entry.getKey() + " : " + entry.getValue());
                        }

                    }
                }
//                String line;
//                while( (line = br.readLine()) != null ){
//                    System.out.println(line);
//                }

                //br.close();

            } catch (IOException e){
                e.printStackTrace();
            } catch (ParseException pe) {
                throw new RuntimeException(pe);
            }

        }

        return null;
    }



    public ResponseEntity<byte[]> downloadFile(String FileName) throws IOException {
        // 실제 파일의 경로
        File file = new File(FileName);

        // 파일이 존재하는지 확인
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 파일 내용을 읽기 위한 byte 배열
        byte[] content = Files.readAllBytes(file.toPath());

        // Content-Disposition 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", file.getName());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // ResponseEntity를 사용하여 파일을 반환
        return ResponseEntity.ok()
                .headers(headers)
                .body(content);
    }

    @GetMapping("/export")
    public void dataExport(HttpServletResponse response, @RequestParam("dbName") String dbName, @RequestParam("tableName") String tableName) throws Exception {
        // ec2_mysql 연결 -> 임의의 테이블 정보 가져오기 -> json파일 생성 -> 반환 -> 파일 삭제
        // 파일 생성 장소 지정 -> 생성 시 다운로드 -> 파일 삭제
        // 생성 장소 : 서버 내부 주소

        // 1. ec2_mysql 연결
        mysqlConnect(dbName);

        // 2. export할 테이블 정보 가져오기
        SchemaDTO schemaDTO = manageService.simpleTableData(dbName, tableName);
        ArrayList<LinkedHashMap<String, String>> rows = schemaDTO.getRows(); // 링크드해시맵 어레이리스트

        FileWriter fw;
        String fileName = tableName + ".json";

        // 3. json파일 생성
        try{
            fw = new FileWriter(fileName);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JSONArray list = new JSONArray();
            String totalData = "";

            for ( LinkedHashMap<String, String> dto : rows ){ // 한줄씩 읽기 (링크드해시맵)
                JSONObject obj = new JSONObject();
                for ( Map.Entry<String, String> pair : dto.entrySet() ){ // 해당 한줄을 JSON 한줄로 변경
                    // 여기서 각 키 밸류를 추출하기 위해 사용하는 Entry는 Map에만 있기 때문에 순서가 차례대로가 아니라 정렬되어서 들어옴. 어쩔 수 없음 ~
                    obj.put(pair.getKey(), pair.getValue());
                }

                list.add(obj);
            }

            totalData += gson.toJson(list);
            fw.write(totalData);
            fw.flush();
            fw.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        // 4. 파일 보내기 (반환)
        ResponseEntity<byte[]> exportFile = downloadFile(fileName);
        File file = new File(fileName);

        FileInputStream in = new FileInputStream(fileName);
        OutputStream os = response.getOutputStream();

        int length;
        byte[] b = new byte[(int)file.length()];

        while ((length = in.read(b)) > 0) {
            os.write(b,0,length);
        }

        // os.write(b);
        os.flush();
        os.close();
        in.close();

        // 5. 파일 삭제하기
//        if ( file.exists() && !fileName.isEmpty()){
//            file.delete();
//        }

        //return exportFile;
    }



}