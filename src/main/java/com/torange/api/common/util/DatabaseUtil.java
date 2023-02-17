package com.torange.api.common.util;

import com.torange.api.common.constant.Const;
import com.torange.api.dbmanager.dao.vo.CreatePoolVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DatabaseUtil {

    private static final Logger log = LoggerFactory.getLogger(DatabaseUtil.class);

    private final static Map<String, String> JDBC_DRIVER_MAP = new ConcurrentHashMap<>();

    public static void invokeJdbcDriver(CreatePoolVO dsInfo) throws Exception {
        //if (!JDBC_DRIVER_MAP.containsKey(dsInfo.getDbType()) && !Const.POSTGRES_DB.equals(dsInfo.getDbType())) {
        if (!JDBC_DRIVER_MAP.containsKey(dsInfo.getJdbcPath())) {
            log.info("======== jdbc driver load process start ========");

            URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            log.info("class path :: {}", new ClassPathResource(dsInfo.getJdbcPath()).getURL());
            //Class<?> loadclass = sysloader.loadClass(new ClassPathResource(dsInfo.getJdbcPath()).getURL().toString());
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(sysloader, (Object[]) new URL[]{new ClassPathResource(dsInfo.getJdbcPath()).getURL()});
            JDBC_DRIVER_MAP.put(dsInfo.getJdbcPath(), dsInfo.getJdbcPath());
            //JDBC_DRIVER_MAP.put(dsInfo.getDbType(), dsInfo.getDbType());

            log.info("======== jdbc driver load process end ========");
        }
    }

    public static void makeDatabaseUrl(CreatePoolVO dsInfo) {
        String resultUrl = "";
        switch (dsInfo.getDbType()) {
            case Const.ORACLE_DB:
                if (dsInfo.getDbListenType() == 0)
                    resultUrl = String.format("jdbc:oracle:thin:@%s:%s:%s", dsInfo.getDbHost(), dsInfo.getDbPort(), dsInfo.getDbSid());
                else
                    resultUrl = String.format("jdbc:oracle:thin:@%s:%s/%s", dsInfo.getDbHost(), dsInfo.getDbPort(), dsInfo.getDbServiceName());
                break;
            case Const.MARIA_DB:
                resultUrl = String.format("jdbc:mariadb://%s:%s/%s", dsInfo.getDbHost(), dsInfo.getDbPort(), dsInfo.getDbName());
                break;
            case Const.POSTGRES_DB:
                resultUrl = String.format("jdbc:postgresql://%s:%s/%s", dsInfo.getDbHost(), dsInfo.getDbPort(), dsInfo.getDbServiceName());
                break;
            case Const.MYSQL_DB:
                resultUrl = String.format("jdbc:mysql://%s:%s/%s", dsInfo.getDbHost(), dsInfo.getDbPort(), dsInfo.getDbName());
                break;
        }
        dsInfo.setDbUrl(resultUrl);
    }

    public static int getSqlType(String sql) {
        int sqlType = 5;
        sql = sql.trim();

        if (sql.toUpperCase().contains("SELECT")
                && sql.toUpperCase().contains("FROM")
                && !sql.toUpperCase().contains("INSERT")) {
            sqlType = 0;
        }else if (sql.toUpperCase().indexOf("INSERT") == 0
                && sql.toUpperCase().indexOf("INTO") != 0 ) {
            sqlType = 1;
        } else if (sql.toUpperCase().indexOf("UPDATE") == 0
                && sql.toUpperCase().indexOf("SET") != 0) {
            sqlType = 2;
        } else if (sql.toUpperCase().indexOf("DELETE") == 0
                && sql.toUpperCase().indexOf("FROM") != 0) {
            sqlType = 3;
        } else {
            sqlType = 4;
        }

        log.info("sql type = {}", sqlType);

        return sqlType;
    }

}
