package com.appletantam.yesql_back.manage.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ManageDAOImpl implements ManageDAO{

    @Autowired
    private SqlSession sqlSession;
}
