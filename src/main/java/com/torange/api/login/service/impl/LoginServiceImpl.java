package com.torange.api.login.service.impl;

import com.torange.api.createPool.dao.vo.UserDbInfoVO;
import com.torange.api.login.dao.LoginDAO;
import com.torange.api.login.dao.vo.LoginUserInfoVO;
import com.torange.api.login.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginDAO loginDAO;

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Override
    public List<UserDbInfoVO> loginProcess(LoginUserInfoVO userVo) throws Exception {
        return loginDAO.selectAccessUserInfo(userVo);
    }


}
