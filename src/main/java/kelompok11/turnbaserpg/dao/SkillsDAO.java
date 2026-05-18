/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kelompok11.turnbaserpg.database.Connector;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.skill.*;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 *
 * @author Pongo
 */
public class SkillsDAO {

    public void save(Player player) {
        String deleteQuery = "DELETE FROM player_skills WHERE player_id = ?";
        String insertQuery = "INSERT INTO player_skills (player_id, skill_name) VALUES (?, ?)";

        try (Connection conn = Connector.connect()) {
            PreparedStatement deletePs = conn.prepareStatement(deleteQuery);
            deletePs.setInt(1, player.getId());
            deletePs.executeUpdate();

            PreparedStatement insertPs = conn.prepareStatement(insertQuery);
            for (var skill : player.getUnlockedSkills()) {
                insertPs.setInt(1, player.getId());
                insertPs.setString(2, skill.getName());
                insertPs.addBatch();
            }
            insertPs.executeBatch();
            GameLogger.info("Skills saved for player " + player.getCharacterName());
        } catch (SQLException e) {
            GameLogger.error("Failed to save skills: " + e.getMessage());
        }
    }

    public void read(Player player) {
        String query = "SELECT skill_name FROM player_skills WHERE player_id = ?";
        try (Connection conn = Connector.connect()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, player.getId());
            ResultSet rs = ps.executeQuery();

            player.getUnlockedSkills().clear();

            while (rs.next()) {
                String skillName = rs.getString("skill_name");
                Skill skill = createSkillByName(skillName);
                if (skill != null) {
                    player.unlockSkill(skill);
                }
            }
            GameLogger.info("Skills loaded for player " + player.getCharacterName());
        } catch (SQLException e) {
            GameLogger.error("Failed to load skills: " + e.getMessage());
        }
    }

    private Skill createSkillByName(String name) {
        switch (name) {
            case "Basic Heal":
                return new BasicHeal();
            case "Fire Ball":
                return new FireBall();
            case "Thunder Strike":
                return new ThunderStrike();
            case "Ice Spear":
                return new IceSpear();
            case "Iron Wall":
                return new IronWall();
            case "Shadow Slash":
                return new ShadowSlash();
            case "Earth Crusher":
                return new EarthCrusher();
            case "Dragon Fury":
                return new DragonFury();
            case "Greater Heal":
                return new GreaterHeal();
            case "Life Drain":
                return new LifeDrain();
            case "Berserker Rage":
                return new BerserkerRage();
            case "Arcane Power":
                return new ArcanePower();
            case "Guardian Aura":
                return new GuardianAura();
            case "Stone Body":
                return new StoneBody();
            case "Mana Buff":
                return null; // Not in skill pool, handled separately
            default:
                GameLogger.warning("Unknown skill name: " + name);
                return null;
        }
    }
}
