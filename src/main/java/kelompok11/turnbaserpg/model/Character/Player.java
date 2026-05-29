/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.character;

import kelompok11.turnbaserpg.model.character.Character;
import java.util.ArrayList;
import kelompok11.turnbaserpg.enums.*;
import kelompok11.turnbaserpg.model.skill.BasicHeal;
import kelompok11.turnbaserpg.model.skill.Skill;
import kelompok11.turnbaserpg.utils.GameConstants;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 * Represents a player character. Extends Character with leveling, gold, floor
 * tracking, and skill management.
 */
public class Player extends Character {

    private Role role;
    private int level;
    private int currentExp;
    private int maxExp;
    private int currentFloor;
    private int highestClearedFloor; // highest floor the player has already completed
    private int pendingReplayFloor = -1;
    private int totalGold;
    private int id;
    private String password;
    private Inventory inventory;
    private ArrayList<Skill> unlockedSkills;

    public Player(String characterName, Role role) {
        super(characterName, createStatsByRole(role));
        this.role = role;
        this.level = GameConstants.DEFAULT_LEVEL;
        this.currentExp = 0;
        this.currentFloor = 0;
        this.highestClearedFloor = 0;
        this.totalGold = GameConstants.INITIAL_GOLD;
        this.inventory = new Inventory();
        this.maxExp = GameConstants.INITIAL_EXP_REQUIRED;
        this.unlockedSkills = new ArrayList<>();
        // BasicHeal is the default starting skill
        this.unlockSkill(new BasicHeal());
    }

    public Player() {
        this.role = Role.WARRIOR; // default; overwritten by setRole() during login
        this.stats = new Stats();
        this.inventory = new Inventory();
        this.unlockedSkills = new ArrayList<>();
    }

