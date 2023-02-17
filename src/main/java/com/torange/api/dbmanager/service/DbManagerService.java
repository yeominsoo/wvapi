package com.torange.api.dbmanager.service;

import com.torange.api.dbmanager.dao.vo.DbManagerVO;
import com.torange.api.login.dao.vo.LoginUserInfoVO;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public interface DbManagerService {

    HashMap<String, Object> excuteQueryTest(Connection connection, String sql) throws Exception;

    List<DbManagerVO> selectDatabaseConfig(LoginUserInfoVO userVo) throws Exception;

}
