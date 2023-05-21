package jdbc_database_connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ScoreManager {
    private Connection connection;

    public ScoreManager(Connection connection) {
        this.connection = connection;
    }

    // 점수 저장
    public void saveScore(String playerName, int score) {
        try {
            String query = "INSERT INTO scores (player_name, score) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerName);
            statement.setInt(2, score);
            statement.executeUpdate();
            System.out.println("Score saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}