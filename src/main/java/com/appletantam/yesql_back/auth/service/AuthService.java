package com.appletantam.yesql_back.auth.service;

import com.appletantam.config.response.BaseException;
import com.appletantam.yesql_back.auth.dto.UserDTO;

public interface AuthService {
    public UserDTO addUser(UserDTO userDTO);

    public String checkDuplicatedId(String userId);

    public boolean login(UserDTO userDTO);

    public void createDB(String dbName, String userId);
}
