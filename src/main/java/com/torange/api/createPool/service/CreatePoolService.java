package com.torange.api.createPool.service;


import com.torange.api.createPool.dao.vo.UserDbInfoVO;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public interface CreatePoolService {

    List<UserDbInfoVO> selectUserDatabaseInfo(String userId) throws Exception;

    List<HashMap<String, Object>> selectConnectionTest(Connection connection, String sql) throws Exception;

}
