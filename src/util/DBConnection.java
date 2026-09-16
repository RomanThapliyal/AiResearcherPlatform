package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = 
    private static final String USER = "root";
    private static final String PASS = "Rom42_2011";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASS);
    }
}
