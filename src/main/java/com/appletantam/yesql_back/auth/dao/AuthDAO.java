package com.appletantam.yesql_back.auth.dao;

import com.appletantam.yesql_back.auth.dto.UserDTO;

import java.util.Map;

public interface AuthDAO {
    public UserDTO adduser(UserDTO userDTO);

    public String checkDuplicatedId(String userId);

    public UserDTO selectLogin(UserDTO userDTO);

}
