/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.character;

import kelompok11.turnbaserpg.model.character.Character;
import java.util.concurrent.ThreadLocalRandom;
import kelompok11.turnbaserpg.utils.GameConstants;

/**
 * Represents an AI-controlled enemy character. Has a basic attack and a skill
 * attack used every 3rd turn.
 */
public class Enemy extends Character {

    public Enemy(String characterName) {
        super(characterName, new Stats(
                GameConstants.BASE_ENEMY_HP,
                GameConstants.BASE_ENEMY_ATK,
                GameConstants.BASE_ENEMY_DEF,
                0, // Magic
                0 // Mana
        ));
    }

    /**
     * Used every 3rd enemy turn — deals scaled damage based on attack stat.
     */
    public void skillAttack(Character target) {
        double multiplier = ThreadLocalRandom.current().nextDouble(
                GameConstants.ENEMY_SKILL_MIN_MULTIPLIER,
                GameConstants.ENEMY_SKILL_MAX_MULTIPLIER
        );
        int damage = (int) (stats.getTotalAttack() * multiplier);
        target.takeDamage(damage);
    }
}
