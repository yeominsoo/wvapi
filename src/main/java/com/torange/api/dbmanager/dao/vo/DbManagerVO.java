package com.torange.api.dbmanager.dao.vo;

import com.torange.api.login.dao.vo.LoginUserInfoVO;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
public @Data class DbManagerVO extends LoginUserInfoVO{
    // String userId;

    String seq;
    String dbType;
    String dbHost;
    String dbPort;
    String dbServiceName;
    String dbSid;

    String dbName;

    int dbListenType;
    String dbUser;
    String dbPw;

    String dbDriverClNm;
    String dbJdbcPath;
    String dbJdbcFile;
    String jdbcPath;

    String dbPoolName = "";

    String dbUrl = "";

    String useFlg;
    int sortNo;


}