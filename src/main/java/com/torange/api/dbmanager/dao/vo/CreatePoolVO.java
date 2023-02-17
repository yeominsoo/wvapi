package com.torange.api.dbmanager.dao.vo;

import lombok.Data;


public @Data class CreatePoolVO {
    /* jdbc Config info */
    String dbHost;
    String dbPort;
    String dbUser;
    String dbPw;
    int dbListenType;
    String dbServiceName;
    String dbSid;
    String dbName;
    String dbType;
    String dbDriverClNm;
    String jdbcPath;

    String dbUrl = "";
    String dbPoolName = "";
}