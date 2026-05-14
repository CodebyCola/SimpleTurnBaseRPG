/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.game;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import kelompok11.turnbaserpg.enums.BattleResult;
import kelompok11.turnbaserpg.enums.Difficulty;
import kelompok11.turnbaserpg.model.Character.Enemy;
import kelompok11.turnbaserpg.model.Character.Player;
import kelompok11.turnbaserpg.model.skill.BasicHeal;
import kelompok11.turnbaserpg.model.skill.Skill;
import kelompok11.turnbaserpg.utils.GameConstants;

/**
 *
 * @author Pongo
 */
public class DungeonSystem {

    private Player player;
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
            int maxWave = 5;

            Difficulty diff = determineDifficulty(); // Menentukan tingkat kesulitan berdasarkan lantai
            System.out.println("Welcome To Floor : " + player.getCurrentFloor());

            for (int wave = 1; wave <= maxWave; wave++) {

                Enemy enemy = generateEnemy(diff); // Generate musuh random
                updateEnemyStatus(enemy, diff); // update status musuh menyesuaikan lantai
                BattleSystem battle = new BattleSystem(player, enemy); // buat object battle controller
                BattleResult result = battle.startBattle(); // return 1 = menang, return 0 = kalah
                handleBattleResult(result); // menentukan hasil dari battle

                if (result != BattleResult.WIN) {
                    isRunning = false;
                    break;
                }
            }

//            // Jika Player menyelesaikan semua wave dan menang
            if (player.isAlive() && isRunning) {
                player.setCurrentFloor(player.getCurrentFloor() + 1); // update lantai
                System.out.println("Step to next floor ? (y/n)");
                String next = input.next();
                if (next.equalsIgnoreCase("n")) {
                    isRunning = false;
                }

                // reward skill didapat setelah menyelesaikan milestone floor ( kelipatan 10 )
                if (player.getCurrentFloor() % GameConstants.FLOOR_MILESTONE == 0
                        && player.getTotalUnlockedSkills() < GameConstants.MAX_SKILL_SLOTS) {

                    Skill newSkill = Skill.getRandomSkill(player);

                    if (newSkill != null) {

                        player.unlockSkill(newSkill);
                        System.out.println(
                                "You gain new skill : " + newSkill.getName()
                        );
                    }

                }
            }

        }

        // Logic berhenti loop ketika lantai sudah mencapai 101
        if (player.getCurrentFloor() > GameConstants.MAX_FLOOR) {
            System.out.println("Congrats you win!");

        }

    }

    public Difficulty determineDifficulty() {
        if (player.getCurrentFloor() <= GameConstants.EASY_FLOOR_MAX) {
            return Difficulty.EASY;
        } else if (player.getCurrentFloor() <= GameConstants.NORMAL_FLOOR_MAX) {
            return Difficulty.NORMAL;
        } else if (player.getCurrentFloor() <= GameConstants.HARD_FLOOR_MAX) {
            return Difficulty.HARD;
        } else { // NightMare
            return Difficulty.NIGHTMARE;
        }
    }

    public Enemy generateEnemy(Difficulty diff) {
        String enemyName;
        
        switch (diff) {
            case EASY -> {
                String[] easyEnemies = {
                    "Goblin",
                    "Slime",
                    "Wolf"
                };

                enemyName = easyEnemies[ThreadLocalRandom.current()
                                .nextInt(easyEnemies.length)];
            }

            case NORMAL -> {
                String[] normalEnemies = {
                    "Troll",
                    "Orc",
                    "Skeleton"
                };

                enemyName = normalEnemies[ThreadLocalRandom.current()
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

    public void updateEnemyStatus(Enemy enemy, Difficulty diff) {
        int HP = GameConstants.ENEMY_HP_PER_LEVEL * player.getCurrentFloor();
        HP = (int) (HP * diff.getStatMultiplier());

        int attack = GameConstants.ENEMY_ATK_PER_LEVEL * player.getCurrentFloor();
        attack = (int) (attack * diff.getStatMultiplier());

        int defense = GameConstants.ENEMY_DEF_PER_LEVEL * player.getCurrentFloor();
        defense = (int) (defense * diff.getStatMultiplier());

        enemy.getStats().increaseBaseHP(HP - enemy.getStats().getMaxHP());
        enemy.getStats().increaseBaseAttack(attack - enemy.getStats().getBaseAttack());
        enemy.getStats().increaseBaseDefense(defense - enemy.getStats().getBaseDefense());
    }

    private void handleBattleResult(BattleResult result) {
        switch (result) {

            case WIN -> {
                System.out.println("You win! next wave incoming");
            }

            case LOSE -> {
                System.out.println("Game Over!");
            }

            case ESCAPED -> {
                System.out.println("You escaped from dungeon");
            }
        }
    }

}
