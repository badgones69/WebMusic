package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    private static final Logger LOG = LogManager.getLogger(SQLiteConnection.class);
    private static volatile Connection connection = null;

    private SQLiteConnection() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static void connect() {

        try {
            // DATABASE FILE URL
            String url = "jdbc:sqlite:C:/workspace/WebMusicTest.db";

            // DATABASE CONNECTION CREATION
            connection = DriverManager.getConnection(url);

            LOG.info("");
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
