/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.controller;

import java.util.Scanner;
import kelompok11.turnbaserpg.model.Enemy;
import kelompok11.turnbaserpg.model.Player;

/**
 *
 * @author Pongo
 */
public class GameController {

    private Scanner scanner = new Scanner(System.in);
    private Player player;
    private Enemy enemy;

    public void startGame() {
        showMenu();
    }

    private void showMenu() {
        System.out.println("=== TURN BASE RPG ===");
        System.out.println("1. Start New Game");
        System.out.println("2. Load Character");
        System.out.println("3. Exit");

        int choice = scanner.nextInt();

        if (choice == 1) {
            initGame();
            gameLoop();
        } else {
            System.out.println("Bye!");
        }
    }

    private void initGame() {

        scanner.nextLine(); // clear buffer
        
        System.out.println("Enter your character name: ");
        String characterName = scanner.nextLine();
        player = new Player(characterName);

        enemy = new Enemy("Goblin");

        System.out.println("Game Started!");
    }

    private void gameLoop() {
        while (player.isAlive() && enemy.isAlive()) {

            System.out.println("\nPlayer HP: " + player.getCharacterHP());
            System.out.println("Enemy HP: " + enemy.getCharacterHP());

            playerTurn();
            if (!enemy.isAlive()) {
                break;
            }

            enemyTurn();
        }

        if (player.isAlive()) {
            System.out.println("You Win!");
        } else {
            System.out.println("You Lose!");
        }
    }

    private void playerTurn() {
        System.out.println("1. Attack");
        System.out.println("2. Heal");

        int choice = scanner.nextInt();

        if (choice == 1) {
            int damage = player.attack();
            enemy.takeDamage(damage);
            System.out.println("You deal " + damage + " damage!");
        } else if (choice == 2) {
            player.setCharacterHP(player.getCharacterHP() + 20);
            System.out.println("You heal 20 HP!");
        }

    }

    private void enemyTurn() {
        int damage = enemy.attack();
        player.takeDamage(damage);

        System.out.println("Enemy attacks for " + damage + " damage!");
    }
}
