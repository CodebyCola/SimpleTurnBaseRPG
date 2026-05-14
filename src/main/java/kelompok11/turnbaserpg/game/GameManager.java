/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.game;

import java.util.Scanner;
import kelompok11.turnbaserpg.enums.Role;
import kelompok11.turnbaserpg.model.Character.Player;
import kelompok11.turnbaserpg.model.skill.BasicHeal;

/**
 *
 * @author Pongo
 */
public class GameManager {

    private DungeonSystem dungeon;
    Scanner input = new Scanner(System.in);
    private Player player;

//    public void loadGame()
    public void startNewGame() {

        System.out.println("Input your character name: ");
        String characterName;
        do {
            characterName = input.nextLine().trim();
        } while (characterName.isEmpty());

        Role role = null;
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

        player = new Player(characterName, role);
        player.unlockSkill(new BasicHeal());
        player.getPlayerDetail();
        player.getStatsDetail();
        dungeon = new DungeonSystem(player);
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
}
