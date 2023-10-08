package com.appletantam.yesql_back.auth.service;

import com.appletantam.config.response.BaseException;
import com.appletantam.yesql_back.auth.dto.UserDTO;

public interface AuthService {
    public UserDTO addUser(UserDTO userDTO) throws BaseException;

    public String checkDuplicatedId(String userId);
}
