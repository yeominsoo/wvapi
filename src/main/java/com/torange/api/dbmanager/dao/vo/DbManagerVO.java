package com.torange.api.dbmanager.dao.vo;

import lombok.Data;


public @Data class DbManagerVO{
    /* jdbc Config info */
    String seq;
    String dbDriverClNm;
    String dbJdbcPath;
    String jdbcPath;
    String dbJdbcFile;

    /*  */
    String dbUrl = "";
    String dbPoolName = "";

}