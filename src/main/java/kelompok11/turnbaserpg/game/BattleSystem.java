/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.game;

import java.util.Scanner;
import kelompok11.turnbaserpg.model.Enemy;
import kelompok11.turnbaserpg.model.Player;
import kelompok11.turnbaserpg.utils.GameConstants;

/**
 *
 * @author Pongo
 */
public class BattleSystem {

    private Player player;
    private Enemy enemy;
    int turn = 1;

    int temporaryDefenseBonus = 0;
    boolean isEscaped = false;

    Scanner input = new Scanner(System.in);

    public BattleSystem(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;

    }

    public boolean startBattle() {

        while (player.isAlive() && enemy.isAlive() && !isEscaped) {
            System.out.println("Player HP : " + player.getStats().getCurrentHP());
            System.out.println("Enemy (" + enemy.getCharacterName() + ") HP : " + enemy.getStats().getCurrentHP());

            if (turn % 2 == 1) {
                System.out.println("Turn : " + turn);
                System.out.println("Choose your move :");
                System.out.println("1. Basic Attack");
                System.out.println("2. Use Skill");
                System.out.println("3. Defend");
                if (turn == 1) {
                    System.out.println("4. Run (leave)");
                }
                int move = input.nextInt();
                move(move);
            } else {
                if (turn % 3 == 0) {
                    int damage = enemy.skillAttack();
                    player.takeDamage(damage);
                    System.out.println("Player take " + damage + " damage");

                } else {
                    enemy.basicAttack(enemy, player);
                    System.out.println("Player take "
                            + enemy.getStats().getTotalAttack() + " ");
                }

            }

            if (enemy.isAlive() == false) {
                player.gainExp(GameConstants.BASE_EXP_REWARD
                        * (player.getCurrentFloor() * GameConstants.EXP_SCALING_PER_LEVEL));

            }

            turn++;
        }
        player.getStats().setBaseDefense(player.getStats().getBaseDefense() - temporaryDefenseBonus);

        return player.isAlive() && !isEscaped;
    }

    public void move(int move) {
        switch (move) {
            case 1 -> {
                player.basicAttack(player, enemy);
                System.out.println("Enemy take "
                        + player.getStats().getTotalAttack() + " damage");
            }

            case 2 -> {
//                Nanti ditambahin
            }

            case 3 -> {
                temporaryDefenseBonus += 10;
                player.getStats().increaseDefense(10);
                System.out.println("Player increasing defense by 10");

            }

            case 4 -> {
                if (turn == 1) {
                    isEscaped = true;
                } else {
                    System.out.println("You can only leave in turn 1");
                }

            }

            default -> {
                System.out.println("Invalid move!");
            }
        }

    }
}
