package com.torange.api.login.dao.vo;

import lombok.Data;


public @Data class LoginUserInfoVO {
    String userId;
    String userPw;
    String userNm;
    String userIpAddr;
    String pwFailCnt;
    String useFlg;
    String lockFlg;

}