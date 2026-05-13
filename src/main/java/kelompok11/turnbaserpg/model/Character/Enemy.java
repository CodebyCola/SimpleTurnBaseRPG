/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.Character;

import kelompok11.turnbaserpg.model.Character.Character;
import java.util.concurrent.ThreadLocalRandom;
import kelompok11.turnbaserpg.utils.GameConstants;

/**
 *
 * @author Pongo
 */
public class Enemy extends Character {

    // Default stats for enemy
    public Enemy(String characterName) {
        super(characterName, new Stats(
                GameConstants.BASE_ENEMY_HP,
                GameConstants.BASE_ENEMY_ATK,
                GameConstants.BASE_ENEMY_DEF,
                0, // Magic
                0 // Mana
        ));
    }

   
// every turn mod 3 == 0 (every 3 turn)    
    public int skillAttack() {

        double multiplier = ThreadLocalRandom.current().nextDouble(
                GameConstants.ENEMY_SKILL_MIN_MULTIPLIER,
                GameConstants.ENEMY_SKILL_MAX_MULTIPLIER);

        int damage = (int) (stats.getBaseAttack() * multiplier);

//        System.out.println("Critical multiplier: " + multiplier);
        return damage;
    }

}
