package com.torange.api.createPool.service;


import java.sql.Connection;
import java.util.HashMap;

public interface CreatePoolService {

    HashMap<String, Object> excuteQueryTest(Connection connection, String sql) throws Exception;

}
