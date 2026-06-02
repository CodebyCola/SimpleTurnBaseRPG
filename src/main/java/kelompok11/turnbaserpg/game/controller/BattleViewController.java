package kelompok11.turnbaserpg.game.controller;

import kelompok11.turnbaserpg.model.character.Enemy;
import kelompok11.turnbaserpg.model.character.InventorySlot;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.skill.Skill;

import java.util.List;

public class BattleViewController {

    
    public record PlayerBattleSnapshot(
        String name,
        String role,
        int level,
        int currentHp, int maxHp,
        int currentMana, int maxMana
    ) {}

    
    public record EnemyBattleSnapshot(
        String name,
        int currentHp,
        int maxHp
    ) {}

    
    public record SkillInfo(
        String name,
        int manaCost,
        int currentCooldown
    ) {}

    
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

    
    public EnemyBattleSnapshot getEnemySnapshot() {
        if (enemy == null) return null;
        return new EnemyBattleSnapshot(
            enemy.getCharacterName(),
            enemy.getStats().getCurrentHP(),
            enemy.getStats().getMaxHP()
        );
    }

    
    public List<SkillInfo> getSkillInfoList() {
        if (player == null) return List.of();
        return player.getUnlockedSkills().stream()
            .map(sk -> new SkillInfo(sk.getName(), sk.getManaCost(), sk.getCurrentCooldown()))
            .toList();
    }

    
    public List<ItemInfo> getItemInfoList() {
        if (player == null) return List.of();
        return player.getInventory().getSlots().stream()
            .map(slot -> new ItemInfo(slot.getItem().getName(), slot.getQuantity()))
            .toList();
    }

    
    public int getPlayerCurrentMana() {
        if (player == null) return 0;
        return player.getStats().getCurrentMana();
    }

    
    public int getSkillManaCost(int index) {
        if (player == null) return Integer.MAX_VALUE;
        List<Skill> skills = player.getUnlockedSkills();
        if (index < 0 || index >= skills.size()) return Integer.MAX_VALUE;
        return skills.get(index).getManaCost();
    }

    
    public int getSkillCooldown(int index) {
        if (player == null) return 0;
        List<Skill> skills = player.getUnlockedSkills();
        if (index < 0 || index >= skills.size()) return 0;
        return skills.get(index).getCurrentCooldown();
    }
}
