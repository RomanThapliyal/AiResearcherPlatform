package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ai_research_platform";
    private static final String USER = "root";
    private static final String PASS = "Rom42_2011";

    public static Connection getConnection() throws SQLException{
    try{
        Class.forName("com.mysql.cj.jdbc.Driver");
    }catch(ClassNotFoundException e){
        throw new SQLException("MySQL JDBC Driver not found",e);
    }
    return DriverManager.getConnection(URL,USER,PASS);
}
}
