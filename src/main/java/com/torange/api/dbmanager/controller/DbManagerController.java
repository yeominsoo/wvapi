package com.torange.api.dbmanager.controller;

import com.torange.api.common.constant.Const;
import com.torange.api.common.database.MultiDbConnectionPool;
import com.torange.api.common.validation.ConnectionAuthValidation;
import com.torange.api.common.vo.ExcuteQueryVO;
import com.torange.api.dbmanager.dao.vo.CreatePoolVO;
import com.torange.api.dbmanager.service.DbManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.HashMap;

    @RestController
    @RequestMapping(path = "/warevalley/torange/api", produces = "application/json; charset=utf-8")
    public class DbManagerController {

        @Resource(name = "dbManagerService")
        private DbManagerService dbManagerService;

        private static final Logger log = LoggerFactory.getLogger(DbManagerController.class);

        @PostMapping(value = "/createUserConnectionPool")
        @ResponseStatus(HttpStatus.OK)
        @ResponseBody
        public HashMap<String, Object> createUserConnectionPool(HttpServletRequest request, @RequestBody CreatePoolVO dsInfo) throws Exception {

            HashMap<String, Object> resultMap = new HashMap<>();
            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("userId");

            boolean authFlg = ConnectionAuthValidation.validateConnectionAuth(session, dsInfo);
            if (!authFlg  ) {
                resultMap.put("resultCode", HttpStatus.FORBIDDEN);
                resultMap.put("resultMessage", "Please check Authorization");
                return resultMap;
            }

            String dbPoolName = session.getId() + Const.SEPARATOR_AT + userId + Const.SEPARATOR_AT + dsInfo.getDbType();

            dsInfo.setDbPoolName(dbPoolName);
            MultiDbConnectionPool.createConnectionPool(dsInfo);

            resultMap.put("resultCode", HttpStatus.OK);
            resultMap.put("resultMessage", "Created Connection pool");
            resultMap.put(Const.POOL_NAME, dsInfo.getDbPoolName());
            return resultMap;
        }


        @PostMapping(value = "/excuteQueryTest")
        @ResponseStatus(HttpStatus.OK)
        @ResponseBody
        public HashMap<String, Object> excuteQueryTest(HttpServletRequest request, @RequestBody ExcuteQueryVO paramVO) {
            HashMap<String, Object> resultMap = new HashMap<>();
            HashMap<String, Object> result = null;

            HttpSession session = request.getSession();

            try {
                String poolName = session.getId() + Const.SEPARATOR_AT + paramVO.getDbPoolName();

                if (!MultiDbConnectionPool.isAliveConnectionPool(poolName)) {
                    String errMessage = new Exception("Connection has been closed! Please create connections").getMessage();
                    resultMap.put("result", errMessage);
                    return resultMap;
                }
                Connection conn = MultiDbConnectionPool.getConnection(poolName);

                result = dbManagerService.excuteQueryTest(conn, paramVO.getSql());
            } catch (Exception e) {
                e.printStackTrace();
            }

            resultMap.put("result", result);
            return resultMap;
        }

        @PostMapping(value = "/poolShutdown")
        @ResponseStatus(HttpStatus.OK)
        @ResponseBody
        public void poolShutdown(HttpServletRequest request, @RequestBody ExcuteQueryVO paramVO) {
            HttpSession session = request.getSession();

            try {
                String poolName = session.getId() + Const.SEPARATOR_AT + paramVO.getDbPoolName();
                MultiDbConnectionPool.poolShutdown(poolName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
