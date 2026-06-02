package kelompok11.turnbaserpg.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kelompok11.turnbaserpg.database.Connector;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.skill.*;
import kelompok11.turnbaserpg.utils.GameLogger;

public class SkillsDAO {

    public void save(Player player) {
        String deleteQuery = "DELETE FROM player_skills WHERE player_id = ?";
        String insertQuery = "INSERT INTO player_skills (player_id, skill_name) VALUES (?, ?)";

        try (Connection conn = Connector.connect()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deletePs = conn.prepareStatement(deleteQuery);
                 PreparedStatement insertPs = conn.prepareStatement(insertQuery)) {

                deletePs.setInt(1, player.getId());
                deletePs.executeUpdate();

                for (var skill : player.getUnlockedSkills()) {
                    insertPs.setInt(1, player.getId());
                    insertPs.setString(2, skill.getName());
                    insertPs.addBatch();
                }
                insertPs.executeBatch();
                conn.commit();
                GameLogger.info("Skills saved for player " + player.getCharacterName());

            } catch (SQLException e) {
                conn.rollback();
                GameLogger.error("Failed to save skills: " + e.getMessage());
            }
        } catch (SQLException e) {
            GameLogger.error("Failed to open connection: " + e.getMessage());
        }
    }

    public void read(Player player) {
        String query = "SELECT skill_name FROM player_skills WHERE player_id = ?";

        try (Connection conn = Connector.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, player.getId());

            try (ResultSet rs = ps.executeQuery()) {
                player.getUnlockedSkills().clear();
                while (rs.next()) {
                    Skill skill = createSkillByName(rs.getString("skill_name"));
                    if (skill != null) {
                        player.unlockSkill(skill);
                    }
                }
            }
            GameLogger.info("Skills loaded for player " + player.getCharacterName());
        } catch (SQLException e) {
            GameLogger.error("Failed to load skills: " + e.getMessage());
        }
    }

    private Skill createSkillByName(String name) {
        return switch (name) {
            case "Basic Heal"     -> new BasicHeal();
            case "Fire Ball"      -> new FireBall();
            case "Thunder Strike" -> new ThunderStrike();
            case "Ice Spear"      -> new IceSpear();
            case "Iron Wall"      -> new IronWall();
            case "Shadow Slash"   -> new ShadowSlash();
            case "Earth Crusher"  -> new EarthCrusher();
            case "Dragon Fury"    -> new DragonFury();
            case "Greater Heal"   -> new GreaterHeal();
            case "Life Drain"     -> new LifeDrain();
            case "Berserker Rage" -> new BerserkerRage();
            case "Arcane Power"   -> new ArcanePower();
            case "Guardian Aura"  -> new GuardianAura();
            case "Stone Body"     -> new StoneBody();
            default -> {
                GameLogger.warning("Unknown skill name: " + name);
                yield null;
            }
        };
    }
}
