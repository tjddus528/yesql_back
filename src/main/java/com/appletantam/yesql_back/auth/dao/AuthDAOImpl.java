package com.appletantam.yesql_back.auth.dao;

import com.appletantam.config.response.BaseException;
import com.appletantam.yesql_back.auth.dto.UserDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.appletantam.config.response.BaseResponseStatus.DATABASE_ERROR;

@Repository
public class AuthDAOImpl implements AuthDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public UserDTO adduser(UserDTO userDTO) {
        sqlSession.insert("auth.insertUser", userDTO);
        return userDTO;
    }

    @Override
    public String checkDuplicatedId(String userId) {
        return sqlSession.selectOne("auth.selectDuplicatedId", userId);
    }
}
