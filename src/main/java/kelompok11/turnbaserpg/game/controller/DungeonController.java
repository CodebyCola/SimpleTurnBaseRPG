package kelompok11.turnbaserpg.game.controller;

import java.util.List;

import kelompok11.turnbaserpg.model.enums.BattleResult;
import kelompok11.turnbaserpg.model.enums.Difficulty;
import kelompok11.turnbaserpg.game.services.BattleEvent;
import kelompok11.turnbaserpg.game.services.DungeonEvent;
import kelompok11.turnbaserpg.game.services.DungeonService;
import kelompok11.turnbaserpg.model.character.Enemy;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.utils.GameLogger;

public class DungeonController {

    @FunctionalInterface
    public interface DungeonEventListener {

        void onDungeonEvents(List<DungeonEvent> events);
    }

    @FunctionalInterface
    public interface BattleEventListener {

        void onBattleEvents(List<BattleEvent> events);
    }

    @FunctionalInterface
    public interface BattleInputProvider {

        void requestAction(BattleController battle);
    }

 
    @FunctionalInterface
    public interface AdvancePrompt {

        void promptAdvance(int nextFloor);
    }

    private final DungeonService dungeonService;
    private final DungeonEventListener dungeonListener;
    private final BattleEventListener battleListener;
    private final BattleInputProvider battleInput;
    private final AdvancePrompt advancePrompt;

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

    
    public void enterDungeon() {
        dungeonService.initDungeon();
        dungeonRunning = true;
        GameLogger.info(dungeonService.getPlayer().getCharacterName() + " entering dungeon");
        runNextFloor();
    }

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

        
        dispatchDungeon(dungeonService.applySkillReward(floor));

        
        List<DungeonEvent> advanceEvents = dungeonService.advanceFloor();
        dispatchDungeon(advanceEvents);

        boolean dungeonComplete = advanceEvents.stream()
                .anyMatch(e -> e.getType() == DungeonEvent.Type.DUNGEON_COMPLETE);

        if (dungeonComplete || !dungeonService.hasMoreFloors()) {
            dungeonRunning = false;
            return;
        }

        
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

    
    private BattleResult runBattle(Enemy enemy) {
        Player player = dungeonService.getPlayer();

        BattleController battle = new BattleController(player, enemy, battleListener::onBattleEvents);
        battle.startBattle();

        while (!battle.isBattleOver()) {
            battleInput.requestAction(battle);
        }

        return battle.getResult();
    }

    
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
