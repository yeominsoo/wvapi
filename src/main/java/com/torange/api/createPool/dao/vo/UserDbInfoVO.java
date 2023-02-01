package com.torange.api.createPool.dao.vo;

import lombok.Data;


public @Data class UserDbInfoVO {
    String userId;
    String dbDriverName;
    String dbUrl;
    String dbType;
    String dbUser;
    String dbPw;
    String useFlg;
    int sortNo;

}