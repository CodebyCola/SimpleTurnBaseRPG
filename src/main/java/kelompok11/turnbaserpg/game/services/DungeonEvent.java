package kelompok11.turnbaserpg.game.services;

/**
 * Carries a single log message produced during dungeon progression.
 * Controllers receive these events and route them to the appropriate view.
 */
public class DungeonEvent {

    public enum Type {
        FLOOR_START,
        WAVE_START,
        BOSS_APPEAR,
        FLOOR_CLEARED,
        SKILL_UNLOCKED,
        DUNGEON_COMPLETE,
        PLAYER_DEFEATED,
        PLAYER_ESCAPED,
        BATTLE_RESULT
    }

    private final Type type;
    private final String message;

    public DungeonEvent(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    public Type getType() { return type; }
    public String getMessage() { return message; }

    public String toString() { return "[" + type + "] " + message; }
}
