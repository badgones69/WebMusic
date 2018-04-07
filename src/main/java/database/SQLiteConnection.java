package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    private static volatile Connection connection = null;

    public static void connect() {

        try {
            // URL to the database file
            String url = "jdbc:sqlite:C:/workspace/WebMusicTest.db";

            // Creation of a connection to the database
            connection = DriverManager.getConnection(url);

            System.out.println("Connection to your SQLite database established");
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
