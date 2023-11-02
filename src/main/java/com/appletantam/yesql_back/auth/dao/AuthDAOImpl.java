package com.appletantam.yesql_back.auth.dao;

import com.appletantam.yesql_back.auth.dto.UserDTO;
import com.appletantam.yesql_back.manage.dto.UserDatabaseDTO;
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

    @Override
    public String selectCd(String userId) { return sqlSession.selectOne("auth.selectCd", userId); }

    @Override
    public void addUserDatabase(Map<String, String> map) { sqlSession.insert("auth.insertUserDatabase", map); }

    // userId를 통해 userCd의 데베를 가져오기
    @Override
    public UserDatabaseDTO findDatabase(String userId) {
        return sqlSession.selectOne("auth.selectDB", userId);
    }
}
