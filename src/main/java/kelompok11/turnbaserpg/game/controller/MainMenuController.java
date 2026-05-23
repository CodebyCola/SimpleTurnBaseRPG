package kelompok11.turnbaserpg.game.controller;

import java.util.List;
import kelompok11.turnbaserpg.model.character.InventorySlot;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.skill.Skill;

// Controller for MainMenuPanel — view asks controller, controller reads from model
public class MainMenuController {

    // DTO so the view never touches Player directly
    public record PlayerSnapshot(
        String name,
        String role,
        int level,
        int currentHp, int maxHp,
        int currentMana, int maxMana,
        int currentExp, int maxExp,
        int gold,
        int floor
    ) {}

    private Player player;

    public MainMenuController(Player player) {
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    // Returns a snapshot of player stats for display
    public PlayerSnapshot getPlayerSnapshot() {
        if (player == null) return null;
        return new PlayerSnapshot(
            player.getCharacterName(),
            player.getRole().name(),
            player.getLevel(),
            player.getStats().getCurrentHP(),
            player.getStats().getMaxHP(),
            player.getStats().getCurrentMana(),
            player.getStats().getBaseMana(),
            player.getCurrentExp(),
            player.getMaxExp(),
            player.getTotalGold(),
            player.getCurrentFloor()
        );
    }

    // DTO for full character stats dialog
    public record StatsSnapshot(
        String name,
        String role,
        int level,
        int currentHp, int maxHp,
        int baseAttack, int totalAttack,
        int baseDefense, int totalDefense,
        int baseMagic, int totalMagic,
        int currentMana, int baseMana,
        int currentExp, int maxExp,
        int gold,
        int floor
    ) {}

    // Returns a full stats snapshot for the character stats dialog
    public StatsSnapshot getCharacterStats() {
        if (player == null) return null;
        return new StatsSnapshot(
            player.getCharacterName(),
            player.getRole().name(),
            player.getLevel(),
            player.getStats().getCurrentHP(),
            player.getStats().getMaxHP(),
            player.getStats().getBaseAttack(),
            player.getStats().getTotalAttack(),
            player.getStats().getBaseDefense(),
            player.getStats().getTotalDefense(),
            player.getStats().getBaseMagic(),
            player.getStats().getTotalMagic(),
            player.getStats().getCurrentMana(),
            player.getStats().getBaseMana(),
            player.getCurrentExp(),
            player.getMaxExp(),
            player.getTotalGold(),
            player.getCurrentFloor()
        );
    }

    // Returns inventory slots for display
    public List<InventorySlot> getInventorySlots() {
        if (player == null) return List.of();
        return player.getInventory().getSlots();
    }

    // Returns unlocked skills for display
    public List<Skill> getUnlockedSkills() {
        if (player == null) return List.of();
        return player.getUnlockedSkills();
    }

    public boolean hasPlayer() {
        return player != null;
    }
}
