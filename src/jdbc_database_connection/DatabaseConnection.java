package jdbc_database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// 데이터베이스 연결
public class DatabaseConnection {
    private Connection connection;

    public DatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/yourdatabase";
            String username = "yourusername";
            String password = "yourpassword";
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 데이터베이스 연결 종료
    public void closeConnection() {
        try {
            connection.close();
            System.out.println("Database connection closed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}