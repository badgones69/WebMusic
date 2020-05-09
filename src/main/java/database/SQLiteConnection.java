package database;

import utils.LogUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    private static Connection connection = null;
    private static Boolean connectionEstablished = Boolean.FALSE;

    private SQLiteConnection() {
        LogUtils.generateConstructorLog(SQLiteConnection.class);
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
            LogUtils.generateSQLExceptionLog(SQLiteConnection.class, e);
        }
    }

    public static Connection getInstance() {
        synchronized (SQLiteConnection.class) {
            connect();
        }
        return connection;
    }
}