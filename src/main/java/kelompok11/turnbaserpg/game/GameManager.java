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
 * Central game controller. Manages all user-facing routing: login, new account
 * creation, and main menu. Business logic is delegated to service classes
 * (DungeonService, BattleService) and persistence to SaveManager / LoadManager.
 */
public class GameManager {

    private Player player;
    private DungeonService dungeonService;
    private final SaveManager saveManager;
    private final LoadManager loadManager;
    private final Scanner input;

    public GameManager() {
        this.saveManager = new SaveManager();
        this.loadManager = new LoadManager();
        this.input = new Scanner(System.in);
    }

    // -------------------------------------------------------------------------
    // Entry Point
    // -------------------------------------------------------------------------
    public void run() {
        System.out.println("==========================================");
        System.out.println("       WELCOME TO TURN-BASED RPG");
        System.out.println("==========================================");
        showLoginMenu();
    }

    // -------------------------------------------------------------------------
    // Login / Account Creation
    // -------------------------------------------------------------------------
    private void showLoginMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n1. Login");
            System.out.println("2. Create New Account");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> {
                    if (handleLogin()) {
                        showMainMenu();
                    }
                }
                case 2 -> {
                    handleNewAccount();
                    showMainMenu();
                }
                case 3 ->
                    running = false;
                default ->
                    System.out.println("Invalid choice!");
            }
        }
        System.out.println("Goodbye!");
    }

    private boolean handleLogin() {
        System.out.print("Enter username: ");
        String name = readLine();
        System.out.print("Enter password: ");
        String password = readLine();

        player = loadManager.load(name, password);
        if (player == null) {
            System.out.println("Login failed. Username or password incorrect.");
            return false;
        }
        System.out.println("Welcome back, " + player.getCharacterName() + "!");
        dungeonService = new DungeonService(player);
        return true;
    }

    private void handleNewAccount() {
        System.out.println("=== CREATE NEW ACCOUNT ===");
        System.out.print("Enter character name: ");
        String name = readLine();

        System.out.print("Set password: ");
        String password = readLine();

        Role role = chooseRole();

        player = new Player(name, role);
        player.setPassword(password);

        // Persist immediately so the player gets a DB id
        saveManager.save(player);

        GameLogger.info("New account created: " + name + " [" + role + "]");
        System.out.println("Account created! Welcome, " + name + "!");
        dungeonService = new DungeonService(player);
    }

    private Role chooseRole() {
        Role role = null;
        while (role == null) {
            System.out.println("\n=== CHOOSE YOUR ROLE ===");
            System.out.println("1. " + Role.WARRIOR.getDisplayName() + " - " + Role.WARRIOR.getDescription());
            System.out.println("2. " + Role.MAGE.getDisplayName() + " - " + Role.MAGE.getDescription());
            System.out.println("3. " + Role.ARCHER.getDisplayName() + " - " + Role.ARCHER.getDescription());
            System.out.print("Choose: ");

            switch (readInt()) {
                case 1 ->
                    role = Role.WARRIOR;
                case 2 ->
                    role = Role.MAGE;
                case 3 ->
                    role = Role.ARCHER;
                default ->
                    System.out.println("Invalid choice, try again.");
            }
        }
        return role;
    }

    // -------------------------------------------------------------------------
    // Main Menu
    // -------------------------------------------------------------------------
    public void showMainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n==================== MAIN MENU ====================");
            System.out.println("1. Enter Dungeon");
            System.out.println("2. Save Game");
            System.out.println("3. View Inventory");
            System.out.println("4. View Character Info");
            System.out.println("5. Logout");
            System.out.print("Choose: ");

            int choice = readInt();
            switch (choice) {
                case 1 ->
                    dungeonService.attackDungeon(input);
                case 2 ->
                    saveManager.save(player);
                case 3 ->
                    player.getInventory().showInventory();
                case 4 -> {
                    player.getPlayerDetail();
                    player.getStatsDetail();
                }
                case 5 -> {
                    System.out.println("Logging out...");
                    running = false;
                }
                default ->
                    System.out.println("Invalid choice!");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Input Helpers
    // -------------------------------------------------------------------------
    private int readInt() {
        while (!input.hasNextInt()) {
            System.out.println("Please enter a number.");
            input.next();
        }
        int val = input.nextInt();
        input.nextLine(); // consume newline
        return val;
    }

    private String readLine() {
        String line = "";
        while (line.isBlank()) {
            line = input.nextLine().trim();
        }
        return line;
    }
}
