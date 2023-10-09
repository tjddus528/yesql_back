package com.appletantam.yesql_back.auth.service;

import com.appletantam.config.response.BaseException;
import com.appletantam.config.response.BaseResponse;
import com.appletantam.yesql_back.auth.dao.AuthDAO;
import com.appletantam.yesql_back.auth.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.appletantam.config.response.BaseResponseStatus.DATABASE_ERROR;
import static com.appletantam.config.response.BaseResponseStatus.EXISTS_ID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthDAO authDAO;

    @Override
    public UserDTO addUser(UserDTO userDTO) throws BaseException {

        // 중복 여부 검사
        if(checkDuplicatedId(userDTO.getUserId()).equals("duplicate")){
            throw new BaseException(EXISTS_ID);
        }

        return authDAO.adduser(userDTO);

    }

    @Override
    public String checkDuplicatedId(String userId) {
        if ( authDAO.checkDuplicatedId(userId) == null ) return "notDuplicate";
        else return "duplicate";
    }
}