    public Player getPlayerDetail() { // ambil detail player untuk view
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurrentExp() {
        return currentExp;
    }

    public void setCurrentExp(int currentExp) {
        this.currentExp = currentExp;
    }

    public int getMaxExp() {
        return maxExp;
    }

    public void setMaxExp(int maxExp) {
        this.maxExp = maxExp;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Stats getStats() {
        return stats;
    }

    public void setCurrentFloor(int currentFloor) {
        if (currentFloor > this.currentFloor) {
            this.currentFloor = currentFloor;
        }
    }

    /**
     * Used by DAO / persistence layer to restore the exact saved floor value.
     * Bypasses the "only advance" guard so the loaded value is always applied.
     */
    public void loadCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getHighestClearedFloor() {
        return highestClearedFloor;
    }

    public void setHighestClearedFloor(int floor) {
        this.highestClearedFloor = floor;
    }
    
    public int getPendingReplayFloor() { return pendingReplayFloor; }
    public void setPendingReplayFloor(int floor) { this.pendingReplayFloor = floor; }
    public void clearPendingReplayFloor() { this.pendingReplayFloor = -1; }

    /**
     * Player is "dead" when their current HP has hit 0. They can still use
     * potions to revive themselves from the main menu.
     */
    public boolean isDead() {
        return stats.getCurrentHP() <= 0;
    }

    /**
     * Restores the player to full HP and mana (e.g. after using a potion from
     * the main menu or on explicit revive). Does NOT change floor.
     */
    public boolean revive() {
        if (isDead()) {
            GameLogger.warning("Cannot revive character");
            return false;
        } else {
            stats.setCurrentHP(stats.getMaxHP());
            stats.setCurrentMana(stats.getBaseMana());
            GameLogger.info(characterName + " revived to full HP/Mana");
            return true;
        }
    }

    public int getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(int totalGold) {
        this.totalGold = totalGold;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public ArrayList<Skill> getUnlockedSkills() {
        return unlockedSkills;
    }

    public void setUnlockedSkills(ArrayList<Skill> unlockedSkills) {
        this.unlockedSkills = unlockedSkills;
    }

    public int getTotalUnlockedSkills() {
        return unlockedSkills.size();
    }

    private static Stats createStatsByRole(Role role) {
        switch (role) {
            case WARRIOR:
                return new Stats(GameConstants.WarriorStats.INITIAL_HP, GameConstants.WarriorStats.INITIAL_ATK,
                        GameConstants.WarriorStats.INITIAL_DEF, GameConstants.WarriorStats.INITIAL_MAGIC, GameConstants.WarriorStats.INITIAL_MANA);
            case MAGE:
                return new Stats(GameConstants.MageStats.INITIAL_HP, GameConstants.MageStats.INITIAL_ATK,
                        GameConstants.MageStats.INITIAL_DEF, GameConstants.MageStats.INITIAL_MAGIC, GameConstants.MageStats.INITIAL_MANA);
            case ARCHER:
                return new Stats(GameConstants.ArcherStats.INITIAL_HP, GameConstants.ArcherStats.INITIAL_ATK,
                        GameConstants.ArcherStats.INITIAL_DEF, GameConstants.ArcherStats.INITIAL_MAGIC, GameConstants.ArcherStats.INITIAL_MANA);
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    public void levelUp() {
        if (this.level >= GameConstants.MAX_LEVEL) {
            return;
        }
        level++;
        GameLogger.info(characterName + " leveled up to " + level);
        currentExp -= maxExp;
        maxExp = (int) (maxExp * GameConstants.EXP_SCALING_MULTIPLIER);
        switch (role) {
            case WARRIOR:
                stats.increaseBaseHP(GameConstants.WarriorStats.LEVEL_UP_HP_BONUS);
                stats.increaseBaseDefense(GameConstants.WarriorStats.LEVEL_UP_DEF_BONUS);
                break;
            case MAGE:
                stats.increaseBaseMagic(GameConstants.MageStats.LEVEL_UP_MAGIC_BONUS);
                stats.increaseBaseMana(GameConstants.MageStats.LEVEL_UP_MANA_BONUS);
                break;
            case ARCHER:
                stats.increaseBaseAttack(GameConstants.ArcherStats.LEVEL_UP_ATK_BONUS);
                stats.increaseBaseDefense(GameConstants.ArcherStats.LEVEL_UP_DEF_BONUS);
                stats.increaseBaseHP(GameConstants.ArcherStats.LEVEL_UP_HP_BONUS);
                break;
        }
        stats.boostStats();
    }

    public void gainExp(int exp) {
        GameLogger.info(characterName + " gained " + exp + " EXP");
        currentExp += exp;
        while (currentExp >= maxExp && level < GameConstants.MAX_LEVEL) {
            levelUp();
        }
    }

    public void gainGold(int amount) {
        if (amount > 0) {
            totalGold += amount;
        }
    }

    public boolean spendGold(int amount) {
        if (totalGold >= amount) {
            totalGold -= amount;
            return true;
        }
        return false;
    }

    public boolean unlockSkill(Skill skill) {
        for (Skill owned : unlockedSkills) {
            if (owned.getName().equals(skill.getName())) {
                return false;
            }
        }
        unlockedSkills.add(skill);
        return true;
    }

    public void updateSkillCooldowns() {
        for (Skill skill : unlockedSkills) {
            skill.reduceCooldown();
        }
    }

    public void setDefend(boolean set) {
        if (set) {
            stats.increaseDefenseBonus(GameConstants.DEFEND_BONUS);
        } else {
            stats.decreaseDefenseBonus(GameConstants.DEFEND_BONUS);
        }
    }

    public int basicAttack(Character target) {

        switch (role) {
            case WARRIOR -> {
                int totalAttack = stats.getTotalAttack();
                return target.takeDamage(totalAttack);
            }
            case ARCHER -> {
                int totalAttack = stats.getTotalAttack();
                return target.takeDamage(totalAttack);
            }
            case MAGE -> {
                int totalMagic = stats.getTotalMagic();
                return target.takeDamage(totalMagic);
            }

            default -> {
                return 0;
            }
        }
    }
}
