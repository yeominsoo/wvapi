package com.torange.api.common.util;

import lombok.Data;

public @Data class DatasourceInfoVO {

    String userId = "";
    String dbUrl = "";
    String dbUser = "";
    String dbPw = "";
    String dbType = "";
    String dbDriverName = "";
    String dbPoolName = "";

}
