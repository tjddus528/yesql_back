package com.appletantam.yesql_back.auth.service;

import com.appletantam.config.response.BaseException;
import com.appletantam.config.response.BaseResponse;
import com.appletantam.yesql_back.auth.dao.AuthDAO;
import com.appletantam.yesql_back.auth.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;

import static com.appletantam.config.response.BaseResponseStatus.DATABASE_ERROR;
import static com.appletantam.config.response.BaseResponseStatus.EXISTS_ID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthDAO authDAO;

    @Override
    public UserDTO addUser(UserDTO userDTO){
        return authDAO.adduser(userDTO);
    }

    @Override
    public String checkDuplicatedId(String userId) {
        if ( authDAO.checkDuplicatedId(userId) == null ) return "notDuplicate";
        else return "duplicate";
    }

    @Override
    public boolean login(UserDTO userDTO) {
        UserDTO dto = authDAO.selectLogin(userDTO);
        if ( dto != null ) { // dto 변수 내에 값이 있는 경우, 해당 아이디와 비밀번호를 가진 회원을 데베에서 찾았음 = 로그인 성공
            if (dto.getUserPassword().equals(userDTO.getUserPassword())) {
                System.out.println("login success");
                return true;
            }
        }

        return false;
    }

    @Override
    public void createDB(String dbName, String userId) {
        // userId를 가진 userCd를 들고와서 userCd 랑 dbName UserDatabase에 넣어주기

        String userCd = authDAO.selectCd(userId); // 유저의 코드 들고오기

        if ( userCd != null ){ // 잘 가져온 경우
            // DTO 객체에 해당하지 않는 여러개 값을 mapper에 들고 가고 싶은 경우 hashMap을 이용해서 들고 간다
            Map<String, String> map = new HashMap<String, String>();
            map.put("userCd", userCd);
            map.put("dbName", dbName);

            authDAO.addUserDatabase(map); // 유저의 코드에 맞는 데이터베이스 생성해주기
        }
    }

}
