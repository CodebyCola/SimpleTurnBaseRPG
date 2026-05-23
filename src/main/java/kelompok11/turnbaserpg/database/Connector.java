package kelompok11.turnbaserpg.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import kelompok11.turnbaserpg.utils.GameLogger;

public class Connector {

    public static Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/turn_based_rpg";
            String user = "root";
            String password = "";
            Connection conn = DriverManager.getConnection(url, user, password);
            GameLogger.info("Database connected");
            return conn;
        } catch (SQLException e) {
            GameLogger.error("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}
