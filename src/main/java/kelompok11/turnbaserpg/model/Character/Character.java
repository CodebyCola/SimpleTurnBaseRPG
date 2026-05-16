/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.character;

import java.util.ArrayList;
import kelompok11.turnbaserpg.model.buff.Buff;
import kelompok11.turnbaserpg.utils.GameConstants;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 *
 * @author Pongo
 */
public abstract class Character {

    protected String characterName;
    protected Stats stats;
    private ArrayList<Buff> activeBuffs;

    public Character(String characterName, Stats stats) {
        this.characterName = characterName;
        this.stats = stats;
        activeBuffs = new ArrayList<>();
    }

    public String getCharacterName() {
        return characterName;
    }

    public Stats getStats() {
        return stats;
    }

    public boolean isAlive() {
        return stats.getCurrentHP() > 0;
    }

    public void takeDamage(int damage) {
        stats.takeDamage(damage);
    }

    public void heal(int amount) {
        stats.heal(amount);
    }

    public void basicAttack(Character target) {
        int totalAttack = stats.getTotalAttack();

        target.takeDamage(totalAttack);
        System.out.println(characterName + " hit " + target.getCharacterName());
        System.out.println(target.getCharacterName() + " Take " + totalAttack + " damage");
    }

    // Buff Systems
    public void addBuff(Buff buff) {
        if (activeBuffs.size() >= GameConstants.MAX_ACTIVE_BUFFS) {
            return;
        }

        buff.use(this);
        activeBuffs.add(buff);
        GameLogger.info(characterName + " gain " + buff + " buff");
    }

    public ArrayList<Buff> getActiveBuffs() {
        return activeBuffs;
    }

    public void updateBuffs() {

        for (int i = activeBuffs.size() - 1; i >= 0; i--) {

            Buff buff = activeBuffs.get(i);

            buff.decreaseDuration();

            if (buff.isExpired()) {

                buff.remove(this);

                System.out.println(buff.getName()
                        + " has expired!");

                activeBuffs.remove(i);
                GameLogger.info(buff + " buff had been removed" );
            }
        }
    }

}
