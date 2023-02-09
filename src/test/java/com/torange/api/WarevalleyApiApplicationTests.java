package com.torange.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WarevalleyApiApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(JdbcPath.ORACLE_DB.getJdbcPath());
        System.out.println(JdbcPath.ORACLE_DB.getDriverClass());
        System.out.println(JdbcPath.ORACLE_DB.getTimeoutKey());
    }

    public enum JdbcPath {

        ORACLE_DB("oracle.jdbc.driver.OracleDriver", "/jdbc/oracle/ojdbc8-19.3.0.0.jar", "oracle.net.CONNECT_TIMEOUT"),
        MYSQL_DB("com.mysql.jdbc.Driver", "/jdbc/mysql/mysql-connector-java-8.0.28.jar", "loginTimeout"),
        POSTGRES_DB("org.postgresql.Driver", "/jdbc/postgres/postgresql-42.5.1.jar", "login_timeout"),
        MARIA_DB("org.mariadb.jdbc.Driver", "/jdbc/maria/mariadb-java-client-2.7.5.jar", "loginTimeout");

        private final String driverClass;
        private final String jdbcPath;
        private final String timeoutKey;

        JdbcPath(String jdbcPath, String driverClass, String timeoutKey) {
            this.jdbcPath = jdbcPath;
            this.driverClass = driverClass;
            this.timeoutKey = timeoutKey;
        }

        public String getJdbcPath() {
            return this.jdbcPath;
        }

        public String getDriverClass() {
            return this.driverClass;
        }

        public String getTimeoutKey() {
            return this.timeoutKey;
        }

    }
}
