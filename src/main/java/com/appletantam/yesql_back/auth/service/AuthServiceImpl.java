package com.appletantam.yesql_back.auth.service;

import com.appletantam.yesql_back.auth.dao.AuthDAO;
import com.appletantam.yesql_back.auth.dto.UserDTO;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthDAO authDAO;

    @Override
    public UserDTO addUser(UserDTO userDTO){
        return authDAO.adduser(userDTO);
    }

    @Override
    public boolean checkDuplicatedId(String userId) {
        try {
            if ( authDAO.checkDuplicatedId(userId) == null ) return true;       // 중복이 없을 때 true
            return false;                                                       // 중복된 값일 때 false
        } catch (TooManyResultsException exception) {
            return false;                                                       // 중복된 값일 때 false
        }
    }

    @Override
    public boolean login(UserDTO userDTO) {
        UserDTO dto = authDAO.selectLogin(userDTO);
        if ( dto != null ) { // dto 변수 내에 값이 있는 경우, 해당 아이디를 가진 회원을 데베에서 찾았음
            if (dto.getUserPassword().equals(userDTO.getUserPassword())) { // 비밀번호가 같은 경우 로그인 성공
                return true;
            }
        }
        return false;
    }


}
