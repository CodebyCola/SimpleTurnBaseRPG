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
 * Manages battle logic between a Player and an Enemy.
 * All console I/O is handled by GameManager; this class only manages game state.
 */
public class BattleService {

    private final Player player;
    private final Enemy enemy;

    private int enemyTurnCounter;
    private boolean isEscaped;
    private boolean playerTurn;
    private boolean playerDefend;

    public BattleService(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.isEscaped = false;
        this.playerTurn = true;
        this.playerDefend = false;
        this.enemyTurnCounter = 1;
    }

    // -------------------------------------------------------------------------
    // Battle Loop (called by GameManager)
    // -------------------------------------------------------------------------

    public BattleResult startBattle() {
        playerTurn = true;
        enemyTurnCounter = 1;

        System.out.println("--- Battle Start: " + player.getCharacterName() + " vs " + enemy.getCharacterName() + " ---");

        while (enemy.isAlive() && !isEscaped && player.isAlive()) {
            if (playerTurn) {
                gainMana();
                player.updateBuffs();
                player.updateSkillCooldowns();
                // Player turn is handled by GameManager which calls the action methods
                // and then calls advanceTurn() to move to enemy turn
                return null; // Signal: waiting for player input
            } else {
                enemyTurn();
                if (playerDefend) {
                    player.setDefend(false);
                    playerDefend = false;
                }
                playerTurn = true;
            }
        }
        return resolveResult();
    }

    /**
     * Full battle loop for console mode. GameManager calls this when it has handled
     * all player input via the action methods below.
     */
    public BattleResult runBattleLoop(java.util.Scanner input) {
        playerTurn = true;
        enemyTurnCounter = 1;

        System.out.println("--- Battle Start: " + player.getCharacterName()
                + " vs " + enemy.getCharacterName() + " ---");

        while (enemy.isAlive() && !isEscaped && player.isAlive()) {
            if (playerTurn) {
                gainMana();
                player.updateBuffs();
                player.updateSkillCooldowns();
                playerTurn(input);
            } else {
                enemyTurn();
                if (playerDefend) {
                    player.setDefend(false);
                    playerDefend = false;
                }
            }

            if (!enemy.isAlive()) break;
            playerTurn = !playerTurn;
        }

        applyWinRewards();
        return resolveResult();
    }

    // -------------------------------------------------------------------------
    // Turn Logic
    // -------------------------------------------------------------------------

    public void gainMana() {
        switch (player.getRole()) {
            case WARRIOR -> player.getStats().increaseCurrentMana(5);
            case MAGE    -> player.getStats().increaseCurrentMana(20);
            case ARCHER  -> player.getStats().increaseCurrentMana(10);
        }
    }

    private void playerTurn(java.util.Scanner input) {
        printBattleStatus();
        boolean validAction = false;

        while (!validAction) {
            printPlayerMenu();
            int choice = input.nextInt();
            validAction = handlePlayerAction(choice, input);
        }
    }

    /**
     * Processes a player's chosen battle action.
     * @param action 1=Attack, 2=Defend, 3=Skill, 4=Item, 5=Escape
     * @return true if the action was valid and the turn should end
     */
    public boolean handlePlayerAction(int action, java.util.Scanner input) {
        switch (action) {
            case 1 -> {
                player.basicAttack(enemy);
                return true;
            }
            case 2 -> {
                player.setDefend(true);
                playerDefend = true;
                System.out.println(player.getCharacterName() + " takes a defensive stance!");
                return true;
            }
            case 3 -> {
                if (player.getTotalUnlockedSkills() == 0) {
                    System.out.println("No skills available!");
                    return false;
                }
                return useSkillMenu(input);
            }
            case 4 -> {
                if (player.getInventory().isEmpty()) {
                    System.out.println("Inventory is empty!");
                    return false;
                }
                player.getInventory().showInventory();
                System.out.println("Choose item (number): ");
                int index = input.nextInt() - 1;
                player.getInventory().useItem(index, player);
                return true;
            }
            case 5 -> {
                if (Math.random() < 0.5) {
                    System.out.println(player.getCharacterName() + " escaped!");
                    isEscaped = true;
                } else {
                    System.out.println("Escape failed!");
                }
                return true;
            }
            default -> {
                System.out.println("Invalid choice! Try again.");
                return false;
            }
        }
    }

    public void enemyTurn() {
        System.out.println("--- Enemy Turn: " + enemy.getCharacterName() + " ---");
        if (enemyTurnCounter % 3 == 0) {
            enemy.skillAttack(player);
        } else {
            enemy.basicAttack(player);
        }
        enemyTurnCounter++;
    }

    public boolean useSkillMenu(java.util.Scanner input) {
        System.out.println("=== SKILL LIST ===");
        var skills = player.getUnlockedSkills();
        for (int i = 0; i < skills.size(); i++) {
            Skill skill = skills.get(i);
            System.out.printf("[%d] %-18s | Mana: %2d | CD: %d%n",
                i + 1, skill.getName(), skill.getManaCost(), skill.getCurrentCooldown());
        }
        System.out.println("Choose skill (0 to cancel): ");
        int choice = input.nextInt();
        if (choice == 0) return false;
        if (choice < 1 || choice > skills.size()) {
            System.out.println("Invalid skill choice!");
            return false;
        }
        return skills.get(choice - 1).cast(player, enemy);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void applyWinRewards() {
        if (!enemy.isAlive()) {
            int exp = (int) (GameConstants.BASE_EXP_REWARD
                    * GameConstants.EXP_SCALING_PER_LEVEL * (player.getLevel() * 0.1));
            player.gainExp(exp);

            if (Math.random() < 0.3) {
                int gold = (int) (GameConstants.BASE_GOLD_REWARD
                        * GameConstants.GOLD_SCALING_PER_LEVEL * player.getLevel());
                player.gainGold(gold);
            }

            if (Math.random() < GameConstants.LOOT_DROP_RATE) {
                if (Math.random() < 0.5) {
                    player.getInventory().addItem(new HealthPotion(SMALL));
                } else {
                    player.getInventory().addItem(new ManaPotion(SMALL));
                }
            }
        }
    }

    private BattleResult resolveResult() {
        if (!player.isAlive()) return BattleResult.LOSE;
        if (isEscaped) return BattleResult.ESCAPED;
        return BattleResult.WIN;
    }

    private void printBattleStatus() {
        System.out.println("--- Your Turn ---");
        System.out.printf("HP: %d/%d   Mana: %d/%d%n",
            player.getStats().getCurrentHP(), player.getStats().getMaxHP(),
            player.getStats().getCurrentMana(), player.getStats().getBaseMana());
        System.out.printf("Enemy HP: %d/%d%n",
            enemy.getStats().getCurrentHP(), enemy.getStats().getMaxHP());
    }

    private void printPlayerMenu() {
        System.out.println("1. Basic Attack");
        System.out.println("2. Defend");
        System.out.println("3. Use Skill");
        System.out.println("4. Use Item");
        System.out.println("5. Escape (50% chance)");
        System.out.print("Choose: ");
    }

    public boolean isEscaped() { return isEscaped; }
    public Player getPlayer() { return player; }
    public Enemy getEnemy() { return enemy; }
}
