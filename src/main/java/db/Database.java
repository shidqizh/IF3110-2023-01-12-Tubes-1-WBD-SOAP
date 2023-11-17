package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection connector() {
        String username = "ok";
        String pass = "ok";
        String url = "jdbc:mysql://localhost:3307/soap_api_db";


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, username, pass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("[Database] Driver error", e);
        } catch (SQLException e) {
            throw new RuntimeException("[Database] SQL error", e);
        }
    }
}
