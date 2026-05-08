/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model;

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

    public void takeDamage(int damage) {
        stats.takeDamage(damage);
    }

}
