package database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class TestSQLiteConnection {

    private OutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
    }

    @Test
    public void connect() {
        SQLiteConnection.connect();
        assertEquals("", outputStream.toString());
    }

    @After
    public void tearDown() throws Exception {
        PrintStream originalOut = System.out;
        System.setOut(originalOut);
    }
}