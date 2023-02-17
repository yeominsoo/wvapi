package com.torange.api.common.validation;

import com.torange.api.common.constant.Const;
import com.torange.api.dbmanager.dao.vo.DbManagerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class ConnectionAuthValidation {

    private static final Logger log = LoggerFactory.getLogger(ConnectionAuthValidation.class);

    private ConnectionAuthValidation() {
        super();
    }

    public static boolean validateConnectionAuth(HttpSession session, DbManagerVO dbInfo) {
        try {
            List<DbManagerVO> dbInfoSession = (List<DbManagerVO>) session.getAttribute(Const.USER_INFO);

            if (dbInfoSession != null) {
                // host:port 접근이 가능한지 여부만 체크.
                long filterCount = dbInfoSession.stream().filter(
                                map -> map.getDbHost().equals(dbInfo.getDbHost())
                                        && map.getDbPort().equals(dbInfo.getDbPort()))
                        .count();
                if (filterCount != 0) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getMessage(DbManagerVO dsInfo) {

        if (dsInfo.getDbHost().isEmpty()) return new Exception("Argument is null [HOST]").getMessage();
        if (dsInfo.getDbPort().isEmpty()) return new Exception("Argument is null [PORT]").getMessage();
        if (dsInfo.getDbUser().isEmpty()) return new Exception("Argument is null [Username]").getMessage();
        if (dsInfo.getDbPw().isEmpty()) return new Exception("Argument is null [Password]").getMessage();
        if (dsInfo.getDbDriverClNm().isEmpty())
            return new Exception("Argument is null [Driver Class Name]").getMessage();
        if (dsInfo.getDbPoolName().isEmpty()) return new Exception("Argument is null [Pool name]").getMessage();

        return "";
    }

    public static boolean isVariableNull(DbManagerVO dsInfo) {
        boolean bNull = false;

        if (dsInfo.getDbHost().isEmpty()) bNull = true;
        if (dsInfo.getDbPort().isEmpty()) bNull = true;
        if (dsInfo.getDbUser().isEmpty()) bNull = true;
        if (dsInfo.getDbPw().isEmpty()) bNull = true;
        if (dsInfo.getDbDriverClNm().isEmpty()) bNull = true;
        if (dsInfo.getDbPoolName().isEmpty()) bNull = true;

        return bNull;
    }
}
