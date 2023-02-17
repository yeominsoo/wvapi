package com.torange.api.login.controller;

import com.torange.api.common.constant.Const;
import com.torange.api.dbmanager.dao.vo.DbManagerVO;
import com.torange.api.dbmanager.service.DbManagerService;
import com.torange.api.login.dao.vo.LoginUserInfoVO;
import com.torange.api.login.dao.vo.UserDbInfoVO;
import com.torange.api.login.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/warevalley/torange/api", produces = "application/json; charset=utf-8")
public class LoginController {

    @Resource(name = "loginService")
    private LoginService loginService;

    @Resource(name = "dbManagerService")
    private DbManagerService dbManagerService;

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);


    @PostMapping(value = "/loginProcess")
    @ResponseBody
    public HashMap<String, Object> loginProcess(HttpServletRequest request, @RequestBody LoginUserInfoVO userVo) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();
        HashMap<String, Object> initMap = new HashMap<>();
        HttpSession session = request.getSession();

        List<UserDbInfoVO> accessUserInfo = loginService.loginProcess(userVo);
        List<DbManagerVO> dbConfigInfo = dbManagerService.selectDatabaseConfig(userVo);

        if (accessUserInfo.isEmpty()) {
            resultMap.put("result_code", HttpStatus.NO_CONTENT.value());
            resultMap.put("result_message", "no user");
            return resultMap;
        }

        if (!dbConfigInfo.isEmpty()) {
            initMap.put("init_data", dbConfigInfo);
        }

        session.setAttribute(Const.USER_DB_INFO, accessUserInfo);
        session.setAttribute("userId", userVo.getUserId());

        resultMap.put("result_code", HttpStatus.OK.value());
        resultMap.put("result_message", "");
        resultMap.put("result_data", accessUserInfo);
        resultMap.put("init_data", initMap);

        return resultMap;
    }
}
