package com.torange.api.dbmanager.service;

import java.sql.Connection;
import java.util.HashMap;

public interface DbManagerService {

    HashMap<String, Object> excuteQueryTest(Connection connection, String sql) throws Exception;
}
