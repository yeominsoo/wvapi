package com.torange.api.createPool.controller;

import com.torange.api.common.database.MultiDbConnectionPool;
import com.torange.api.common.util.Const;
import com.torange.api.common.util.DatasourceInfoVO;
import com.torange.api.common.util.ExcuteQueryVO;
import com.torange.api.createPool.dao.vo.UserDbInfoVO;
import com.torange.api.createPool.service.CreatePoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/warevalley/torange/api", produces = "application/json; charset=utf-8")
public class CreatePoolController {

    @Resource(name = "createPoolService")
    private CreatePoolService createPoolService;

    private static final Logger log = LoggerFactory.getLogger(CreatePoolController.class);


    @PostMapping(value = "/selectUserDatabaseInfo")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public HashMap<String, Object> selectUserDatabaseInfo(HttpServletRequest request, @RequestBody String userId) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();
        HttpSession session = request.getSession();

        List<UserDbInfoVO> result = createPoolService.selectUserDatabaseInfo(userId);

        resultMap.put(session.getId() + Const.SUFFIX_AT + userId, result);

        return resultMap;
    }

    @PostMapping(value = "/createUserConnectionPool")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public HashMap<String, Object> createUserConnectionPool(HttpServletRequest request
            , @RequestBody DatasourceInfoVO dsInfo) throws Exception {

        HashMap<String, Object> resultMap = new HashMap<>();
        HttpSession session = request.getSession();
        String dbPoolName = session.getId() + Const.SUFFIX_AT + dsInfo.getUserId() + Const.SUFFIX_AT + dsInfo.getDbType();

        dsInfo.setDbPoolName(dbPoolName);
        Connection conn = MultiDbConnectionPool.getConnection(dsInfo);

        resultMap.put("dbPoolName", dsInfo.getDbPoolName());
        return resultMap;
    }


    @PostMapping(value = "/selectConnectionTest")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public HashMap<String, Object> selectConnectionTest(HttpServletRequest request, @RequestBody ExcuteQueryVO paramVO){
        HttpSession session = request.getSession();

        DatasourceInfoVO dsInfo = new DatasourceInfoVO();
        HashMap<String, Object> resultMap = new HashMap<>();
        List<HashMap<String, Object>> result = null;

        try {
            dsInfo.setDbPoolName(session.getId() + Const.SUFFIX_AT + paramVO.getDbPoolName());

            if(!MultiDbConnectionPool.isAliveConnectionPool(dsInfo)){
                String errMessage = new Exception("Connection has been closed!\nPlease create connections").getMessage();
                resultMap.put("result", errMessage);
                return resultMap;
            }
            Connection connection = MultiDbConnectionPool.getConnection(dsInfo);

            result = createPoolService.selectConnectionTest(connection, paramVO.getSql());
        } catch (Exception e) {
            e.printStackTrace();
        }

        resultMap.put("result", result);
        return resultMap;
    }

    @PostMapping(value = "/poolShutdown")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void poolShutdown(HttpServletRequest request, @RequestBody ExcuteQueryVO paramVO){
        HttpSession session = request.getSession();
        DatasourceInfoVO dsInfo = new DatasourceInfoVO();

        try {
            dsInfo.setDbPoolName(session.getId() + Const.SUFFIX_AT + paramVO.getDbPoolName());
            MultiDbConnectionPool.poolShutdown(dsInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
