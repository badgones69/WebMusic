package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    private static volatile Connection connection = null;

    public static void connect() {

        try {
            // DATABASE FILE URL
            String url = "jdbc:sqlite:C:/workspace/WebMusicTest.db";

            // DATABASE CONNECTION CREATION
            connection = DriverManager.getConnection(url);

            System.out.print("");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Connection getInstance() {
        synchronized (SQLiteConnection.class) {
            connect();
        }
        return connection;
    }
}
