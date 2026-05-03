/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package kelompok11.turnbaserpg;

import kelompok11.turnbaserpg.model.Player;

/**
 *
 * @author Pongo
 */
public class TurnBaseRPG {

    public static void main(String[] args) {
        Player p = new Player("Hero");
        p.setAttackPower(20);

        System.out.println("HP awal: " + p.getCharacterHP());

        p.takeDamage(p.attack());

        System.out.println("HP setelah kena damage: " + p.getCharacterHP());
        System.out.println("Masih hidup? " + p.isAlive());
    }
}
