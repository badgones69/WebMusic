package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    public static void connect() {

        Connection connection = null;

        try {
            // URL to the database file
            String url = "jdbc:sqlite:C:/workspace/WebMusicTest.db";

            // Creation of a connection to the database
            connection = DriverManager.getConnection(url);

            System.out.println("Connection to your SQLite database established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
