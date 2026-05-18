/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.game;

import kelompok11.turnbaserpg.dao.InventoryDAO;
import kelompok11.turnbaserpg.dao.PlayerDAO;
import kelompok11.turnbaserpg.dao.SkillsDAO;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 * Handles loading a full player session from the database.
 */
public class LoadManager {

    private final PlayerDAO playerDAO;
    private final InventoryDAO inventoryDAO;
    private final SkillsDAO skillsDAO;

    public LoadManager() {
        this.playerDAO = new PlayerDAO();
        this.inventoryDAO = new InventoryDAO();
        this.skillsDAO = new SkillsDAO();
    }

    /**
     * Authenticates and loads a complete player from the database, including
     * their inventory and unlocked skills.
     *
     * @param name player username
     * @param password player password
     * @return fully loaded Player, or null if login failed
     */
    public Player load(String name, String password) {
        Player player = playerDAO.login(name, password);
        if (player == null) {
            GameLogger.warning("Login failed for: " + name);
            return null;
        }
        inventoryDAO.read(player);
        skillsDAO.read(player);
        GameLogger.info("Game loaded for player: " + player.getCharacterName());
        return player;
    }
}
