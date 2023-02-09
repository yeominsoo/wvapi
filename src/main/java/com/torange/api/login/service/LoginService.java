package com.torange.api.login.service;


import com.torange.api.createPool.dao.vo.UserDbInfoVO;
import com.torange.api.login.dao.vo.LoginUserInfoVO;

import java.util.List;

public interface LoginService {

    List<UserDbInfoVO> loginProcess(LoginUserInfoVO userVo) throws Exception;

}
