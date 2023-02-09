package com.torange.api.login.controller;

import com.torange.api.common.constant.Const;
import com.torange.api.createPool.dao.vo.UserDbInfoVO;
import com.torange.api.login.dao.vo.LoginUserInfoVO;
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

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);


    @PostMapping(value = "/loginProcess")
    @ResponseBody
    public HashMap<String, Object> loginProcess(HttpServletRequest request, @RequestBody LoginUserInfoVO userVo) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();
        HttpSession session = request.getSession();

        List<UserDbInfoVO> result = loginService.loginProcess(userVo);

        if(result == null || result.size() < 1){
            resultMap.put("result_code", HttpStatus.NO_CONTENT.value());
            resultMap.put("result_message", "no user");
            return resultMap;
        }

        session.setAttribute(Const.USER_INFO, result);
        resultMap.put("result_code", HttpStatus.OK.value());
        resultMap.put("result_message", "");
        resultMap.put(session.getId() + Const.SEPARATOR_AT + userVo.getUserId(), result);
        return resultMap;
    }
}
