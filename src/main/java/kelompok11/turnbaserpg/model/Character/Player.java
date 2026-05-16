/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.character;

import kelompok11.turnbaserpg.model.character.Inventory;
import kelompok11.turnbaserpg.model.character.Character;
import java.util.ArrayList;
import kelompok11.turnbaserpg.model.buff.Buff;
import kelompok11.turnbaserpg.enums.*;
import kelompok11.turnbaserpg.model.skill.BasicHeal;
import kelompok11.turnbaserpg.model.skill.Skill;
import kelompok11.turnbaserpg.utils.GameConstants;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 *
 * @author Pongo
 */
public class Player extends Character {

    private Role role; // Enums role untuk simpan role player
    private int level; // Level player saat ini
    private int currentExp; // Exp player saat ini
    private int maxExp; // Batas exp yang diperlukan untuk naik level
    private int currentFloor; // Lantai tempat player berada saat ini
    private int totalGold;
    private Inventory inventory;
    private ArrayList<Skill> unlockedSkills;

    public Player(String characterName, Role role) {

        super(characterName, createStatsByRole(role));

        this.role = role;
        level = GameConstants.DEFAULT_LEVEL;
        currentExp = 0;
        currentFloor = 0;
        totalGold = GameConstants.INITIAL_GOLD;
        inventory = new Inventory();
        maxExp = GameConstants.INITIAL_EXP_REQUIRED;
        unlockedSkills = new ArrayList<>();
        this.unlockSkill(new BasicHeal());

    }

    public void getPlayerDetail() {
        System.out.println("Character name : " + characterName);
        System.out.println("Role : " + role.getDisplayName());
        System.out.println("Level : " + level);
        System.out.println("Exp : " + currentExp + " / " + maxExp);
        System.out.println("Gold Currency : " + totalGold);

    }

    public Inventory getInventory() {
        return inventory;
    }
    
    

    public Role getRole() {
        return role;
    }

    public void getStatsDetail() {
        stats.viewDetailStats();
    }

    public int getLevel() {
        return level;
    }

    public int getCurrentExp() {
        return currentExp;
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

    public void setCurrentFloor(int currentFloor) {
        if (currentFloor > this.currentFloor) {
            this.currentFloor = currentFloor;
        }
    }

    public int getTotalGold() {
        return totalGold;
    }

    // Fungsi inisialisasi stats berdasarkan role
    private static Stats createStatsByRole(Role role) {

        switch (role) {

            case WARRIOR:

                return new Stats(
                        GameConstants.WarriorStats.INITIAL_HP,
                        GameConstants.WarriorStats.INITIAL_ATK,
                        GameConstants.WarriorStats.INITIAL_DEF,
                        GameConstants.WarriorStats.INITIAL_MAGIC,
                        GameConstants.WarriorStats.INITIAL_MANA
                );

            case MAGE:

                return new Stats(
                        GameConstants.MageStats.INITIAL_HP,
                        GameConstants.MageStats.INITIAL_ATK,
                        GameConstants.MageStats.INITIAL_DEF,
                        GameConstants.MageStats.INITIAL_MAGIC,
                        GameConstants.MageStats.INITIAL_MANA);

            case ARCHER:

                return new Stats(
                        GameConstants.ArcherStats.INITIAL_HP,
                        GameConstants.ArcherStats.INITIAL_ATK,
                        GameConstants.ArcherStats.INITIAL_DEF,
                        GameConstants.ArcherStats.INITIAL_MAGIC,
                        GameConstants.ArcherStats.INITIAL_MANA);

            default:
                throw new IllegalArgumentException("Invalid role");
        }
    }

//    Method untuk progress character
    public void levelUp() {
        if (this.level >= GameConstants.MAX_LEVEL) {
            System.out.println("️Already at max level!");
            return;
        }

        level += 1;
        GameLogger.info(characterName + " level up");
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
        GameLogger.info(characterName + " gain " + exp + " exp");
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
        } else {
            return false;
        }
    }

//    Method game system
    public boolean unlockSkill(Skill skill) {
        for (Skill unlockedSkill : unlockedSkills) {

            if (unlockedSkill.getName().equals(skill.getName())) {
                return false;
            }
        }

        unlockedSkills.add(skill);
        return true;
    }

    public ArrayList<Skill> getUnlockedSkills() {
        return unlockedSkills;
    }

    public int getTotalUnlockedSkills() {
        return unlockedSkills.size();
    }

//    Player battle system
    public void setDefend(boolean set) {
        if (set) {
            stats.increaseDefenseBonus(GameConstants.DEFEND_BONUS);

        } else {
            stats.decreaseDefenseBonus(GameConstants.DEFEND_BONUS);

        }
    }

    public void updateSkillCooldowns() {

        for (Skill skill : unlockedSkills) {
            skill.reduceCooldown();
        }
    }

}
