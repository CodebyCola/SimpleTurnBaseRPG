package kelompok11.turnbaserpg.view;

import javax.swing.JFrame;
import kelompok11.turnbaserpg.game.controller.GameController;

/**
 * Root application window.
 *
 * Receives the {@link GameController} at construction time. All view panels
 * added in the future will call controller methods for actions and register
 * listeners for state/event callbacks — never touching services or models
 * directly.
 */
public class GameFrame extends JFrame {

//    private final GameController controller;
    public GameFrame() {
//        this.controller = new GameController(state -> {
//            switch (state) {
//                case MAIN_MENU ->
        ////                    showMainMenuPanel();
//                case IN_DUNGEON ->
////                    showDungeonPanel();
//                case GAME_OVER ->
////                    showGameOverPanel();
//            }
//        });
        // View panels will be added here in future iterations
        
        setTitle("Turn Base RPG");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        showLoginPanel();
    }

//    public GameController getController() {
////        return controller;
//    }
}
