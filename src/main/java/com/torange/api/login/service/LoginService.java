package com.torange.api.login.service;


import com.torange.api.dbmanager.dao.vo.DbManagerVO;
import com.torange.api.login.dao.vo.LoginUserInfoVO;

import java.util.List;

public interface LoginService {

    List<DbManagerVO> loginProcess(LoginUserInfoVO userVo) throws Exception;

}
