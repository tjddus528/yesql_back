package com.appletantam.yesql_back.auth.dao;

import com.appletantam.yesql_back.auth.dto.UserDTO;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class AuthDAOImpl implements AuthDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public UserDTO adduser(UserDTO userDTO) {
        sqlSession.insert("auth.insertUser", userDTO);
        userDTO.setDbName(userDTO.getUserId());
        return userDTO;
    }

    @Override
    public String checkDuplicatedId(String userId) throws TooManyResultsException {
        return sqlSession.selectOne("auth.selectDuplicatedId", userId);
    }

    @Override
    public UserDTO selectLogin(UserDTO userDTO) {
        return sqlSession.selectOne("auth.selectLogin", userDTO);
    }

}
