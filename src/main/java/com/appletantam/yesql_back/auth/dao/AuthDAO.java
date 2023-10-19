package com.appletantam.yesql_back.auth.dao;

import com.appletantam.yesql_back.auth.dto.UserDTO;
import com.appletantam.yesql_back.manage.dto.UserDatabaseDTO;

import java.util.Map;

public interface AuthDAO {
    public UserDTO adduser(UserDTO userDTO);

    public String checkDuplicatedId(String userId);

    public UserDTO selectLogin(UserDTO userDTO);

    public String selectCd(String userId);

    public void addUserDatabase(Map<String, String> map);

    public UserDatabaseDTO findDatabase(String userId);
}
