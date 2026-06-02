package kelompok11.turnbaserpg.game.controller;

import kelompok11.turnbaserpg.database.dao.InventoryDAO;
import kelompok11.turnbaserpg.database.dao.PlayerDAO;
import kelompok11.turnbaserpg.database.dao.SkillsDAO;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.utils.GameLogger;

public class SaveManager {

    private final PlayerDAO playerDAO;
    private final InventoryDAO inventoryDAO;
    private final SkillsDAO skillsDAO;

    public SaveManager() {
        this.playerDAO = new PlayerDAO();
        this.inventoryDAO = new InventoryDAO();
        this.skillsDAO = new SkillsDAO();
    }

    public void save(Player player) {
        if (player.getId() == 0) {
            playerDAO.insert(player);
        } else {
            playerDAO.update(player);
        }
        inventoryDAO.save(player);
        skillsDAO.save(player);
        GameLogger.info("Game saved for player: " + player.getCharacterName());
    }

    public boolean usernameExists(String name) {
        return playerDAO.usernameExists(name);
    }
}
