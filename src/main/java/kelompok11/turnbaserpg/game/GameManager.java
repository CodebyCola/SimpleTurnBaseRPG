/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.game;

import kelompok11.turnbaserpg.game.services.DungeonService;
import java.sql.Connection;
import java.util.Scanner;
import kelompok11.turnbaserpg.database.Connector;
import kelompok11.turnbaserpg.enums.Role;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.skill.BasicHeal;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 *
 * @author Pongo
 */
public class GameManager {

    private DungeonService dungeon;
    Scanner input = new Scanner(System.in);
    private Player player;
    private String characterName;
    private Role role = null;
    
    

//    public void loadGame()
    public void startNewGame() {

        System.out.println("Input your character name: ");

        do {
            characterName = input.nextLine().trim();
        } while (characterName.isEmpty());

        chooseRole();

        player = new Player(characterName, role);
        GameLogger.info(characterName + " Join the game");
        player.unlockSkill(new BasicHeal());
        GameLogger.info(characterName + " Gain " + player.getUnlockedSkills().get(0).getName());
        dungeon = new DungeonService(player);
        showMainMenu();

    }

    public void showMainMenu() {
        boolean isRunning = true;
        while (isRunning) {

            System.out.println("1. Jelajahi Dungeon");
            System.out.println("2. Save Data");
            System.out.println("3. Inventory");
            System.out.println("4. Informasi Character");
            System.out.println("5. Exit");
            System.out.println("Input angka : ");
            int pilihanMenu = input.nextInt();

            switch (pilihanMenu) {
                case 1:
                    
                    dungeon.attackDungeon();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    player.getPlayerDetail();
                    player.getStatsDetail();
                    break;
                case 5:
                    isRunning = false;
                    break;
                default:
                    System.out.println("invalid menu!");
                    break;

            }
        }

    }

    public void chooseRole() {
        while (role == null) {

            System.out.println("Pilih Role mu:");
            System.out.println("1. " + Role.WARRIOR.getDisplayName() + " - " + Role.WARRIOR.getDescription());
            System.out.println("2. " + Role.MAGE.getDisplayName() + " - " + Role.MAGE.getDescription());
            System.out.println("3. " + Role.ARCHER.getDisplayName() + " - " + Role.ARCHER.getDescription());
            System.out.println("Input Angka : ");
            int pilihanRole = input.nextInt();

            switch (pilihanRole) {
                case 1 -> {
                    role = Role.WARRIOR;
                }

                case 2 -> {
                    role = Role.MAGE;
                }

                case 3 -> {
                    role = Role.ARCHER;
                }

                default -> {
                    System.out.println("Role invalid, try again!");
                }
            }
        }

        GameLogger.info(characterName + " Role is " + role);
    }
}
