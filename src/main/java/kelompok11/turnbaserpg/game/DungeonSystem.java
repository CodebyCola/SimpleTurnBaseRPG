/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.game;

import kelompok11.turnbaserpg.model.Player;
import kelompok11.turnbaserpg.utils.GameConstants;

/**
 *
 * @author Pongo
 */
public class DungeonSystem {

    Player player;

    public void attackDungeon() {
        boolean Program = true;

        while (player.getCurrentFloor() <= GameConstants.MAX_FLOOR && Program) {
            System.out.println("Floor : " + player.getCurrentFloor());
            int maxWave = 5;
            int wave = 1;

            while (wave <= maxWave) {
                
            }
        }
    }
}
