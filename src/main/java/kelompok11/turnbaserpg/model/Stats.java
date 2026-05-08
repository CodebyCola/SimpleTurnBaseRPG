/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model;

import kelompok11.turnbaserpg.utils.GameConstants;

/**
 *
 * @author Pongo
 */
public class Stats {

    private int maxHP;
    private int bonusHP;
    private int currentHP;

    private int baseAttack;

    private int baseDefense;

    private int baseMagic;

    private int baseMana;
    private int bonusMana;
    private int currentMana;

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseMagic() {
        return baseMagic;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public int getBaseMana() {
        return baseMana;
    }

    public Stats(
            int maxHP,
            int attack,
            int defense,
            int magic,
            int mana
    ) {

        this.maxHP = maxHP;
        this.currentHP = maxHP;

        this.baseAttack = attack;
        this.baseDefense = defense;
        this.baseMagic = magic;
        this.baseMana = mana;
    }

//    Battle System
    public void takeDamage(int damage) {
        int finalDamage = damage * 100 / (100 + baseDefense);

        if (finalDamage < 1) {
            finalDamage = 1;
        }

        currentHP -= finalDamage;

        if (currentHP < 0) {
            currentHP = 0;
        }
    }

    public void heal(int amount) {
        if (amount < 0) {
            amount = 0;
        }

        int newHP
                = currentHP + amount;

        if (newHP > maxHP) {
            newHP = maxHP;
        }

        currentHP = newHP;
    }

//    Level up boost
    public void boostStats() {
        maxHP += GameConstants.LEVEL_UP_HP_BONUS;
        currentHP = maxHP;
        baseAttack += GameConstants.LEVEL_UP_ATK_BONUS;
        baseDefense += GameConstants.LEVEL_UP_DEF_BONUS;
        baseMagic += GameConstants.LEVEL_UP_MAGIC_BONUS;
        baseMana += GameConstants.LEVEL_UP_MANA_BONUS;
    }
}
