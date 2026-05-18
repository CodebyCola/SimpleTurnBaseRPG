/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import kelompok11.turnbaserpg.database.Connector;
import kelompok11.turnbaserpg.enums.Role;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.character.Stats;
import kelompok11.turnbaserpg.utils.GameConstants;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 *
 * @author Pongo
 */
public class PlayerDAO {

    public void insert(Player player) {
        String query = "INSERT INTO players "
                + "(name, password, role, level, exp, gold, floor, "
                + "current_hp, current_mp, stat_hp, stat_atk, stat_def, stat_magic, stat_mana) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Connector.connect(); PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, player.getCharacterName());
            ps.setString(2, player.getPassword());
            ps.setString(3, player.getRole().name());
            ps.setInt(4, player.getLevel());
            ps.setInt(5, player.getCurrentExp());
            ps.setInt(6, player.getTotalGold());
            ps.setInt(7, player.getCurrentFloor());
            ps.setInt(8, player.getStats().getCurrentHP());
            ps.setInt(9, player.getStats().getCurrentMana());
            ps.setInt(10, player.getStats().getMaxHP());
            ps.setInt(11, player.getStats().getBaseAttack());
            ps.setInt(12, player.getStats().getBaseDefense());
            ps.setInt(13, player.getStats().getBaseMagic());
            ps.setInt(14, player.getStats().getBaseMana());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    player.setId(rs.getInt(1));
                }
            }
            GameLogger.info("Player inserted: " + player.getCharacterName());
        } catch (SQLException e) {
            GameLogger.error("Failed to insert player: " + e.getMessage());
        }
    }

    public void update(Player player) {
        String query = "UPDATE players SET "
                + "level = ?, exp = ?, gold = ?, floor = ?, "
                + "current_hp = ?, current_mp = ?, "
                + "stat_hp = ?, stat_atk = ?, stat_def = ?, stat_magic = ?, stat_mana = ? "
                + "WHERE id = ?";

        try (Connection conn = Connector.connect(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, player.getLevel());
            ps.setInt(2, player.getCurrentExp());
            ps.setInt(3, player.getTotalGold());
            ps.setInt(4, player.getCurrentFloor());
            ps.setInt(5, player.getStats().getCurrentHP());
            ps.setInt(6, player.getStats().getCurrentMana());
            ps.setInt(7, player.getStats().getMaxHP());
            ps.setInt(8, player.getStats().getBaseAttack());
            ps.setInt(9, player.getStats().getBaseDefense());
            ps.setInt(10, player.getStats().getBaseMagic());
            ps.setInt(11, player.getStats().getBaseMana());
            ps.setInt(12, player.getId());

            ps.executeUpdate();
            GameLogger.info("Player updated: " + player.getCharacterName());
        } catch (SQLException e) {
            GameLogger.error("Failed to update player: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM players WHERE id = ?";
        try (Connection conn = Connector.connect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            GameLogger.info("Player deleted: id=" + id);
        } catch (SQLException e) {
            GameLogger.error("Failed to delete player: " + e.getMessage());
        }
    }

    /**
     * Authenticates a player and loads their base data. Inventory and skills
     * are loaded separately by their respective DAOs.
     *
     * @return Player if credentials match, null otherwise.
     */
    public Player login(String name, String password) {
        String query = "SELECT * FROM players WHERE name = ? AND password = ?";

        try (Connection conn = Connector.connect(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Role role = Role.valueOf(rs.getString("role").toUpperCase());

                    Stats stats = new Stats(
                            rs.getInt("stat_hp"),
                            rs.getInt("stat_atk"),
                            rs.getInt("stat_def"),
                            rs.getInt("stat_magic"),
                            rs.getInt("stat_mana")
                    );
                    stats.setCurrentHP(rs.getInt("current_hp"));
                    stats.setCurrentMana(rs.getInt("current_mp"));

                    Player player = new Player();
                    player.setId(rs.getInt("id"));
                    player.setCharacterName(rs.getString("name"));
                    player.setPassword(rs.getString("password"));
                    player.setRole(role);
                    player.setLevel(rs.getInt("level"));
                    player.setCurrentExp(rs.getInt("exp"));
                    player.setMaxExp((int) (GameConstants.INITIAL_EXP_REQUIRED * Math.pow(1.2, rs.getInt("level") - 1)));
                    player.setTotalGold(rs.getInt("gold"));
                    player.setCurrentFloor(rs.getInt("floor"));
                    player.setStats(stats);

                    GameLogger.info("Login successful: " + name);
                    return player;
                }
            }
        } catch (SQLException e) {
            GameLogger.error("Login error: " + e.getMessage());
        }

        return null;
    }

}
