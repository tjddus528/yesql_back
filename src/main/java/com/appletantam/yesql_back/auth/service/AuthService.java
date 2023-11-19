package com.appletantam.yesql_back.auth.service;

import com.appletantam.yesql_back.auth.dto.UserDTO;

public interface AuthService {
    public UserDTO addUser(UserDTO userDTO);

    public boolean checkDuplicatedId(String userId);

    public boolean login(UserDTO userDTO);

}
