/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.game;

import java.util.Scanner;
import kelompok11.turnbaserpg.enums.Role;
import kelompok11.turnbaserpg.model.Character.Player;

/**
 *
 * @author Pongo
 */
public class GameManager {

    private DungeonSystem dungeon;
    Scanner input = new Scanner(System.in);

//    public void loadGame()
    public void startNewGame() {
        System.out.println("Input your character nname: ");
        String characterName = input.nextLine();

        Role role = null;
        System.out.println("Pilih Role mu:");
        System.out.println("1. " + role.WARRIOR.getDisplayName() + " - " + role.WARRIOR.getDescription());
        System.out.println("2. " + role.MAGE.getDisplayName() + " - " + role.MAGE.getDescription());
        System.out.println("3. " + role.ARCHER.getDisplayName() + " - " + role.ARCHER.getDescription());
        System.out.println("Input Angka : ");
        int pilihanRole = input.nextInt();

        switch (pilihanRole) {
            case 1:
                role = Role.WARRIOR;
                break;
            case 2:
                role = Role.MAGE;
                break;
            case 3:
                role = Role.ARCHER;
                break;
        }

        Player player = new Player(characterName, role);

        player.getPlayerDetail();
        player.getStatsDetail();
        dungeon = new DungeonSystem(player);
        mainMenu();

    }

    public void mainMenu() {
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
                break;

        }
    }
}
