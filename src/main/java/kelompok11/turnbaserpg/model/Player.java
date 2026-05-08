/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model;

import kelompok11.turnbaserpg.enums.*;
import kelompok11.turnbaserpg.utils.GameConstants;

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

    public Player(String characterName, Role role) {

        super(characterName, createStatsByRole(role));

        this.role = role;
        level = GameConstants.DEFAULT_LEVEL;
        currentExp = 0;
        currentFloor = 0;
        totalGold = GameConstants.INITIAL_GOLD;
        inventory = new Inventory();
        maxExp = GameConstants.INITIAL_EXP_REQUIRED;
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
        this.currentFloor = currentFloor;
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
        level += 1;
        currentExp -= maxExp;
        maxExp *= GameConstants.EXP_SCALING_MULTIPLIER;
        stats.boostStats();
    }

    public void gainExp(int exp) {
        currentExp += exp;
        while (currentExp >= maxExp) {
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

}
