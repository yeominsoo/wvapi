package com.torange.api.common.util;

import lombok.Data;

public @Data class ExcuteQueryVO {
    String sql = "";
    String dbPoolName = "";
}
