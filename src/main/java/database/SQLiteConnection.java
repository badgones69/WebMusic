package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    private static final Logger LOG = LogManager.getLogger(SQLiteConnection.class);
    private static volatile Connection connection = null;
    private static Boolean connectionEstablished = Boolean.FALSE;

    private SQLiteConnection() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static String getPathDb() {
        String userHome = System.getProperty("user.home");
        return userHome + File.separator + "WebMusic.db";
    }

    public static Boolean isConnectionEstablished() {
        return connectionEstablished;
    }

    public static void connect() {
        //DATABASE FILE URL
        String url = "jdbc:sqlite:" + getPathDb();

        // DATABASE CONNECTION CREATION
        try {
            connection = DriverManager.getConnection(url);
            connectionEstablished = Boolean.TRUE;
        } catch (SQLException e) {
            LOG.error("SQLException : " + e.getMessage(), e);
        }
    }

    public static Connection getInstance() {
        synchronized (SQLiteConnection.class) {
            connect();
        }
        return connection;
    }
}