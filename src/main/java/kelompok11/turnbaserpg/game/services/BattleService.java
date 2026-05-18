/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.game.services;

import java.util.Scanner;
import kelompok11.turnbaserpg.enums.BattleResult;
import static kelompok11.turnbaserpg.enums.PotionTier.*;
import kelompok11.turnbaserpg.model.character.Enemy;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.item.consumable.*;
import kelompok11.turnbaserpg.model.skill.Skill;
import kelompok11.turnbaserpg.utils.GameConstants;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 *
 * @author Pongo
 */
public class BattleService {

    private Player player;
    private Enemy enemy;

    private int enemyTurnCounter;
    private boolean isEscaped = false;
    private boolean playerTurn;
    private boolean playerDefend = false;

    Scanner input = new Scanner(System.in);

    public BattleService(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;

    }

    public BattleResult startBattle() {
        playerTurn = true;
        enemyTurnCounter = 1;

        while (enemy.isAlive() && !isEscaped && player.isAlive()) {

            if (playerTurn) {
                gainMana();
                player.updateBuffs();
                player.updateSkillCooldowns();

                playerTurn();

                if (!enemy.isAlive()) {
                    break;
                }

            } else {
                enemyTurn();
                if (playerDefend) {
                    player.setDefend(false);
                    playerDefend = false;
                }
            }

            playerTurn = !playerTurn;

        }

        if (!enemy.isAlive()) {
            player.gainExp((int) (GameConstants.BASE_EXP_REWARD
                    * GameConstants.EXP_SCALING_PER_LEVEL * (player.getLevel() * 0.1)));

            if (Math.random() < 0.3) {
                player.gainGold((int) (GameConstants.BASE_GOLD_REWARD
                        * GameConstants.GOLD_SCALING_PER_LEVEL * player.getLevel()));
            }

            double roll = Math.random();

            if (roll < 0.2) {
                GameLogger.info(player.getCharacterName() + " gain small potion");
                if (Math.random() < 0.5) {
                    player.getInventory().addItem(new HealthPotion(SMALL));
                } else {
                    player.getInventory().addItem(new ManaPotion(SMALL));
                }

            }

        }

        if (!player.isAlive()) {
            return BattleResult.LOSE;
        } else if (isEscaped) {
            return BattleResult.ESCAPED;
        }

        return BattleResult.WIN;
    }

    public void gainMana() {
        switch (player.getRole()) {
            case WARRIOR -> {
                player.getStats().increaseCurrentMana(5);
            }
            case MAGE -> {
                player.getStats().increaseCurrentMana(20);
            }

            case ARCHER -> {
                player.getStats().increaseCurrentMana(10);
            }
        }

    }

    public void playerTurn() {
        System.out.println("Player Turn");
        System.out.println("HP : " + player.getStats().getCurrentHP());
        boolean isValid = false;
        int pil;

        while (!isValid) {
            System.out.println("Choose: ");
            System.out.println("1. Basic Attack");
            System.out.println("2. Defend");
            System.out.println("3. Use Skill");
            System.out.println("4. Inventory");
            System.out.println("5. Escape (50% chance)");

            System.out.println("Type number you choose :");
            pil = input.nextInt();

            switch (pil) {
                case 1 -> {
                    player.basicAttack(enemy);
                    isValid = true;
                }

                case 2 -> {
                    player.setDefend(true);
                    playerDefend = true;
                    isValid = true;

                }

                case 3 -> {
                    if (player.getTotalUnlockedSkills() != 0) {
                        if (useSkillMenu()) {
                            isValid = true;
                        }
                    } else {
                        System.out.println("No Skills available!");
                    }
                }

                case 4 -> {
                    if (player.getInventory().isEmpty()) {
                        System.out.println("Inventory empty!");
                        break;
                    }

                    player.getInventory().showInventory();
                    System.out.println("Choose item : ");
                    int index = input.nextInt() - 1;
                    player.getInventory().useItem(index, player);
                    isValid = true;
                }

                case 5 -> {
                    if (Math.random() < 0.7) {
                        System.out.println("You Escaped!");
                        isEscaped = true;
                    } else {
                        System.out.println("Cannot escape!");
                    }
                    isValid = true;
                }

                default -> {
                    System.out.println("Input tidak valid!");
                }

            }

        }

    }

    public void enemyTurn() {
        System.out.println("Enemy attack");
        if (enemyTurnCounter % 3 == 0) {
            enemy.skillAttack(player);
        } else {
            enemy.basicAttack(player);
        }

        enemyTurnCounter++;
    }

    public boolean useSkillMenu() {
        if (player.getUnlockedSkills().isEmpty()) {
            System.out.println("No skills available!");
            return false;
        }

        System.out.println("=== Skill List ===");

        for (int i = 0; i < player.getUnlockedSkills().size(); i++) {

            Skill skill = player.getUnlockedSkills().get(i);

            System.out.println(
                    (i + 1) + ". "
                    + skill.getName()
                    + " | Mana: "
                    + skill.getManaCost()
                    + " | CD: "
                    + skill.getCurrentCooldown()
            );
        }

        System.out.println("Choose skill:");

        int choice = input.nextInt();

        if (choice < 1
                || choice > player.getUnlockedSkills().size()) {

            System.out.println("Invalid choice!");
            return false;
        }

        Skill selectedSkill
                = player.getUnlockedSkills().get(choice - 1);

        return selectedSkill.cast(player, enemy);
    }

}
