/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model;

/**
 *
 * @author Pongo
 */
public class Enemy extends Character {

    public Enemy(String characterName) {
        super(characterName);
    }

    @Override
    public int attack() {
        return getAttackPower();
    }

    @Override
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

}
