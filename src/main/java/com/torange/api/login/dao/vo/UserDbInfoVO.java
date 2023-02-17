package com.torange.api.login.dao.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
public @Data class UserDbInfoVO extends LoginUserInfoVO{
    // String userId;
    /* user database info */
    int sortNo;
    int dbListenType;
    String dbUser;
    String dbPw;
    String useFlg;
    String dbType;
    String dbHost;
    String dbPort;
    String dbServiceName;
    String dbSid;
    String dbName;

}