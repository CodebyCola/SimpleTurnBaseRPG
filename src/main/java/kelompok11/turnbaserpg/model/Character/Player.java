/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.character;


import kelompok11.turnbaserpg.model.character.Character;
import java.util.ArrayList;
import kelompok11.turnbaserpg.model.buff.Buff;
import kelompok11.turnbaserpg.enums.*;
import kelompok11.turnbaserpg.model.skill.BasicHeal;
import kelompok11.turnbaserpg.model.skill.Skill;
import kelompok11.turnbaserpg.utils.GameConstants;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 * Represents a player character.
 * Extends Character with leveling, gold, floor tracking, and skill management.
 */
public class Player extends Character {

    private Role role;
    private int level;
    private int currentExp;
    private int maxExp;
    private int currentFloor;
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
        this.totalGold = GameConstants.INITIAL_GOLD;
        this.inventory = new Inventory();
        this.maxExp = GameConstants.INITIAL_EXP_REQUIRED;
        this.unlockedSkills = new ArrayList<>();
        // BasicHeal is the default starting skill
        this.unlockSkill(new BasicHeal());
    }

    public Player() {
        this.stats = new Stats();
        this.inventory = new Inventory();
        this.unlockedSkills = new ArrayList<>();
    }

    public void getPlayerDetail() {
        System.out.println("=== CHARACTER INFO ===");
        System.out.println("Name  : " + characterName);
        System.out.println("Role  : " + role.getDisplayName());
        System.out.println("Level : " + level);
        System.out.println("EXP   : " + currentExp + " / " + maxExp);
        System.out.println("Gold  : " + totalGold);
        System.out.println("Floor : " + currentFloor);
    }

    public void getStatsDetail() { stats.viewDetailStats(); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getCurrentExp() { return currentExp; }
    public void setCurrentExp(int currentExp) { this.currentExp = currentExp; }
    public int getMaxExp() { return maxExp; }
    public void setMaxExp(int maxExp) { this.maxExp = maxExp; }
    public int getCurrentFloor() { return currentFloor; }

    public void setCurrentFloor(int currentFloor) {
        if (currentFloor > this.currentFloor) {
            this.currentFloor = currentFloor;
        }
    }

    public int getTotalGold() { return totalGold; }
    public void setTotalGold(int totalGold) { this.totalGold = totalGold; }
    public Inventory getInventory() { return inventory; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }
    public void setStats(Stats stats) { this.stats = stats; }
    public ArrayList<Skill> getUnlockedSkills() { return unlockedSkills; }
    public void setUnlockedSkills(ArrayList<Skill> unlockedSkills) { this.unlockedSkills = unlockedSkills; }
    public int getTotalUnlockedSkills() { return unlockedSkills.size(); }

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
            System.out.println("Already at max level!");
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
        System.out.println("*** LEVEL UP! You are now Level " + level + " ***");
    }

    public void gainExp(int exp) {
        GameLogger.info(characterName + " gained " + exp + " EXP");
        currentExp += exp;
        System.out.println("+" + exp + " EXP  (" + currentExp + "/" + maxExp + ")");
        while (currentExp >= maxExp && level < GameConstants.MAX_LEVEL) {
            levelUp();
        }
    }

    public void gainGold(int amount) {
        if (amount > 0) {
            totalGold += amount;
            System.out.println("+" + amount + " Gold  (Total: " + totalGold + ")");
        }
    }

    public boolean spendGold(int amount) {
        if (totalGold >= amount) {
            totalGold -= amount;
            return true;
        }
        System.out.println("Not enough gold!");
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
}
