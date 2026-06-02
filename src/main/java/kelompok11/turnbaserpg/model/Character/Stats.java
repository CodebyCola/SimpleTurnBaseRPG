package kelompok11.turnbaserpg.model.character;

import kelompok11.turnbaserpg.model.enums.BuffType;
import kelompok11.turnbaserpg.utils.GameConstants;

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

    public Stats(int maxHP, int attack, int defense, int magic, int mana) {
        this.maxHP = maxHP;
        this.currentHP = maxHP;
        this.baseAttack = attack;
        this.baseDefense = defense;
        this.baseMagic = magic;
        this.baseMana = mana;
        this.currentMana = this.baseMana;
    }

    public Stats() {}

    public int getMaxHP() { return maxHP; }
    public int getCurrentHP() { return currentHP; }
    public void setCurrentHP(int currentHP) { this.currentHP = currentHP; }
    public void setMaxHP(int maxHP) { this.maxHP = maxHP; }

    public int getBaseAttack() { return baseAttack; }
    public void setBaseAttack(int baseAttack) { this.baseAttack = baseAttack; }

    public int getBaseDefense() { return baseDefense; }
    public void setBaseDefense(int baseDefense) { this.baseDefense = baseDefense; }

    public int getBaseMagic() { return baseMagic; }
    public void setBaseMagic(int baseMagic) { this.baseMagic = baseMagic; }

    public int getBaseMana() { return baseMana; }
    public void setBaseMana(int baseMana) { this.baseMana = baseMana; }

    public int getCurrentMana() { return currentMana; }
    public void setCurrentMana(int currentMana) { this.currentMana = currentMana; }

    public int getTotalAttack() { return baseAttack + bonusAttack; }
    public int getTotalDefense() { return baseDefense + bonusDefense; }
    public int getTotalMaxHP() { return maxHP + bonusHP; }
    public int getTotalMagic() { return baseMagic + bonusMagic; }
    public int getTotalMana() { return baseMana + bonusMana; }

    public void increaseBaseAttack(int amount) { baseAttack += amount; }
    public void increaseBaseDefense(int amount) { baseDefense += amount; }
    public void increaseBaseHP(int amount) { maxHP += amount; }
    public void increaseBaseMagic(int amount) { baseMagic += amount; }
    public void increaseBaseMana(int amount) { baseMana += amount; }

    public void increaseCurrentMana(int amount) {
        currentMana = Math.min(currentMana + amount, baseMana);
    }

    public void decreaseCurrentMana(int manaCost) {
        currentMana = Math.max(currentMana - manaCost, 0);
    }

    public int takeDamage(int damage) {
        int finalDamage = damage * 100 / (100 + getTotalDefense());
        if (finalDamage < GameConstants.MIN_DAMAGE) {
            finalDamage = GameConstants.MIN_DAMAGE;
        }
        currentHP = Math.max(currentHP - finalDamage, 0);
        return finalDamage;
    }

    public void heal(int amount) {
        if (amount < 0) amount = 0;
        currentHP = Math.min(currentHP + amount, getTotalMaxHP());
    }

    public void applyBuff(int effectValue, BuffType type) {
        switch (type) {
            case ATTACK  -> bonusAttack  += effectValue;
            case DEFENSE -> bonusDefense += effectValue;
            case MAGIC   -> bonusMagic   += effectValue;
            case MANA    -> bonusMana    += effectValue;
        }
    }

    public void removeBuff(int effectValue, BuffType type) {
        switch (type) {
            case ATTACK  -> bonusAttack  -= effectValue;
            case DEFENSE -> bonusDefense -= effectValue;
            case MAGIC   -> bonusMagic   -= effectValue;
            case MANA    -> bonusMana    -= effectValue;
        }
    }

    public void boostStats() {
        maxHP      += GameConstants.LEVEL_UP_HP_BONUS;
        baseAttack += GameConstants.LEVEL_UP_ATK_BONUS;
        baseDefense+= GameConstants.LEVEL_UP_DEF_BONUS;
        baseMagic  += GameConstants.LEVEL_UP_MAGIC_BONUS;
        baseMana   += GameConstants.LEVEL_UP_MANA_BONUS;
        currentHP = maxHP;
    }

    public void increaseAttackBonus(int amount)  { bonusAttack  += amount; }
    public void decreaseAttackBonus(int amount)  { bonusAttack  -= amount; }
    public void increaseHPBonus(int amount)      { bonusHP      += amount; }
    public void decreaseHPBonus(int amount)      { bonusHP      -= amount; }
    public void increaseDefenseBonus(int amount) { bonusDefense += amount; }
    public void decreaseDefenseBonus(int amount) { bonusDefense -= amount; }
    public void increaseMagicBonus(int amount)   { bonusMagic   += amount; }
    public void decreaseMagicBonus(int amount)   { bonusMagic   -= amount; }
    public void increaseManaBonus(int amount)    { bonusMana    += amount; }
    public void decreaseManaBonus(int amount)    { bonusMana    -= amount; }
}
