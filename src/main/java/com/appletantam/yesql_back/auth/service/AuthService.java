package com.appletantam.yesql_back.auth.service;

import com.appletantam.yesql_back.auth.dto.UserDTO;

public interface AuthService {
    public void addUser(UserDTO userDTO);

    public String checkDuplicatedId(String userId);
}
