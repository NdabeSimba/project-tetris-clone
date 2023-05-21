package jdbc_database_connection;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        ScoreManager scoreManager = new ScoreManager(dbConnection.getConnection());

        String playerName = "John";
        int score = 1000;
        scoreManager.saveScore(playerName, score);

        dbConnection.closeConnection();
    }
}