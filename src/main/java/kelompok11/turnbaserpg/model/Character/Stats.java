/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.character;

import kelompok11.turnbaserpg.enums.BuffType;
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

    // Modifier base stats method
    public void increaseBaseAttack(int amount) {
        baseAttack += amount;
    }

    public void increaseBaseDefense(int amount) {
        baseDefense += amount;
    }

    public void increaseBaseHP(int amount) {
        maxHP += amount;
    }

    public void increaseBaseMagic(int amount) {
        baseMagic += amount;
    }

    public void increaseBaseMana(int amount) {
        baseMana += amount;
    }

    public void increaseCurrentMana(int amount) {
        currentMana += amount;
        if (currentMana >= baseMana) {
            currentMana = baseMana;
        }
    }

    public void decreaseCurrentMana(int manaCost) {
        currentMana -= manaCost;

        if (currentMana < 0) {
            currentMana = 0;
        }
    }

    // Getter & Setter method
    public int getMaxHP() {
        return maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }

    public void setBaseDefense(int baseDefense) {
        this.baseDefense = baseDefense;
    }

    public void setBaseMagic(int baseMagic) {
        this.baseMagic = baseMagic;
    }

    public void setBaseMana(int baseMana) {
        this.baseMana = baseMana;
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

    public int getCurrentMana() {
        return currentMana;
    }

    public int getTotalAttack() {
        return (this.baseAttack + this.bonusAttack);
    }

    public int getTotalDefense() {
        return (this.baseDefense + this.bonusDefense);
    }

    public int getTotalMaxHP() {
        return (this.maxHP + this.bonusHP);
    }

    public int getTotalMagic() {
        return (this.baseMagic + this.bonusMagic);
    }

    public int getTotalMana() {
        return (this.baseMana + this.bonusMana);
    }

    // constructor
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
        this.currentMana = this.baseMana;

        this.bonusHP = 0;
        this.bonusAttack = 0;
        this.bonusDefense = 0;
        this.bonusMagic = 0;
        this.bonusMana = 0;

    }

    public Stats() {

    }

//    Battle System
    public void takeDamage(int damage) {
        int finalDamage = damage * 100 / (100 + getTotalDefense());

        if (finalDamage < GameConstants.MIN_DAMAGE) {
            finalDamage = GameConstants.MIN_DAMAGE;
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

        if (newHP > getTotalMaxHP()) {
            newHP = getTotalMaxHP();
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

        baseAttack += GameConstants.LEVEL_UP_ATK_BONUS;
        baseDefense += GameConstants.LEVEL_UP_DEF_BONUS;
        baseMagic += GameConstants.LEVEL_UP_MAGIC_BONUS;
        baseMana += GameConstants.LEVEL_UP_MANA_BONUS;

        currentHP = maxHP; // Full hp reward for level up
    }

//    Method modify bonus stats
    public void increaseAttackBonus(int amount) {
        bonusAttack += amount;
    }

    public void decreaseAttackBonus(int amount) {
        bonusAttack -= amount;
    }

    public void increaseHPBonus(int amount) {
        bonusHP += amount;
    }

    public void decreaseHPBonus(int amount) {
        bonusHP -= amount;
    }

    public void increaseDefenseBonus(int amount) {
        bonusDefense += amount;
    }

    public void decreaseDefenseBonus(int amount) {
        bonusDefense -= amount;
    }

    public void increaseMagicBonus(int amount) {
        bonusMagic += amount;
    }

    public void decreaseMagicBonus(int amount) {
        bonusMagic -= amount;
    }

    public void increaseManaBonus(int amount) {
        bonusMana += amount;
    }

    public void decreaseManaBonus(int amount) {
        bonusMana -= amount;
    }

}
