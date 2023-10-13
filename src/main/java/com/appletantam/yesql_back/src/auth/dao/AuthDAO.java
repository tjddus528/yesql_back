package com.appletantam.yesql_back.src.auth.dao;

import com.appletantam.yesql_back.src.auth.dto.UserDTO;

import java.util.Map;

public interface AuthDAO {
    public UserDTO adduser(UserDTO userDTO);

    public String checkDuplicatedId(String userId);

    public UserDTO selectLogin(UserDTO userDTO);

    public String selectCd(String userId);

    public void addUserDatabase(Map<String, String> map);
}
