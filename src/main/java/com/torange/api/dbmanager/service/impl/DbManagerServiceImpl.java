package com.torange.api.dbmanager.service.impl;

import com.torange.api.common.util.DatabaseUtil;
import com.torange.api.dbmanager.dao.DbManagerDAO;
import com.torange.api.dbmanager.service.DbManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service("dbManagerService")
public class DbManagerServiceImpl implements DbManagerService {
    @Autowired
    private DbManagerDAO dbManagerDAO;

    private final Logger log = LoggerFactory.getLogger(DbManagerServiceImpl.class);

    @Override
    public HashMap<String, Object> excuteQueryTest(Connection conn, String sql) throws Exception {

        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> resultMap = new HashMap<>();

        log.info("query : {}", sql);

        try {
            stmt = conn.prepareStatement(sql);

            // 쿼리 유형 확인.
            int sqlType = DatabaseUtil.getSqlType(sql);

            // 쿼리 유옇에 따라 executeUpdate, executeQuery 사용으로 구분.
            if (sqlType == 0) {
                /* 조회 처리 */
                stmt.executeQuery();
                resultSet = stmt.getResultSet();
            } else {
                /* 조회 이외 처리 */
                int rst = stmt.executeUpdate();

                String msg = "";
                if (sqlType == 1) msg = "추가";
                else if (sqlType == 2) msg = "수정";
                else if (sqlType == 3) msg = "삭제";

                if (rst < 0) {
                    msg = "처리";
                    resultMap.put("resultMessage", String.format("%s되었습니다.", msg));
                } else {
                    resultMap.put("resultMessage", String.format("%s건이 %s되었습니다.", rst, msg));
                }
                resultMap.put("resultData", rst);
            }
            // 조회
            if (resultSet != null) {
                HashMap<String, Object> dataMap;
                ResultSetMetaData rsData = resultSet.getMetaData();

                int index = 0;
                // row data
                while (resultSet.next()) {
                    dataMap = new HashMap<>();
                    // col data
                    for (int i = 1; i <= rsData.getColumnCount(); i++) {
                        dataMap.put(rsData.getColumnName(i), resultSet.getObject(rsData.getColumnName(i)));
                    }
                    list.add(index, dataMap);
                    index++;
                }

                resultMap.put("resultMessage", String.format("%s 건이 조회되었습니다.", index));
                resultMap.put("resultData", list);
                // resultMap.put("resultData2", resultSet.getMetaData());
            }
            resultMap.put("resultCode", HttpStatus.OK.value());
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
