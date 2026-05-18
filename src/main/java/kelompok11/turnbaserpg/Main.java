/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package kelompok11.turnbaserpg;

import java.sql.Connection;
import kelompok11.turnbaserpg.database.Connector;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.game.*;

/**
 *
 * @author Pongo
 */
public class Main {

    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.startNewGame();
    }
}
