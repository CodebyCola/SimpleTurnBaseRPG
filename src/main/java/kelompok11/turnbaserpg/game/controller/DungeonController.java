package kelompok11.turnbaserpg.game.controller;

import java.util.List;

import kelompok11.turnbaserpg.enums.BattleResult;
import kelompok11.turnbaserpg.enums.Difficulty;
import kelompok11.turnbaserpg.game.services.BattleEvent;
import kelompok11.turnbaserpg.game.services.DungeonEvent;
import kelompok11.turnbaserpg.game.services.DungeonService;
import kelompok11.turnbaserpg.model.character.Enemy;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 * Controls dungeon floor progression.
 *
 * Sits between {@link DungeonService} (logic) and the view. The controller
 * drives the floor → wave → battle loop, delegates individual battles to a
 * {@link BattleController}, dispatches {@link DungeonEvent}s and
 * {@link BattleEvent}s to the view, and waits for the player's advance/retreat
 * decision between floors via {@link #advanceDecision(boolean)}.
 */
public class DungeonController {

    @FunctionalInterface
    public interface DungeonEventListener {

        void onDungeonEvents(List<DungeonEvent> events);
    }

    @FunctionalInterface
    public interface BattleEventListener {

        void onBattleEvents(List<BattleEvent> events);
    }

    /**
     * The view implements this to supply player battle actions during a wave.
     * The controller will call {@link #requestAction(BattleController)} each
     * time an action is needed, and the view calls
     * {@link BattleController#handleAction} before returning.
     */
    @FunctionalInterface
    public interface BattleInputProvider {

        void requestAction(BattleController battle);
    }

    /**
     * The view implements this to ask whether the player wants to advance to
     * the next floor. Call {@link DungeonController#advanceDecision(boolean)}
     * with the answer.
     */
    @FunctionalInterface
    public interface AdvancePrompt {

        void promptAdvance(int nextFloor);
    }

    // -----------------------------------------------------------------------
    private final DungeonService dungeonService;
    private final DungeonEventListener dungeonListener;
    private final BattleEventListener battleListener;
    private final BattleInputProvider battleInput;
    private final AdvancePrompt advancePrompt;

    /**
     * Set true once the view responds to {@link AdvancePrompt}.
     */
    private boolean waitingForAdvanceDecision = false;
    private boolean dungeonRunning = false;

    public DungeonController(
            Player player,
            DungeonEventListener dungeonListener,
            BattleEventListener battleListener,
            BattleInputProvider battleInput,
            AdvancePrompt advancePrompt) {

        this.dungeonService = new DungeonService(player);
        this.dungeonListener = dungeonListener;
        this.battleListener = battleListener;
        this.battleInput = battleInput;
        this.advancePrompt = advancePrompt;
    }

    // -----------------------------------------------------------------------
    // Dungeon Entry
    // -----------------------------------------------------------------------
    /**
     * Begins the dungeon run. Starts the first floor immediately. If the view
     * needs to prompt between floors, control returns temporarily; call
     * {@link #advanceDecision(boolean)} to continue.
     */
    public void enterDungeon() {
        dungeonService.initDungeon();
        dungeonRunning = true;
        GameLogger.info(dungeonService.getPlayer().getCharacterName() + " entering dungeon");
        runNextFloor();
    }

    /**
     * Called by the view in response to {@link AdvancePrompt}.
     *
     * @param advance true = proceed to next floor; false = exit dungeon
     */
    public void advanceDecision(boolean advance) {
        if (!waitingForAdvanceDecision) {
            return;
        }
        waitingForAdvanceDecision = false;

        if (!advance || !dungeonRunning) {
            dungeonRunning = false;
            return;
        }
        runNextFloor();
    }

    // -----------------------------------------------------------------------
    // Floor Loop (synchronous within a single call stack)
    // -----------------------------------------------------------------------
    private void runNextFloor() {
        if (!dungeonService.hasMoreFloors()) {
            dungeonRunning = false;
            return;
        }

        int floor = dungeonService.getCurrentFloor();
        boolean isBossFloor = dungeonService.isBossFloor(floor);
        Difficulty difficulty = dungeonService.determineDifficulty(floor);

        dispatchDungeon(dungeonService.buildFloorStartEvents(floor, isBossFloor, difficulty));

        boolean floorCleared = runFloor(floor, isBossFloor, difficulty);

        if (!floorCleared) {
            dungeonRunning = false;
            return;
        }

        // Reward
        dispatchDungeon(dungeonService.applySkillReward(floor));

        // Advance counter; check for dungeon completion
        List<DungeonEvent> advanceEvents = dungeonService.advanceFloor();
        dispatchDungeon(advanceEvents);

        boolean dungeonComplete = advanceEvents.stream()
                .anyMatch(e -> e.getType() == DungeonEvent.Type.DUNGEON_COMPLETE);

        if (dungeonComplete || !dungeonService.hasMoreFloors()) {
            dungeonRunning = false;
            return;
        }

        // Ask player whether to continue
        waitingForAdvanceDecision = true;
        advancePrompt.promptAdvance(dungeonService.getCurrentFloor());
    }

    private boolean runFloor(int floor, boolean isBossFloor, Difficulty difficulty) {
        int totalWaves = dungeonService.wavesForFloor(isBossFloor);

        for (int wave = 1; wave <= totalWaves; wave++) {
            Enemy enemy = isBossFloor
                    ? dungeonService.generateBossEnemy(difficulty)
                    : dungeonService.generateEnemy(difficulty);

            dungeonService.scaleEnemyStats(enemy, difficulty, isBossFloor);

            dispatchDungeon(dungeonService.buildWaveStartEvents(wave, totalWaves, enemy, isBossFloor));

            BattleResult battleResult = runBattle(enemy);

            DungeonService.FloorOutcome outcome = dungeonService.processBattleResult(battleResult);
            dispatchDungeon(outcome.getEvents());

            if (!outcome.isWaveCleared()) {
                return false;
            }
        }

        dispatchDungeon(List.of(new DungeonEvent(DungeonEvent.Type.FLOOR_CLEARED,
                "Floor " + floor + " cleared!")));
        return true;
    }

    // -----------------------------------------------------------------------
    // Single Battle Delegation
    // -----------------------------------------------------------------------
    private BattleResult runBattle(Enemy enemy) {
        Player player = dungeonService.getPlayer();

        BattleController battle = new BattleController(player, enemy, battleListener::onBattleEvents);
        battle.startBattle();

        while (!battle.isBattleOver()) {
            battleInput.requestAction(battle);
        }

        return battle.getResult();
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------
    private void dispatchDungeon(List<DungeonEvent> events) {
        if (events != null && !events.isEmpty()) {
            dungeonListener.onDungeonEvents(events);
        }
    }

    public boolean isDungeonRunning() {
        return dungeonRunning;
    }

    public DungeonService getService() {
        return dungeonService;
    }
}
