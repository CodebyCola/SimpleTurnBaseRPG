package kelompok11.turnbaserpg;

import javax.swing.SwingUtilities;
import kelompok11.turnbaserpg.game.controller.GameController;
import kelompok11.turnbaserpg.view.GameFrame;

/**
 * Application entry point.
 *
 * Creates the top-level {@link GameController}, then hands it to the view
 * ({@link GameFrame}) so the view can wire itself to controller callbacks
 * without knowing any game logic.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            new GameFrame();
        });
    }
}
