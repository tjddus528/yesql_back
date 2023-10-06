package com.appletantam.yesql_back.auth.dao;

import com.appletantam.yesql_back.auth.dto.UserDTO;

public interface AuthDAO {
    public void adduser(UserDTO userDTO);

    public String checkDuplicatedId(String userId);
}
