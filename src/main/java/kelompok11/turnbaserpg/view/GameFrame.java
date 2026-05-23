package kelompok11.turnbaserpg.view;

import javax.swing.*;
import java.awt.*;

import kelompok11.turnbaserpg.game.controller.GameController;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.enums.Role;

/**
 * Root window — CardLayout navigator.
 * Login → MainMenu → Dungeon (→ Battle di dalam Dungeon)
 */
public class GameFrame extends JFrame {

    private static final String CARD_LOGIN    = "login";
    private static final String CARD_MAINMENU = "mainmenu";
    private static final String CARD_DUNGEON  = "dungeon";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     root       = new JPanel(cardLayout);
    private final GameController gameController;

    private LoginPanel    loginPanel;
    private MainMenuPanel mainMenuPanel;
    private DungeonPanel  dungeonPanel;

    public GameFrame() {
        setTitle("Dungeon Realm — Turn-Based RPG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 700);
        setMinimumSize(new Dimension(820, 580));
        setLocationRelativeTo(null);

        applyGlobalDefaults();

        gameController = new GameController(state -> { /* navigation driven manually */ });

        root.setBackground(RPGTheme.BG_DARKEST);
        setContentPane(root);

        buildLoginPanel();
        root.add(loginPanel, CARD_LOGIN);
        cardLayout.show(root, CARD_LOGIN);

        setVisible(true);
    }

    // ======================================================
    // Panel builders
    // ======================================================
    private void buildLoginPanel() {
        loginPanel = new LoginPanel(
            // onLogin
            (name, password) -> {
                Player player = gameController.login(name, password);
                if (player != null) {
                    loginPanel.setStatus("", Color.WHITE);
                    showMainMenu(player);
                } else {
                    loginPanel.setStatus("Username atau password salah.", RPGTheme.ACCENT_EMBER);
                }
            },
            // onRegister
            (name, password, roleStr) -> {
                try {
                    // roleStr bisa "WARRIOR", "MAGE", "ARCHER" (sudah difilter di LoginPanel)
                    Role role = Role.valueOf(roleStr.toUpperCase().trim());
                    Player player = gameController.register(name, password, role);
                    loginPanel.setStatus("", Color.WHITE);
                    
                    showMainMenu(player);
                } catch (IllegalArgumentException ex) {
                    loginPanel.setStatus("Pilih role yang valid.", RPGTheme.ACCENT_EMBER);
                }
            }
        );
    }

    private void buildMainMenuPanel(Player player) {
        mainMenuPanel = new MainMenuPanel(
            // onEnterDungeon
            () -> showDungeon(gameController.getCurrentPlayer()),
            // onLogout
            () -> {
                gameController.saveGame();
                root.remove(mainMenuPanel);
                if (dungeonPanel != null) { root.remove(dungeonPanel); dungeonPanel = null; }
                mainMenuPanel = null;
                cardLayout.show(root, CARD_LOGIN);
                loginPanel.setStatus("Logout berhasil.", RPGTheme.HP_GREEN);
            }
        );
        mainMenuPanel.setPlayer(player);
    }

    private void buildDungeonPanel(Player player) {
        dungeonPanel = new DungeonPanel(
            gameController,
            player,
            // onDungeonEnd
            () -> SwingUtilities.invokeLater(() -> {
                // Refresh player data setelah dungeon (level up, gold, dll)
                Player p = gameController.getCurrentPlayer();
                if (mainMenuPanel != null && p != null) mainMenuPanel.setPlayer(p);
                cardLayout.show(root, CARD_MAINMENU);
            })
        );
    }

    // ======================================================
    // Navigation
    // ======================================================
    private void showMainMenu(Player player) {
        if (mainMenuPanel == null) {
            buildMainMenuPanel(player);
            root.add(mainMenuPanel, CARD_MAINMENU);
        } else {
            mainMenuPanel.setPlayer(player);
        }
        cardLayout.show(root, CARD_MAINMENU);
    }

    private void showDungeon(Player player) {
        if (player == null) return;
        // Rebuild tiap masuk dungeon agar state reset
        if (dungeonPanel != null) root.remove(dungeonPanel);
        buildDungeonPanel(player);
        root.add(dungeonPanel, CARD_DUNGEON);
        cardLayout.show(root, CARD_DUNGEON);
        SwingUtilities.invokeLater(() -> dungeonPanel.startDungeon());
    }

    // ======================================================
    // Global dark-theme defaults
    // ======================================================
    private void applyGlobalDefaults() {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}

        UIManager.put("Panel.background",              RPGTheme.BG_DARK);
        UIManager.put("Label.foreground",              RPGTheme.TEXT_PRIMARY);
        UIManager.put("TextField.background",          RPGTheme.BG_DARKEST);
        UIManager.put("TextField.foreground",          RPGTheme.TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground",     RPGTheme.ACCENT_GOLD);
        UIManager.put("PasswordField.background",      RPGTheme.BG_DARKEST);
        UIManager.put("PasswordField.foreground",      RPGTheme.TEXT_PRIMARY);
        UIManager.put("ComboBox.background",           RPGTheme.BG_DARKEST);
        UIManager.put("ComboBox.foreground",           RPGTheme.TEXT_PRIMARY);
        UIManager.put("ComboBox.selectionBackground",  RPGTheme.BG_MID);
        UIManager.put("ComboBox.selectionForeground",  RPGTheme.ACCENT_GOLD);
        UIManager.put("List.background",               RPGTheme.BG_DARKEST);
        UIManager.put("List.foreground",               RPGTheme.TEXT_PRIMARY);
        UIManager.put("ScrollBar.thumb",               RPGTheme.BG_LIGHT);
        UIManager.put("ScrollBar.track",               RPGTheme.BG_DARK);
        UIManager.put("Table.background",              RPGTheme.BG_DARKEST);
        UIManager.put("Table.foreground",              RPGTheme.TEXT_PRIMARY);
        UIManager.put("Table.selectionBackground",     RPGTheme.BG_LIGHT);
        UIManager.put("Table.selectionForeground",     RPGTheme.ACCENT_GOLD);
        UIManager.put("TableHeader.background",        RPGTheme.BG_MID);
        UIManager.put("TableHeader.foreground",        RPGTheme.ACCENT_GOLD);
        UIManager.put("SplitPane.background",          RPGTheme.BG_DARKEST);
        UIManager.put("SplitPaneDivider.background",   RPGTheme.BORDER_DARK);
        UIManager.put("OptionPane.background",         RPGTheme.BG_DARK);
        UIManager.put("OptionPane.messageForeground",  RPGTheme.TEXT_PRIMARY);
        UIManager.put("Dialog.background",             RPGTheme.BG_DARK);
    }
}
