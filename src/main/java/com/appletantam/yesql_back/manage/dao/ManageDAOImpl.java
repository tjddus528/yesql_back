package com.appletantam.yesql_back.manage.dao;

import com.appletantam.yesql_back.manage.dto.DataDTO;
import com.appletantam.yesql_back.manage.dto.UserDatabaseDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ManageDAOImpl implements ManageDAO{

    @Autowired
    private SqlSession sqlSession;

    @Override
    public UserDatabaseDTO renameDB(Map<String, Object> map) {
        sqlSession.update("manage.updateDBname", map);
        return sqlSession.selectOne("manage.selectDatbase", map.get("dbCd"));
    }

    @Override
    public List<DataDTO> selectDataList(long dbCd) {
        return sqlSession.selectList("manage.selectDataList", dbCd);
    }
}
