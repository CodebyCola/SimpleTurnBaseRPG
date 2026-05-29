package kelompok11.turnbaserpg.game.controller;

import java.util.List;
import kelompok11.turnbaserpg.model.character.InventorySlot;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.skill.Skill;
import kelompok11.turnbaserpg.utils.GameLogger;

public class MainMenuController {

    private Player player;

    public enum DungeonEntryResult {
        OK,
        PLAYER_DEAD,
        OVER_LEVELED
    }

    public record PlayerSnapshot(
            String name,
            String role,
            int level,
            int currentHp, int maxHp,
            int currentMana, int maxMana,
            int currentExp, int maxExp,
            int gold,
            int floor,
            int highestClearedFloor,
            boolean isDead) {

    }

    public MainMenuController(Player player) {
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayerSnapshot getPlayerSnapshot() {
        if (player == null) {
            return null;
        }
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
                player.getCurrentFloor(),
                player.getHighestClearedFloor(),
                player.isDead()
        );
    }

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
            int floor,
            int highestClearedFloor,
            boolean isDead) {

    }

    public StatsSnapshot getCharacterStats() {
        if (player == null) {
            return null;
        }
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
                player.getCurrentFloor(),
                player.getHighestClearedFloor(),
                player.isDead()
        );
    }

    public List<InventorySlot> getInventorySlots() {
        if (player == null) {
            return List.of();
        }
        return player.getInventory().getSlots();
    }

    public List<Skill> getUnlockedSkills() {
        if (player == null) {
            return List.of();
        }
        return player.getUnlockedSkills();
    }

    /**
     * True if the player has HP <= 0 and cannot enter the dungeon.
     */
    public boolean isPlayerDead() {
        return player != null && player.isDead();
    }

    /**
     * Use an item from inventory by slot index. Returns a result message for
     * the UI to display.
     */
    public String useItem(int slotIndex) {
        if (player == null) {
            return "No player loaded.";
        }
        if (player.getInventory().isEmpty()) {
            return "Inventory is empty.";
        }
        InventorySlot slot = player.getInventory().getSlot(slotIndex);
        if (slot == null) {
            return "Invalid item slot.";
        }

        String itemName = slot.getItem().getName();
        int hpBefore = player.getStats().getCurrentHP();
        int manaBefore = player.getStats().getCurrentMana();

        player.getInventory().useItem(slotIndex, player);

        int hpAfter = player.getStats().getCurrentHP();
        int manaAfter = player.getStats().getCurrentMana();

        String result = "Used " + itemName + ".";
        if (hpAfter > hpBefore) {
            result += " HP: +" + (hpAfter - hpBefore);
        }
        if (manaAfter > manaBefore) {
            result += " Mana: +" + (manaAfter - manaBefore);
        }

        GameLogger.info("[ITEM USE] " + player.getCharacterName() + " used " + itemName
                + " | HP: " + hpAfter + "/" + player.getStats().getMaxHP());
        return result;
    }

    public boolean canReplayFloor(int floor) {
        return player != null && floor >= 1 && floor <= player.getHighestClearedFloor();
    }

    public DungeonEntryResult canEnterReplay(int chosenFloor) {
        if (player == null) {
            return DungeonEntryResult.PLAYER_DEAD;
        }
        if (player.isDead()) {
            return DungeonEntryResult.PLAYER_DEAD;
        }
        if ((player.getLevel() - chosenFloor) > 0) {
            return DungeonEntryResult.OVER_LEVELED;
        }
        return DungeonEntryResult.OK;
    }

    public boolean setReplayFloor(int floor) {
        if (!canReplayFloor(floor)) {
            return false;
        }
        if (canEnterReplay(floor) != DungeonEntryResult.OK) {
            return false;
        }

        player.setPendingReplayFloor(floor);
        GameLogger.info(player.getCharacterName() + " replaying from floor " + floor);
        return true;
    }

    public boolean hasPlayer() {
        return player != null;
    }

    public int getPlayerLevel() {
        return player.getLevel();
    }

    public DungeonEntryResult canEnterDungeon() {
        if (player == null) {
            return DungeonEntryResult.PLAYER_DEAD;
        }
        if (player.isDead()) {
            return DungeonEntryResult.PLAYER_DEAD;
        }
        return DungeonEntryResult.OK;
    }
}
