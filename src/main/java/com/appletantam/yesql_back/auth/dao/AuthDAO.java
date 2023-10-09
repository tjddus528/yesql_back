package com.appletantam.yesql_back.auth.dao;

import com.appletantam.config.response.BaseException;
import com.appletantam.yesql_back.auth.dto.UserDTO;

public interface AuthDAO {
    public UserDTO adduser(UserDTO userDTO);

    public String checkDuplicatedId(String userId);
}
