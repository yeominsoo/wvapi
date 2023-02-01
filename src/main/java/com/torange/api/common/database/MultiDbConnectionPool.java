package com.torange.api.common.database;

import com.torange.api.common.util.Const;
import com.torange.api.common.util.DatasourceInfoVO;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MultiDbConnectionPool {

    private final static Logger log = LoggerFactory.getLogger(MultiDbConnectionPool.class);
    private final static Map<String, DataSource> DB_POOLMAP = new ConcurrentHashMap<>();


    public static synchronized Connection getConnection(DatasourceInfoVO dsInfo) throws Exception {
        if (!DB_POOLMAP.containsKey(dsInfo.getDbPoolName())) {
            createDataSource(dsInfo);
        }
        DataSource datasource = DB_POOLMAP.get(dsInfo.getDbPoolName());
        return datasource.getConnection();
    }

    public static void poolShutdown(DatasourceInfoVO dsInfo){
        HikariDataSource datasource = (HikariDataSource) DB_POOLMAP.get(dsInfo.getDbPoolName());
        if(datasource != null){
            datasource.close();
            DB_POOLMAP.remove(dsInfo.getDbPoolName());
        }
    }

    private static void createDataSource(DatasourceInfoVO dsInfo) throws Exception {
        if (DB_POOLMAP.containsKey(dsInfo.getDbPoolName())) {
            throw new Exception("Connection pool session is alive !");
        }
        HikariConfig hikariConfig = getHikariConfig(dsInfo);
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        log.info("current pool name : {} " , hikariDataSource.getPoolName());
        DB_POOLMAP.put(dsInfo.getDbPoolName(), hikariDataSource);
    }

    private static HikariConfig getHikariConfig(DatasourceInfoVO dsInfo) throws Exception {

        if (dsInfo.getDbUrl().isEmpty()) throw new Exception("This argument is not null [URL]");
        if (dsInfo.getDbUser().isEmpty()) throw new Exception("This argument is not null [Username]");
        if (dsInfo.getDbPw().isEmpty()) throw new Exception("This argument is not null [Password]");
        if (dsInfo.getDbDriverName().isEmpty()) throw new Exception("This argument is not null [Driver Class Name]");
        if (dsInfo.getDbPoolName().isEmpty()) throw new Exception("This argument is not null [Pool name]");

        HikariConfig hikaConfig = new HikariConfig();

        try {
            // This is same as passing the Connection info to the DriverManager class.
            // your jdbc url. in my case it is mysql.
            hikaConfig.setJdbcUrl(dsInfo.getDbUrl());
            // username
            hikaConfig.setUsername(dsInfo.getDbUser());
            // password
            hikaConfig.setPassword(dsInfo.getDbPw());
            // driver class name
            hikaConfig.setDriverClassName(dsInfo.getDbDriverName());

            // Information about the pool
            // pool name. This is optional you don't have to do it.
            hikaConfig.setPoolName(dsInfo.getDbPoolName());

            // the maximum connection which can be created by or resides in the pool
            hikaConfig.setMaximumPoolSize(Const.INT_CONNECTION_POOL_SIZE);

            // how much time a user can wait to get a connection from the pool.
            // if it exceeds the time limit then a SQlException is thrown
            hikaConfig.setConnectionTimeout(Duration.ofSeconds(Const.INT_CONNECTION_TIMEOUT).toMillis());

            // The maximum time a connection can sit idle in the pool.
            // If it exceeds the time limit it is removed form the pool.
            // If you don't want to retire the connections simply put 0.
            hikaConfig.setIdleTimeout(Duration.ofMinutes(Const.INT_IDLE_TIMEOUT).toMillis());

        }catch (Exception e){
            e.printStackTrace();
        }

        return hikaConfig;
    }

    public static Boolean isAliveConnectionPool(DatasourceInfoVO dsInfo)
    {
        return DB_POOLMAP.containsKey(dsInfo.getDbPoolName());
    }
}
