package kelompok11.turnbaserpg.game.services;

/**
 * Carries a single log message produced during a battle action.
 * Controllers receive these events and route them to the appropriate view.
 */
public class BattleEvent {

    public enum Type {
        BATTLE_START,
        PLAYER_STATUS,
        ACTION_RESULT,
        ENEMY_TURN,
        SKILL_CAST,
        ITEM_USED,
        ESCAPE_SUCCESS,
        ESCAPE_FAILED,
        BATTLE_END,
        ERROR
    }

    private final Type type;
    private final String message;

    public BattleEvent(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    public Type getType() { return type; }
    public String getMessage() { return message; }

    @Override
    public String toString() { return "[" + type + "] " + message; }
}
