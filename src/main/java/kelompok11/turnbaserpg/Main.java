/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package kelompok11.turnbaserpg;

import javax.swing.SwingUtilities;
import kelompok11.turnbaserpg.view.GameFrame;

/**
 *
 * @author Pongo
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            new GameFrame();

        });
    }
}
