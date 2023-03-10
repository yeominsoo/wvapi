package com.torange.api.createPool.dao;

import com.torange.api.createPool.dao.vo.UserDbInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Mapper
@Repository
public interface CreatePoolDAO {
    List<UserDbInfoVO> selectUserDatabaseInfo(String userId) throws SQLException;
}

