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

    protected String characterName;
    protected Stats stats;

    public String getCharacterName() {
        return characterName;
    }

    public Stats getStats() {
        return stats;
    }

    public boolean isAlive() {
        return stats.getCurrentHP() > 0;
    }

    public Character(String characterName, Stats stats) {
        this.characterName = characterName;
        this.stats = stats;
    }

    public void takeDamage(int damage) {
        stats.takeDamage(damage);
    }

    public void heal(int amount) {
        stats.heal(amount);
    }
}
