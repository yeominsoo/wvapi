package com.torange.api.dbmanager.dao;

import com.torange.api.dbmanager.dao.vo.DbManagerVO;
import com.torange.api.login.dao.vo.LoginUserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Mapper
@Repository
public interface DbManagerDAO {
    List<DbManagerVO> selectDatabaseConfig(LoginUserInfoVO userVo) throws SQLException;
}
