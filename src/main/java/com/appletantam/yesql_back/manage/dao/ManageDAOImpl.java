package com.appletantam.yesql_back.manage.dao;

import com.appletantam.yesql_back.manage.dto.UserDatabaseDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ManageDAOImpl implements ManageDAO{

    @Autowired
    private SqlSession sqlSession;

    @Override
    public UserDatabaseDTO renameDB(Map<String, Object> map) {
        sqlSession.update("updateDBname", map);
        return sqlSession.selectOne("selectDatbase", map.get("dbCd"));
    }
}
