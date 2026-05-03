/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model;

/**
 *
 * @author Pongo
 */
public abstract class Character {
    
    private String characterName;
    private int characterHP;
    private int attackPower;

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public int getCharacterHP() {
        return characterHP;
    }

    public void setCharacterHP(int characterHP) {
        this.characterHP = characterHP;
    }
    
    public Character(String characterName) {
        this.characterName = characterName;
        this.characterHP = 100;
        this.attackPower = 10;
    }
    
    public abstract int attack();
    public abstract void takeDamage(int damage);
    public abstract boolean isAlive();
}
