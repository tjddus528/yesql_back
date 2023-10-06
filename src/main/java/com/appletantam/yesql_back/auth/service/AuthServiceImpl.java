package com.appletantam.yesql_back.auth.service;

import com.appletantam.yesql_back.auth.dao.AuthDAO;
import com.appletantam.yesql_back.auth.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthDAO authDAO;

    @Override
    public void addUser(UserDTO userDTO) {
        authDAO.adduser(userDTO);
    }

    @Override
    public String checkDuplicatedId(String userId) {
        if ( authDAO.checkDuplicatedId(userId) == null ) return "notDuplicate";
        else return "duplicate";
    }
}
