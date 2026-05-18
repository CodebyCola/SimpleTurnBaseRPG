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
 * Handles persisting all player data (stats, inventory, skills) to the
 * database.
 */
public class SaveManager {

    private final PlayerDAO playerDAO;
    private final InventoryDAO inventoryDAO;
    private final SkillsDAO skillsDAO;

    public SaveManager() {
        this.playerDAO = new PlayerDAO();
        this.inventoryDAO = new InventoryDAO();
        this.skillsDAO = new SkillsDAO();
    }

    /**
     * Saves all player data. If the player has no ID yet, inserts a new record;
     * otherwise updates the existing one.
     */
    public void save(Player player) {
        if (player.getId() == 0) {
            playerDAO.insert(player);
        } else {
            playerDAO.update(player);
        }
        inventoryDAO.save(player);
        skillsDAO.save(player);
        GameLogger.info("Game saved for player: " + player.getCharacterName());
        System.out.println("Game saved successfully!");
    }
}
