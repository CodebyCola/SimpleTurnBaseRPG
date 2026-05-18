/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.game.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import kelompok11.turnbaserpg.enums.BattleResult;
import kelompok11.turnbaserpg.enums.Difficulty;
import kelompok11.turnbaserpg.model.character.Enemy;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.skill.Skill;
import kelompok11.turnbaserpg.utils.GameConstants;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 * Manages dungeon floor progression logic.
 * This service handles enemy generation, difficulty
 * scaling, boss floors, and wave logic.
 */
public class DungeonService {

    private final Player player;

    public static final int WAVES_PER_FLOOR = 5;
    public static final int BOSS_HP_MULTIPLIER = 3;
    public static final int BOSS_ATK_MULTIPLIER = 2;

    public DungeonService(Player player) {
        this.player = player;
    }
    
    
    // Dungeon Initialisation
    public void initDungeon() {
        if (player.getCurrentFloor() == 0) {
            player.setCurrentFloor(GameConstants.DEFAULT_FLOOR);
        }
    }

    // Floor State Queries (called by DungeonController)
    public boolean hasMoreFloors() {
        return player.getCurrentFloor() <= GameConstants.MAX_FLOOR;
    }

    public int getCurrentFloor() {
        return player.getCurrentFloor();
    }

    public boolean isBossFloor(int floor) {
        return floor % GameConstants.FLOOR_MILESTONE == 0;
    }

    public Difficulty determineDifficulty(int floor) {
        if (floor <= GameConstants.EASY_FLOOR_MAX) {
            return Difficulty.EASY;
        }
        if (floor <= GameConstants.NORMAL_FLOOR_MAX) {
            return Difficulty.NORMAL;
        }
        if (floor <= GameConstants.HARD_FLOOR_MAX) {
            return Difficulty.HARD;
        }
        return Difficulty.NIGHTMARE;
    }

    public int wavesForFloor(boolean isBossFloor) {
        return isBossFloor ? 1 : WAVES_PER_FLOOR;
    }

    // Enemy Generation
    public Enemy generateEnemy(Difficulty difficulty) {
        String[] easyEnemies = {"Goblin", "Slime", "Wolf"};
        String[] normalEnemies = {"Troll", "Orc", "Skeleton"};
        String[] hardEnemies = {"Demon", "Succubus", "Vampire"};
        String[] nightmareEnemies = {"Dragon", "Lich", "Dark Knight"};

        String[] pool = switch (difficulty) {
            case EASY ->
                easyEnemies;
            case NORMAL ->
                normalEnemies;
            case HARD ->
                hardEnemies;
            case NIGHTMARE ->
                nightmareEnemies;
        };

        String name = pool[ThreadLocalRandom.current().nextInt(pool.length)];
        return new Enemy(name);
    }

    public Enemy generateBossEnemy(Difficulty difficulty) {
        String[] easyBosses = {"Goblin King", "SLime King", "Alpha Wolf King"};
        String[] normalBosses = {"Orc Warlord", "Stone Golem"};
        String[] hardBosses = {"Vampire Lord", "Arch Demon"};
        String[] nightmareBosses = {"Ancient Dragon", "Death Lich", "Shadow Emperor"};

        String[] pool = switch (difficulty) {
            case EASY ->
                easyBosses;
            case NORMAL ->
                normalBosses;
            case HARD ->
                hardBosses;
            case NIGHTMARE ->
                nightmareBosses;
        };

        String name = pool[ThreadLocalRandom.current().nextInt(pool.length)];
        return new Enemy(name);
    }

    public void scaleEnemyStats(Enemy enemy, Difficulty difficulty, boolean isBossFloor) {
        int floor = player.getCurrentFloor();
        int hp = (int) ((GameConstants.BASE_ENEMY_HP + (GameConstants.ENEMY_HP_PER_LEVEL * floor)) * difficulty.getStatMultiplier());
        int atk = (int) ((GameConstants.BASE_ENEMY_ATK + (GameConstants.ENEMY_ATK_PER_LEVEL * floor)) * difficulty.getStatMultiplier());
        int def = (int) ((GameConstants.BASE_ENEMY_DEF + (GameConstants.ENEMY_DEF_PER_LEVEL * floor)) * difficulty.getStatMultiplier());

        if (isBossFloor) {
            hp *= BOSS_HP_MULTIPLIER;
            atk *= BOSS_ATK_MULTIPLIER;
        }

        enemy.getStats().setMaxHP(hp);
        enemy.getStats().setCurrentHP(hp);
        enemy.getStats().setBaseAttack(atk);
        enemy.getStats().setBaseDefense(def);
    }

