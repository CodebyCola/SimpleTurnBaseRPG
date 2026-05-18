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
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 *
 * @author Pongo
 */
public class PlayerDAO {

    public void insert(Player p) {
        String query = "INSERT INTO players (name, password, role, level,"
                + " exp, gold, floor,  current_hp, current_mp, stat_hp, stat_atk, stat_def,"
                + "stat_magic, stat_mana) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Connector.connect()) {
            PreparedStatement ps = conn.prepareStatement(query,
                    PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, p.getCharacterName());
            ps.setString(2, p.getPassword());
            ps.setString(3, p.getRole().name());
            ps.setInt(4, p.getLevel());
            ps.setInt(5, p.getCurrentExp());
            ps.setInt(6, p.getTotalGold());
            ps.setInt(7, p.getCurrentFloor());
            ps.setInt(8, p.getStats().getCurrentHP());
            ps.setInt(9, p.getStats().getCurrentMana());
            ps.setInt(10, p.getStats().getMaxHP());
            ps.setInt(11, p.getStats().getBaseAttack());
            ps.setInt(12, p.getStats().getBaseDefense());
            ps.setInt(13, p.getStats().getBaseMagic());
            ps.setInt(14, p.getStats().getBaseMana());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                p.setId(rs.getInt(1));
            }

            rs.close();
            ps.close();
            GameLogger.info("Data player berhasil disimpan");
        } catch (SQLException e) {
            GameLogger.debug("Data player gagal disimpan");
            System.out.println(e.getMessage());
        }
    }

    public void update(Player p) {
        String query = "UPDATE players SET "
                + "level = ?,"
                + "exp = ?,"
                + "gold = ?,"
                + "floor = ?,"
                + "current_hp = ?,"
                + "current_mp = ?,"
                + "stat_hp = ?,"
                + "stat_atk = ?,"
                + "stat_def = ?,"
                + "stat_magic = ?,"
                + "stat_mana = ? "
                + "WHERE id = ?";

        try (Connection conn = Connector.connect()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, p.getLevel());
            ps.setInt(2, p.getCurrentExp());
            ps.setInt(3, p.getTotalGold());
            ps.setInt(4, p.getCurrentFloor());
            ps.setInt(5, p.getStats().getCurrentHP());
            ps.setInt(6, p.getStats().getCurrentMana());
            ps.setInt(7, p.getStats().getMaxHP());
            ps.setInt(8, p.getStats().getBaseAttack());
            ps.setInt(9, p.getStats().getBaseDefense());
            ps.setInt(10, p.getStats().getBaseMagic());
            ps.setInt(11, p.getStats().getBaseMana());

            ps.setInt(12, p.getId());

            ps.executeUpdate();
            ps.close();
            GameLogger.info("Data berhasil di update");
        } catch (SQLException e) {
            GameLogger.debug("Data gagal di update");
            System.out.println(e.getMessage());
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM players WHERE id = ?";

        try (Connection conn = Connector.connect()) {

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, id);

            ps.executeUpdate();
            ps.close();
            GameLogger.info("Akun berhasil dihapus");

        } catch (SQLException e) {

            GameLogger.error("Akun gagal dihapus");
            System.out.println(e.getMessage());

        }
    }

    public Player login(String name, String password) {
        String query = "Select * FROM players where name = ? AND password = ?";
        Player p = null;
        try (Connection conn = Connector.connect()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Role role = Role.valueOf(
                        rs.getString("role").toUpperCase()
                );
                p = new Player();
                p.setCharacterName(rs.getString("name"));
                p.setPassword(rs.getString("password"));
                p.setRole(role);

                p.setId(rs.getInt("id"));
                p.setLevel(rs.getInt("level"));
                p.setCurrentExp(rs.getInt("exp"));
                p.setTotalGold(rs.getInt("gold"));
                p.setCurrentFloor(rs.getInt("floor"));

                Stats stats = new Stats(
                        rs.getInt("stat_hp"),
                        rs.getInt("stat_atk"),
                        rs.getInt("stat_def"),
                        rs.getInt("stat_magic"),
                        rs.getInt("stat_mana")
                );

                stats.setCurrentHP(
                        rs.getInt("current_hp")
                );

                stats.setCurrentMana(
                        rs.getInt("current_mp")
                );

                p.setStats(stats);
                return p;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            GameLogger.debug("Failed to take data");
        }

        return null;
    }

}
