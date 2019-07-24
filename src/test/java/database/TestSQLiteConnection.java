package database;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestSQLiteConnection {

    @Test
    public void connect() {
        SQLiteConnection.connect();
        assertEquals(Boolean.TRUE, SQLiteConnection.isConnectionEstablished());
    }
}