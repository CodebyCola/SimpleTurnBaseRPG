package kelompok11.turnbaserpg.game.controller;

import kelompok11.turnbaserpg.model.enums.Role;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.utils.GameLogger;

public class GameController {

    public enum GameState {
        MAIN_MENU,
        IN_DUNGEON,
        GAME_OVER
    }

    @FunctionalInterface
    public interface StateListener {
        void onStateChanged(GameState newState);
    }

    private final SaveManager saveManager;
    private final LoadManager loadManager;
    private final StateListener stateListener;

    private Player currentPlayer;
    private GameState currentState = GameState.MAIN_MENU;

    public GameController(StateListener stateListener) {
        this.saveManager = new SaveManager();
        this.loadManager = new LoadManager();
        this.stateListener = stateListener;
    }

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
        if (saveManager.usernameExists(name)) {
            GameLogger.warning("GameController: " + name + " username already in database");
            return null;
        }
        Player player = new Player(name, role);
        player.setPassword(password);
        saveManager.save(player);
        currentPlayer = player;
        GameLogger.info("GameController: registered player " + name + " as " + role);
        return player;
    }

    public void saveGame() {
        if (currentPlayer == null) {
            GameLogger.warning("GameController: saveGame called with no active player");
            return;
        }
        saveManager.save(currentPlayer);
    }

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
        return new DungeonController(currentPlayer, dungeonListener, battleListener,
                battleInput, advancePrompt);
    }

    public void onDungeonSessionEnded() {
        saveGame();
        transitionTo(GameState.MAIN_MENU);
    }

    public GameState getCurrentState() { return currentState; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public boolean isLoggedIn() { return currentPlayer != null; }

    private void transitionTo(GameState newState) {
        currentState = newState;
        if (stateListener != null) {
            stateListener.onStateChanged(newState);
        }
    }
}
