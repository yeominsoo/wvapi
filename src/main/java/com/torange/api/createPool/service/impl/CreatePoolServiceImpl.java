package com.torange.api.createPool.service.impl;

import com.torange.api.createPool.dao.CreatePoolDAO;
import com.torange.api.createPool.dao.vo.UserDbInfoVO;
import com.torange.api.createPool.service.CreatePoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service("createPoolService")
public class CreatePoolServiceImpl implements CreatePoolService {

    @Autowired
    private CreatePoolDAO createPoolDAO;

    private static final Logger log = LoggerFactory.getLogger(CreatePoolServiceImpl.class);

    @Override
    public List<UserDbInfoVO> selectUserDatabaseInfo(String userId) throws Exception {
        return createPoolDAO.selectUserDatabaseInfo(userId);
    }


    @Override
    public List<HashMap<String, Object>> selectConnectionTest(Connection conn, String sql) throws Exception {

        Statement stmt = null;
        ResultSet resultSet = null;

        List<HashMap<String, Object>> resultList = new ArrayList<>();

        try {
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sql);

            if (resultSet != null) {
                HashMap<String, Object> resultMap = new HashMap<>();
                ResultSetMetaData rsmd = resultSet.getMetaData();

                int index = 1;
                // row data
                while (resultSet.next()) {
                    resultMap.clear();
                     //col data
                    for(int i = 1; i <= rsmd.getColumnCount(); i++){
                        resultMap.put(rsmd.getColumnName(i), resultSet.getObject(rsmd.getColumnName(i)));
                    }
                    resultList.add(resultMap);
                    index++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) resultSet.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return resultList;
    }
}
