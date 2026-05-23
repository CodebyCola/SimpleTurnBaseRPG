package kelompok11.turnbaserpg.game.controller;

import java.util.List;

import kelompok11.turnbaserpg.enums.BattleResult;
import kelompok11.turnbaserpg.enums.Difficulty;
import kelompok11.turnbaserpg.game.services.DungeonEvent;
import kelompok11.turnbaserpg.game.services.DungeonService;
import kelompok11.turnbaserpg.model.character.Enemy;
import kelompok11.turnbaserpg.model.character.Player;

// Controller for DungeonPanel's async dungeon loop.
// DungeonPanel delegates all service calls here — the view never touches DungeonService directly.
public class DungeonSessionController {

    // Snapshot of player stats for the status bar
    public record PlayerStatusSnapshot(
        int currentHp, int maxHp,
        int currentMana, int maxMana,
        int floor
    ) {}

    private final DungeonService dungeonService;

    public DungeonSessionController(Player player) {
        this.dungeonService = new DungeonService(player);
    }

    // Initialize dungeon state
    public void initDungeon() {
        dungeonService.initDungeon();
    }

    public boolean hasMoreFloors() {
        return dungeonService.hasMoreFloors();
    }

    public int getCurrentFloor() {
        return dungeonService.getCurrentFloor();
    }

    public boolean isBossFloor(int floor) {
        return dungeonService.isBossFloor(floor);
    }

    public Difficulty determineDifficulty(int floor) {
        return dungeonService.determineDifficulty(floor);
    }

    public int wavesForFloor(boolean isBossFloor) {
        return dungeonService.wavesForFloor(isBossFloor);
    }

    // Returns an enemy generated for the current wave
    public Enemy generateEnemy(Difficulty difficulty) {
        return dungeonService.generateEnemy(difficulty);
    }

    public Enemy generateBossEnemy(Difficulty difficulty) {
        return dungeonService.generateBossEnemy(difficulty);
    }

    public void scaleEnemyStats(Enemy enemy, Difficulty difficulty, boolean isBossFloor) {
        dungeonService.scaleEnemyStats(enemy, difficulty, isBossFloor);
    }

    public List<DungeonEvent> buildFloorStartEvents(int floor, boolean isBossFloor, Difficulty difficulty) {
        return dungeonService.buildFloorStartEvents(floor, isBossFloor, difficulty);
    }

    public List<DungeonEvent> buildWaveStartEvents(int wave, int totalWaves, Enemy enemy, boolean isBossFloor) {
        return dungeonService.buildWaveStartEvents(wave, totalWaves, enemy, isBossFloor);
    }

    public DungeonService.FloorOutcome processBattleResult(BattleResult result) {
        return dungeonService.processBattleResult(result);
    }

    public List<DungeonEvent> applySkillReward(int floor) {
        return dungeonService.applySkillReward(floor);
    }

    public List<DungeonEvent> advanceFloor() {
        return dungeonService.advanceFloor();
    }

    // Returns a snapshot of player status for the HUD — no Player reference leaks to view
    public PlayerStatusSnapshot getPlayerStatusSnapshot() {
        Player player = dungeonService.getPlayer();
        return new PlayerStatusSnapshot(
            player.getStats().getCurrentHP(),
            player.getStats().getMaxHP(),
            player.getStats().getCurrentMana(),
            player.getStats().getBaseMana(),
            dungeonService.getCurrentFloor()
        );
    }
}
