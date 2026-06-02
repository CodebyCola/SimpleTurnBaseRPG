
package kelompok11.turnbaserpg.game.controller;

import kelompok11.turnbaserpg.database.dao.InventoryDAO;
import kelompok11.turnbaserpg.database.dao.PlayerDAO;
import kelompok11.turnbaserpg.database.dao.SkillsDAO;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.utils.GameLogger;

public class LoadManager {

    private final PlayerDAO playerDAO;
    private final InventoryDAO inventoryDAO;
    private final SkillsDAO skillsDAO;

    public LoadManager() {
        this.playerDAO = new PlayerDAO();
        this.inventoryDAO = new InventoryDAO();
        this.skillsDAO = new SkillsDAO();
    }

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
