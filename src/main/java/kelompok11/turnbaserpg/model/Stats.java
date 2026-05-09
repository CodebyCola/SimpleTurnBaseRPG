/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model;

import kelompok11.turnbaserpg.enums.BuffType;
//import static kelompok11.turnbaserpg.enums.BuffType.ATTACK;
//import static kelompok11.turnbaserpg.enums.BuffType.DEFENSE;
//import static kelompok11.turnbaserpg.enums.BuffType.MAGIC;
//import static kelompok11.turnbaserpg.enums.BuffType.MANA;
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
    private int bonusAttack;

    private int baseDefense;
    private int bonusDefense;

    private int baseMagic;
    private int bonusMagic;

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

    public int getTotalAttack() {
        return baseAttack + bonusAttack;
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
        currentMana = this.baseMana;
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

    public void applyBuff(int effectValue, BuffType type) {
        switch (type) {

            case ATTACK:
                bonusAttack += effectValue;
                break;

            case DEFENSE:
                bonusDefense += effectValue;
                break;

            case MAGIC:
                bonusMagic += effectValue;
                break;

            case MANA:
                bonusMana += effectValue;
                break;
        }

    }

    public void removeBuff(int effectValue, BuffType type) {
        switch (type) {
            case ATTACK:
                bonusAttack -= effectValue;
                break;

            case DEFENSE:
                bonusDefense -= effectValue;

                break;

            case MAGIC:
                bonusMagic -= effectValue;

                break;

            case MANA:
                bonusMana -= effectValue;

                break;
        }
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

//    Stats detail
    public void viewDetailStats() {
        System.out.println("Hp : " + currentHP + " / " + maxHP);
        System.out.println("Attack : " + baseAttack);
        System.out.println("Defense : " + baseDefense);
        System.out.println("Magic : " + baseMagic);
        System.out.println("Mana : " + baseMana);
    }
}
