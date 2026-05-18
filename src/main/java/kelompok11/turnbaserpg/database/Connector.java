/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 *
 * @author Pongo
 */
public class Connector {

    public static Connection conn;

    public static Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/turn_based_rpg";
            String user = "root";
            String password = "";

            conn = DriverManager.getConnection(url, user, password);
            GameLogger.info("Database connected");

        } catch (SQLException e) {
            GameLogger.error("Database connection failed");
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
