package com.torange.api.createPool.service.impl;

import com.torange.api.createPool.dao.CreatePoolDAO;
import com.torange.api.createPool.service.CreatePoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service("createPoolService")
public class CreatePoolServiceImpl implements CreatePoolService {

    @Autowired
    private CreatePoolDAO createPoolDAO;

    private final Logger log = LoggerFactory.getLogger(CreatePoolServiceImpl.class);

    @Override
    public HashMap<String, Object> excuteQueryTest(Connection conn, String sql) throws Exception {

        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> resultMap = new HashMap<>();

        try {
            stmt = conn.prepareStatement(sql);
            if(stmt.execute()){
                resultSet = stmt.getResultSet();
            } else {
                resultMap.put("resultData", stmt.getUpdateCount());
            }

            if (resultSet != null) {
                HashMap<String, Object> dataMap;
                ResultSetMetaData rsmd = resultSet.getMetaData();

                int index = 1;
                // row data
                while (resultSet.next()) {
                    dataMap = new HashMap<>();
                     //col data
                    for(int i = 1; i <= rsmd.getColumnCount(); i++){
                        dataMap.put(rsmd.getColumnName(i), resultSet.getObject(rsmd.getColumnName(i)));
                    }
                    list.add(dataMap);
                    index++;
                }
                resultMap.put("resultData", list);
            }
            resultMap.put("resultCode", HttpStatus.OK.value());
            resultMap.put("resultMessage", HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            resultMap.put("resultCode", e.getErrorCode());
            resultMap.put("resultMessage", e.getMessage());
        } finally {
            if (resultSet != null) resultSet.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return resultMap;
    }
}
