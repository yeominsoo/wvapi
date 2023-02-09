package com.torange.api.common.vo;

import lombok.Data;

public @Data class ExcuteQueryVO {
    String sql = "";
    String dbPoolName = "";
}
