/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.game.services;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import kelompok11.turnbaserpg.enums.BattleResult;
import kelompok11.turnbaserpg.enums.Difficulty;
import kelompok11.turnbaserpg.model.character.Enemy;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.skill.Skill;
import kelompok11.turnbaserpg.utils.GameConstants;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 * Manages dungeon floor progression logic. Console I/O for routing/menus is
 * delegated to GameManager. This service handles enemy generation, difficulty
 * scaling, boss floors, and wave logic.
 */
public class DungeonService {

    private final Player player;
    private static final int WAVES_PER_FLOOR = 5;
    private static final int BOSS_HP_MULTIPLIER = 3;
    private static final int BOSS_ATK_MULTIPLIER = 2;

    public DungeonService(Player player) {
        this.player = player;
    }

    // -------------------------------------------------------------------------
    // Main Dungeon Loop (console-driven, called by GameManager)
    // -------------------------------------------------------------------------
    public void attackDungeon(Scanner input) {
        GameLogger.info(player.getCharacterName() + " entering dungeon");

        if (player.getCurrentFloor() == 0) {
            player.setCurrentFloor(GameConstants.DEFAULT_FLOOR);
        }

        boolean isRunning = true;

        while (player.getCurrentFloor() <= GameConstants.MAX_FLOOR && isRunning) {
            int floor = player.getCurrentFloor();
            boolean isBossFloor = isBossFloor(floor);
            Difficulty difficulty = determineDifficulty(floor);

            System.out.println("==========================================");
            System.out.println("  FLOOR " + floor + (isBossFloor ? " [BOSS FLOOR]" : "")
                    + " | Difficulty: " + difficulty.getDisplayName());
            System.out.println("==========================================");

            boolean floorCleared = runFloor(difficulty, isBossFloor, input);

            if (!floorCleared) {
                isRunning = false;
                break;
            }

            // Floor cleared
            handleSkillReward(floor);
            player.setCurrentFloor(floor + 1);

            if (player.getCurrentFloor() > GameConstants.MAX_FLOOR) {
                break;
            }

            System.out.print("Advance to Floor " + player.getCurrentFloor() + "? (y/n): ");
            String next = input.next();
            if (next.equalsIgnoreCase("n")) {
                isRunning = false;
            }
        }

        if (player.getCurrentFloor() > GameConstants.MAX_FLOOR) {
            System.out.println("==========================================");
            System.out.println("  CONGRATULATIONS! You cleared all 100 floors!");
            System.out.println("==========================================");
            GameLogger.info(player.getCharacterName() + " completed the dungeon!");
        }
    }

    // -------------------------------------------------------------------------
    // Floor & Wave Logic
    // -------------------------------------------------------------------------
    /**
     * Runs all waves for one floor.
     *
     * @return true if all waves were cleared, false if player lost or escaped
     */
    private boolean runFloor(Difficulty difficulty, boolean isBossFloor, Scanner input) {
        int totalWaves = isBossFloor ? 1 : WAVES_PER_FLOOR;

        for (int wave = 1; wave <= totalWaves; wave++) {
            Enemy enemy;
            if (isBossFloor) {
                enemy = generateBossEnemy(difficulty);
                System.out.println(">>> BOSS APPEARS: " + enemy.getCharacterName() + " <<<");
            } else {
                enemy = generateEnemy(difficulty);
                System.out.println("[Wave " + wave + "/" + totalWaves + "] Enemy: " + enemy.getCharacterName());
            }

            scaleEnemyStats(enemy, difficulty, isBossFloor);

            BattleService battle = new BattleService(player, enemy);
            BattleResult result = battle.runBattleLoop(input);

            handleBattleResult(result);

            if (result == BattleResult.LOSE) {
                return false;
            }
            if (result == BattleResult.ESCAPED) {
                return false;
            }
        }

        System.out.println("Floor " + player.getCurrentFloor() + " cleared!");
        return true;
    }

    // -------------------------------------------------------------------------
    // Difficulty & Enemy Generation
    // -------------------------------------------------------------------------
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

    public boolean isBossFloor(int floor) {
        return floor % GameConstants.FLOOR_MILESTONE == 0;
    }

    public Enemy generateEnemy(Difficulty difficulty) {
        String[] easyEnemies = {"Goblin", "Slime", "Wolf"};
        String[] normalEnemies = {"Troll", "Orc", "Skeleton"};
        String[] hardEnemies = {"Demon", "Succubus", "Vampire"};
        String[] nightmareEnemies = {"Dragon", "Lich", "Dark Knight"};

        String[] pool;
        switch (difficulty) {
            case EASY ->
                pool = easyEnemies;
            case NORMAL ->
                pool = normalEnemies;
            case HARD ->
                pool = hardEnemies;
            case NIGHTMARE ->
                pool = nightmareEnemies;
            default ->
                pool = new String[]{"Unknown"};
        }

        String name = pool[ThreadLocalRandom.current().nextInt(pool.length)];
        return new Enemy(name);
    }

    public Enemy generateBossEnemy(Difficulty difficulty) {
        String[] easyBosses = {"Goblin King"};
        String[] normalBosses = {"Orc Warlord", "Stone Golem"};
        String[] hardBosses = {"Vampire Lord", "Arch Demon"};
        String[] nightmareBosses = {"Ancient Dragon", "Death Lich", "Shadow Emperor"};

        String[] pool;
        switch (difficulty) {
            case EASY ->
                pool = easyBosses;
            case NORMAL ->
                pool = normalBosses;
            case HARD ->
                pool = hardBosses;
            case NIGHTMARE ->
                pool = nightmareBosses;
            default ->
                pool = new String[]{"????"};
        }

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

    // -------------------------------------------------------------------------
    // Rewards
    // -------------------------------------------------------------------------
    private void handleSkillReward(int floor) {
        if (floor % GameConstants.FLOOR_MILESTONE == 0
                && player.getTotalUnlockedSkills() < GameConstants.MAX_SKILL_SLOTS) {
            Skill newSkill = Skill.getRandomSkill(player);
            if (newSkill != null) {
                player.unlockSkill(newSkill);
                System.out.println("*** New Skill Unlocked: " + newSkill.getName() + " ***");
                GameLogger.info(player.getCharacterName() + " unlocked skill: " + newSkill.getName());
            }
        }
    }

    private void handleBattleResult(BattleResult result) {
        switch (result) {
            case WIN ->
                System.out.println("Victory!");
            case LOSE ->
                System.out.println("You were defeated...");
            case ESCAPED ->
                System.out.println("You fled from battle.");
        }
    }

    public Player getPlayer() {
        return player;
    }
}
