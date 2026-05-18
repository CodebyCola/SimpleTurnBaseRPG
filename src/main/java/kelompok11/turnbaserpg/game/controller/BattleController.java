package kelompok11.turnbaserpg.game.controller;

import java.util.List;
import kelompok11.turnbaserpg.enums.BattleResult;
import kelompok11.turnbaserpg.game.services.BattleEvent;
import kelompok11.turnbaserpg.game.services.BattleService;
import kelompok11.turnbaserpg.model.character.Enemy;
import kelompok11.turnbaserpg.model.character.Player;

// Control battle flow
public class BattleController {

    /** Functional interface so any view can receive events without coupling. */
    @FunctionalInterface
    public interface EventListener {
        void onEvents(List<BattleEvent> events);
    }

    private final BattleService battleService;
    private final EventListener eventListener;
    private BattleResult result;

    public BattleController(Player player, Enemy enemy, EventListener eventListener) {
        this.battleService  = new BattleService(player, enemy);
        this.eventListener  = eventListener;
    }

    // Battle Entry Point

    public void startBattle() {
        dispatch(battleService.initBattle());
        dispatch(battleService.beginPlayerTurn());
    }

    // Player Action Handlers (called by the view)
    // Ex: btn.handleAction(1)
    public boolean handleAction(int action, int secondaryIndex) {
        List<BattleEvent> events = switch (action) {
            case 1 -> battleService.actionBasicAttack();
            case 2 -> battleService.actionDefend();
            case 3 -> battleService.actionUseSkill(secondaryIndex);
            case 4 -> battleService.actionUseItem(secondaryIndex);
            case 5 -> battleService.actionEscape();
            default -> {
                dispatch(List.of(new BattleEvent(BattleEvent.Type.ERROR,
                        "Invalid choice! Try again.")));
                yield null;
            }
        };

        if (events == null) {
            
            return false;
        }

        dispatch(events);

        if (!battleService.isBattleOngoing()) {
            endBattle();
            return true;
        }

        // Enemy turn
        dispatch(battleService.executeEnemyTurn());

        if (!battleService.isBattleOngoing()) {
            endBattle();
            return true;
        }

        // Next player turn
        dispatch(battleService.beginPlayerTurn());
        return true;
    }

    /**
     * Convenience overload for actions that don't need a secondary index.
     */
    public boolean handleAction(int action) {
        return handleAction(action, -1);
    }

    // -------------------------------------------------------------------------
    // Battle State
    // -------------------------------------------------------------------------

    public boolean isBattleOver()     { return result != null; }
    public BattleResult getResult()   { return result; }
    public BattleService getService() { return battleService; }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void endBattle() {
        result = battleService.resolveBattle();
        dispatch(List.of(new BattleEvent(BattleEvent.Type.BATTLE_END,
                "Battle ended: " + result)));
    }

    private void dispatch(List<BattleEvent> events) {
        if (events != null && !events.isEmpty()) {
            eventListener.onEvents(events);
        }
    }
}
