package com.torange.api.login.dao;

import com.torange.api.createPool.dao.vo.UserDbInfoVO;
import com.torange.api.login.dao.vo.LoginUserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Mapper
@Repository
public interface LoginDAO {
    List<UserDbInfoVO> selectAccessUserInfo(LoginUserInfoVO userVo) throws SQLException;
}

