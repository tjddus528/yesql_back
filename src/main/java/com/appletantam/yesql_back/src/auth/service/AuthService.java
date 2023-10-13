package com.appletantam.yesql_back.src.auth.service;

import com.appletantam.yesql_back.src.auth.dto.UserDTO;

public interface AuthService {
    public UserDTO addUser(UserDTO userDTO);

    public String checkDuplicatedId(String userId);

    public boolean login(UserDTO userDTO);

    public void createDB(String dbName, String userId);
}
