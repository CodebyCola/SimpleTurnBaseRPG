package kelompok11.turnbaserpg.game;

import kelompok11.turnbaserpg.model.character.Player;

/**
 * Holds shared application-level state (the active player session).
 *
 * GameManager is a simple data container. All navigation and flow control
 * has moved to {@link kelompok11.turnbaserpg.controller.GameController}.
 */
public class GameManager {

    private Player currentPlayer;

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public boolean hasActiveSession() {
        return currentPlayer != null;
    }

    public void clearSession() {
        this.currentPlayer = null;
    }
}
