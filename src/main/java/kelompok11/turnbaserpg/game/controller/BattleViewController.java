package kelompok11.turnbaserpg.game.controller;

import kelompok11.turnbaserpg.model.character.Enemy;
import kelompok11.turnbaserpg.model.character.InventorySlot;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.skill.Skill;

import java.util.List;

// Controller for BattlePanel — exposes stat snapshots so the view never reads model fields directly
public class BattleViewController {

    // Snapshot of player stats needed for the battle HUD
    public record PlayerBattleSnapshot(
        String name,
        String role,
        int level,
        int currentHp, int maxHp,
        int currentMana, int maxMana
    ) {}

    // Snapshot of enemy stats needed for the battle HUD
    public record EnemyBattleSnapshot(
        String name,
        int currentHp,
        int maxHp
    ) {}

    // Skill info needed to render skill sub-buttons
    public record SkillInfo(
        String name,
        int manaCost,
        int currentCooldown
    ) {}

    // Item info needed to render item sub-buttons
    public record ItemInfo(
        String name,
        int quantity
    ) {}

    private Player player;
    private Enemy  enemy;

    public void setContext(Player player, Enemy enemy) {
        this.player = player;
        this.enemy  = enemy;
    }

    // Returns current player stat snapshot for HUD refresh
    public PlayerBattleSnapshot getPlayerSnapshot() {
        if (player == null) return null;
        return new PlayerBattleSnapshot(
            player.getCharacterName(),
            player.getRole().getDisplayName(),
            player.getLevel(),
            player.getStats().getCurrentHP(),
            player.getStats().getMaxHP(),
            player.getStats().getCurrentMana(),
            player.getStats().getBaseMana()
        );
    }

    // Returns current enemy stat snapshot for HUD refresh
    public EnemyBattleSnapshot getEnemySnapshot() {
        if (enemy == null) return null;
        return new EnemyBattleSnapshot(
            enemy.getCharacterName(),
            enemy.getStats().getCurrentHP(),
            enemy.getStats().getMaxHP()
        );
    }

    // Returns skill list info for building skill sub-panel
    public List<SkillInfo> getSkillInfoList() {
        if (player == null) return List.of();
        return player.getUnlockedSkills().stream()
            .map(sk -> new SkillInfo(sk.getName(), sk.getManaCost(), sk.getCurrentCooldown()))
            .toList();
    }

    // Returns item list info for building item sub-panel
    public List<ItemInfo> getItemInfoList() {
        if (player == null) return List.of();
        return player.getInventory().getSlots().stream()
            .map(slot -> new ItemInfo(slot.getItem().getName(), slot.getQuantity()))
            .toList();
    }

    // Returns current mana for skill availability check
    public int getPlayerCurrentMana() {
        if (player == null) return 0;
        return player.getStats().getCurrentMana();
    }

    // Returns skill mana cost by index for availability check
    public int getSkillManaCost(int index) {
        if (player == null) return Integer.MAX_VALUE;
        List<Skill> skills = player.getUnlockedSkills();
        if (index < 0 || index >= skills.size()) return Integer.MAX_VALUE;
        return skills.get(index).getManaCost();
    }

    // Returns skill cooldown by index for availability check
    public int getSkillCooldown(int index) {
        if (player == null) return 0;
        List<Skill> skills = player.getUnlockedSkills();
        if (index < 0 || index >= skills.size()) return 0;
        return skills.get(index).getCurrentCooldown();
    }
}
