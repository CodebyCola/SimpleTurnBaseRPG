/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.game;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import kelompok11.turnbaserpg.enums.Difficulty;
import kelompok11.turnbaserpg.model.Enemy;
import kelompok11.turnbaserpg.model.Player;
import kelompok11.turnbaserpg.utils.GameConstants;

/**
 *
 * @author Pongo
 */
public class DungeonSystem {

    private Player player;
    private Difficulty diff;
    Scanner input = new Scanner(System.in);

    public DungeonSystem(Player player) {
        this.player = player;
    }

    public void attackDungeon() {
        boolean isRunning = true;

        if (player.getCurrentFloor() == 0) {
            player.setCurrentFloor(GameConstants.DEFAULT_FLOOR);
        }

        while (player.getCurrentFloor() <= GameConstants.MAX_FLOOR && isRunning) {
            System.out.println("Floor : " + player.getCurrentFloor());
            int maxWave = 5;
            diff = determineDifficulty();
            System.out.println("Welcome To Floor : " + player.getCurrentFloor());

            for (int wave = 1; wave <= maxWave; wave++) {
                if (player.isAlive() == false) {
                    isRunning = false;
                    break;
                }

                Enemy enemy = generateEnemy();
                updateEnemyStatus(enemy);
                BattleSystem battle = new BattleSystem(player, enemy);
                boolean result = battle.startBattle(); // return 1 = menang, return 0 = kalah

                if (!result) { // kalau kalah
                    isRunning = false;
                    break;
                }
            }

            if (player.isAlive()) {
                player.setCurrentFloor(player.getCurrentFloor() + 1);
                System.out.println("Step to next floor ? (y/n)");
                String next = input.next();
                if (next.equalsIgnoreCase("n")) {
                    isRunning = false;
                }
            }
        }
    }

    public Difficulty determineDifficulty() {
        if (player.getCurrentFloor() <= GameConstants.EASY_FLOOR_MAX) {
            diff = Difficulty.EASY;
        } else if (player.getCurrentFloor() <= GameConstants.NORMAL_FLOOR_MAX) {
            diff = Difficulty.NORMAL;
        } else if (player.getCurrentFloor() <= GameConstants.HARD_FLOOR_MAX) {
            diff = Difficulty.HARD;
        } else { // NightMare
            diff = Difficulty.NIGHTMARE;
        }

        return diff;
    }

    public Enemy generateEnemy() {

        String enemyName;

        switch (diff) {

            case EASY -> {
                String[] easyEnemies = {
                    "Goblin",
                    "Slime",
                    "Wolf"
                };

                enemyName
                        = easyEnemies[ThreadLocalRandom.current()
                                .nextInt(easyEnemies.length)];
            }

            case NORMAL -> {
                String[] normalEnemies = {
                    "Troll",
                    "Orc",
                    "Skeleton"
                };

                enemyName
                        = normalEnemies[ThreadLocalRandom.current()
                                .nextInt(normalEnemies.length)];
            }

            case HARD -> {
                String[] hardEnemies = {
                    "Demon",
                    "Succubus",
                    "Vampire"
                };

                enemyName = hardEnemies[ThreadLocalRandom.current()
                        .nextInt(hardEnemies.length)];
            }

            case NIGHTMARE -> {
                String[] nightmareEnemies = {
                    "Dragon",
                    "Lich",
                    "Dark Knight"
                };

                enemyName = nightmareEnemies[ThreadLocalRandom.current()
                        .nextInt(nightmareEnemies.length)];
            }

            default ->
                enemyName = "Unknown";
        }

        return new Enemy(enemyName);
    }

    public void updateEnemyStatus(Enemy enemy) {
        int maxHP = GameConstants.ENEMY_HP_PER_LEVEL * player.getCurrentFloor();
        maxHP = (int) (maxHP * diff.getStatMultiplier());

        int attack = GameConstants.ENEMY_ATK_PER_LEVEL * player.getCurrentFloor();
        attack = (int) (attack * diff.getStatMultiplier());

        int defense = GameConstants.ENEMY_DEF_PER_LEVEL * player.getCurrentFloor();
        defense = (int) (defense * diff.getStatMultiplier());
        enemy.getStats().increaseMaxHP(maxHP);
        enemy.getStats().increaseAttack(attack);
        enemy.getStats().increaseDefense(defense);
    }

}
