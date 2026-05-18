package kelompok11.turnbaserpg.game.controller;

import kelompok11.turnbaserpg.enums.Role;
import kelompok11.turnbaserpg.game.GameManager;
import kelompok11.turnbaserpg.game.LoadManager;
import kelompok11.turnbaserpg.game.SaveManager;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.utils.GameLogger;

// Main controller that controll the login, dungeon, battle flow
public class GameController {

    // State Change Notification
    public enum GameState {
        MAIN_MENU,
        IN_DUNGEON,
        GAME_OVER
    }

    @FunctionalInterface
    public interface StateListener {

        void onStateChanged(GameState newState);
    }

    private final GameManager gameManager;
    private final SaveManager saveManager;
    private final LoadManager loadManager;
    private final StateListener stateListener;

    private Player currentPlayer;
    private GameState currentState = GameState.MAIN_MENU;

    
    public GameController(StateListener stateListener) {
        this.gameManager = new GameManager();
        this.saveManager = new SaveManager();
        this.loadManager = new LoadManager();
        this.stateListener = stateListener;
    }

    
    // Authentication
    public Player login(String name, String password) {
        Player player = loadManager.load(name, password);
        if (player != null) {
            currentPlayer = player;
            GameLogger.info("GameController: login success for " + name);
        } else {
            GameLogger.warning("GameController: login failed for " + name);
        }
        return player;
    }

   
    public Player register(String name, String password, Role role) {
        Player player = new Player(name, role);
        player.setPassword(password);
        saveManager.save(player);
        currentPlayer = player;
        GameLogger.info("GameController: registered player " + name + " as " + role);
        return player;
    }

    // Save / Load
   
    public void saveGame() {
        if (currentPlayer == null) {
            GameLogger.warning("GameController: saveGame called with no active player");
            return;
        }
        saveManager.save(currentPlayer);
    }

   
    // Dungeon Flow   
    public DungeonController createDungeonController(
            DungeonController.DungeonEventListener dungeonListener,
            DungeonController.BattleEventListener battleListener,
            DungeonController.BattleInputProvider battleInput,
            DungeonController.AdvancePrompt advancePrompt) {

        if (currentPlayer == null) {
            GameLogger.warning("GameController: cannot create DungeonController — no active player");
            return null;
        }

        transitionTo(GameState.IN_DUNGEON);
        return new DungeonController(
                currentPlayer, dungeonListener, battleListener, battleInput, advancePrompt);
    }

    /**
     * Called by the view when the dungeon session ends (win, loss, or manual
     * exit). Saves the player and returns to the main menu state.
     */
    public void onDungeonSessionEnded() {
        saveGame();
        transitionTo(GameState.MAIN_MENU);
    }

    // -----------------------------------------------------------------------
    // State
    // -----------------------------------------------------------------------
    public GameState getCurrentState() {
        return currentState;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isLoggedIn() {
        return currentPlayer != null;
    }

    private void transitionTo(GameState newState) {
        currentState = newState;
        if (stateListener != null) {
            stateListener.onStateChanged(newState);
        }
    }
}
