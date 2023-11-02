package com.appletantam.yesql_back.auth.service;

import com.appletantam.yesql_back.auth.dto.UserDTO;
import com.appletantam.yesql_back.manage.dto.UserDatabaseDTO;

public interface AuthService {
    public UserDTO addUser(UserDTO userDTO);

    public boolean checkDuplicatedId(String userId);

    public boolean login(UserDTO userDTO);

    public void createDB(String dbName, String userId);

    public UserDatabaseDTO findDatabase(String userId);
}
