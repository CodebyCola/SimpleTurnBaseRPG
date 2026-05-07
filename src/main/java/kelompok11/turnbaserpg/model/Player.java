/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pongo
 */
public class Player extends Character {

    private int playerLevel;
    private float playerExp;
    private Inventory inventory;
    private List<Buff> buffs = new ArrayList<>();

    public Player(String characterName) {
        super(characterName);
    }

    @Override
    public int attack() {

        int base = getAttackPower();
        int bonus = 0;

        for (Buff buff : buffs) {
            bonus += buff.getAttackBonus();
        }

        return base + bonus;
    }

    public void takeDamage(int attackDamage) {
        int hpNow = getCharacterHP() - attackDamage;
        if (hpNow < 0) {
            hpNow = 0;
        }
        setCharacterHP(hpNow);
    }

    @Override
    public boolean isAlive() {
        return getCharacterHP() > 0;
    }

    public void addBuff(Buff buff) {
        buffs.add(buff);
    }

    public void updateBuffs() {
        buffs.removeIf(buff -> {
            buff.reduceDuration();
            return buff.isExpired();
        });
    }

}