    // Floor Progression Events
    public List<DungeonEvent> buildFloorStartEvents(int floor, boolean isBossFloor, Difficulty difficulty) {
        List<DungeonEvent> events = new ArrayList<>();
        events.add(new DungeonEvent(DungeonEvent.Type.FLOOR_START,
                "==========================================\n"
                + "  FLOOR " + floor
                + (isBossFloor ? " [BOSS FLOOR]" : "")
                + " | Difficulty: " + difficulty.getDisplayName()
                + "\n=========================================="));
        return events;
    }

    /**
     * Builds events for a wave header. Call before each wave's battle.
     */
    
    public List<DungeonEvent> buildWaveStartEvents(int wave, int totalWaves, Enemy enemy, boolean isBossFloor) {
        List<DungeonEvent> events = new ArrayList<>();
        if (isBossFloor) {
            events.add(new DungeonEvent(DungeonEvent.Type.BOSS_APPEAR,
                    ">>> BOSS APPEARS: " + enemy.getCharacterName() + " <<<"));
        } else {
            events.add(new DungeonEvent(DungeonEvent.Type.WAVE_START,
                    "[Wave " + wave + "/" + totalWaves + "] Enemy: " + enemy.getCharacterName()));
        }
        return events;
    }

    // Post-Battle & Rewards
    public FloorOutcome processBattleResult(BattleResult result) {
        List<DungeonEvent> events = new ArrayList<>();

        switch (result) {
            case WIN ->
                events.add(new DungeonEvent(DungeonEvent.Type.BATTLE_RESULT, "Victory!"));
            case LOSE ->
                events.add(new DungeonEvent(DungeonEvent.Type.PLAYER_DEFEATED, "You were defeated..."));
            case ESCAPED ->
                events.add(new DungeonEvent(DungeonEvent.Type.PLAYER_ESCAPED, "You fled from battle."));
        }

        boolean floorContinues = (result == BattleResult.WIN);
        return new FloorOutcome(floorContinues, events);
    }

    /**
     * Grants a skill reward if the floor milestone is reached and the player
     * has room for more skills.
     */
    public List<DungeonEvent> applySkillReward(int floor) {
        List<DungeonEvent> events = new ArrayList<>();

        if (floor % GameConstants.FLOOR_MILESTONE == 0
                && player.getTotalUnlockedSkills() < GameConstants.MAX_SKILL_SLOTS) {

            Skill newSkill = Skill.getRandomSkill(player);
            if (newSkill != null) {
                player.unlockSkill(newSkill);
                events.add(new DungeonEvent(DungeonEvent.Type.SKILL_UNLOCKED,
                        "*** New Skill Unlocked: " + newSkill.getName() + " ***"));
                GameLogger.info(player.getCharacterName() + " unlocked skill: " + newSkill.getName());
            }
        }
        return events;
    }

    /**
     * Advances the player's floor counter by one. Returns events if the dungeon
     * has been completed.
     */
    public List<DungeonEvent> advanceFloor() {
        int cleared = player.getCurrentFloor();
        player.setCurrentFloor(cleared + 1);

        List<DungeonEvent> events = new ArrayList<>();
        if (player.getCurrentFloor() > GameConstants.MAX_FLOOR) {
            events.add(new DungeonEvent(DungeonEvent.Type.DUNGEON_COMPLETE,
                    "==========================================\n"
                    + "  CONGRATULATIONS! You cleared all 100 floors!\n"
                    + "=========================================="));
            GameLogger.info(player.getCharacterName() + " completed the dungeon!");
        }
        return events;
    }

    public Player getPlayer() {
        return player;
    }

    // Inner result type
    /**
     * Carries the outcome of processing a single battle result for a wave.
     */
    public static class FloorOutcome {

        private final boolean waveCleared;
        private final List<DungeonEvent> events;

        public FloorOutcome(boolean waveCleared, List<DungeonEvent> events) {
            this.waveCleared = waveCleared;
            this.events = events;
        }

        public boolean isWaveCleared() {
            return waveCleared;
        }

        public List<DungeonEvent> getEvents() {
            return events;
        }
    }
}
