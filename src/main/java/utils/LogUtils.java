package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class LogUtils {
    private static final String IO_EXCEPTION = "IOException : {0}";
    private static final String SQL_EXCEPTION = "SQLException : {0}";

    private LogUtils() {
        generateConstructorLog(LogUtils.class);
    }

    public static void generateConstructorLog(Class<? extends Object> classe) {
        Logger log = LogManager.getLogger(classe);
        log.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static void generateIOExceptionLog(Class<? extends Object> classe, IOException e) {
        Logger log = LogManager.getLogger(classe);
        log.error(IO_EXCEPTION, e.getMessage(), e);
    }


    public static void generateSQLExceptionLog(Class<? extends Object> classe, SQLException e) {
        Logger log = LogManager.getLogger(classe);
        log.error(SQL_EXCEPTION, e.getMessage(), e);
    }

}
