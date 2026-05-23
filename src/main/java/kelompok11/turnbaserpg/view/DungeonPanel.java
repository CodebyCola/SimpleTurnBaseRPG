package kelompok11.turnbaserpg.view;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;

import kelompok11.turnbaserpg.enums.BattleResult;
import kelompok11.turnbaserpg.enums.Difficulty;
import kelompok11.turnbaserpg.game.controller.BattleController;
import kelompok11.turnbaserpg.game.controller.GameController;
import kelompok11.turnbaserpg.game.services.BattleEvent;
import kelompok11.turnbaserpg.game.services.DungeonEvent;
import kelompok11.turnbaserpg.game.services.DungeonService;
import kelompok11.turnbaserpg.model.character.Enemy;
import kelompok11.turnbaserpg.model.character.Player;

/**
 * DungeonPanel — mengelola dungeon loop secara manual di background thread.
 *
 * Arsitektur yang diperbaiki:
 * - DungeonService dipakai LANGSUNG (bukan DungeonController) agar kita
 *   bisa kontrol kapan battle dimulai dan BattlePanel mendapat Enemy-nya.
 * - BattlePanel ditampilkan dengan enemy yang benar setiap wave.
 * - Advance prompt menggunakan Object.wait()/notify() di background thread.
 */
public class DungeonPanel extends JPanel {

    private final GameController gameController;
    private final Player player;
    private final Runnable onDungeonEnd;

    // Sub-panels via CardLayout
    private JPanel cardHolder;
    private static final String CARD_LOG    = "log";
    private static final String CARD_BATTLE = "battle";

    // Dungeon log
    private JTextPane dungeonLog;
    private StyledDocument logDoc;

    // Battle panel (reused per fight)
    private BattlePanel battlePanel;

    // Status bar
    private JLabel floorLabel;
    private RPGComponents.StatBar hpBar, mpBar;

    // Advance bar
    private JPanel advanceBar;
    private RPGComponents.RPGButton advanceBtn, retreatBtn;
    private JLabel advanceLabel;

    // Synchronization for advance decision
    private final Object advanceLock = new Object();
    private volatile boolean advanceDecision = false;
    private volatile boolean advanceAnswered = false;

    // Synchronization for battle completion
    private final Object battleLock = new Object();
    private volatile boolean battleDone = false;

    // Current DungeonService
    private DungeonService dungeonService;

    public DungeonPanel(GameController gameController, Player player, Runnable onDungeonEnd) {
        this.gameController = gameController;
        this.player         = player;
        this.onDungeonEnd   = onDungeonEnd;
        setLayout(new BorderLayout(0, 0));
        setBackground(RPGTheme.BG_DARKEST);
        buildUI();
    }

    // ======================================================
    // Build UI
    // ======================================================
    private void buildUI() {
        add(buildStatusBar(), BorderLayout.NORTH);

        cardHolder = new JPanel(new CardLayout());
        cardHolder.setBackground(RPGTheme.BG_DARKEST);
        cardHolder.add(buildLogCard(), CARD_LOG);

        // BattlePanel: onBattleEnd dipanggil saat battle selesai
        battlePanel = new BattlePanel(() -> {
            // Battle selesai — beri tahu dungeon thread
            synchronized (battleLock) {
                battleDone = true;
                battleLock.notifyAll();
            }
        });
        cardHolder.add(battlePanel, CARD_BATTLE);

        add(cardHolder, BorderLayout.CENTER);

        advanceBar = buildAdvanceBar();
        advanceBar.setVisible(false);
        add(advanceBar, BorderLayout.SOUTH);
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setBackground(RPGTheme.BG_DARK);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, RPGTheme.BORDER_GOLD),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        floorLabel = RPGComponents.goldLabel("DUNGEON — Floor 1", RPGTheme.FONT_SUB);
        bar.add(floorLabel, BorderLayout.WEST);

        JPanel statPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        statPanel.setOpaque(false);
        hpBar = new RPGComponents.StatBar("HP", 100, 100, RPGTheme.HP_RED);
        hpBar.setPreferredSize(new Dimension(160, 20));
        mpBar = new RPGComponents.StatBar("MP", 30,  30,  RPGTheme.MANA_BLUE);
        mpBar.setPreferredSize(new Dimension(160, 20));
        statPanel.add(hpBar);
        statPanel.add(mpBar);
        bar.add(statPanel, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildLogCard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(RPGTheme.BG_DARKEST);
        p.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel title = RPGComponents.goldLabel("DUNGEON LOG", RPGTheme.FONT_SUB);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        p.add(title, BorderLayout.NORTH);

        dungeonLog = new JTextPane();
        dungeonLog.setEditable(false);
        dungeonLog.setBackground(RPGTheme.BG_DARK);
        dungeonLog.setFont(RPGTheme.FONT_BATTLE);
        dungeonLog.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        logDoc = dungeonLog.getStyledDocument();

        JScrollPane scroll = new JScrollPane(dungeonLog);
        scroll.setBorder(BorderFactory.createLineBorder(RPGTheme.BORDER_DARK, 1));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildAdvanceBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setBackground(RPGTheme.BG_DARK);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, RPGTheme.ACCENT_GOLD),
            BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));
        advanceLabel = RPGComponents.label(
            "Floor cleared! Advance to the next floor?",
            RPGTheme.ACCENT_GOLD, RPGTheme.FONT_BODY_BOLD);
        bar.add(advanceLabel, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);
        retreatBtn = new RPGComponents.RPGButton("⬅  Retreat", RPGTheme.ACCENT_EMBER, RPGTheme.BG_DARK);
        advanceBtn = new RPGComponents.RPGButton("Advance ➡", RPGTheme.ACCENT_GOLD,  RPGTheme.BG_DARK);
        retreatBtn.setPreferredSize(new Dimension(130, 38));
        advanceBtn.setPreferredSize(new Dimension(130, 38));

        retreatBtn.addActionListener(e -> respondAdvance(false));
        advanceBtn.addActionListener(e -> respondAdvance(true));

        btnRow.add(retreatBtn);
        btnRow.add(advanceBtn);
        bar.add(btnRow, BorderLayout.EAST);
        return bar;
    }

    // ======================================================
    // Start dungeon loop
    // ======================================================
    public void startDungeon() {
        dungeonService = new DungeonService(player);
        dungeonService.initDungeon();
        refreshStatusBar();
        appendLog("=== Memasuki Dungeon ===", RPGTheme.ACCENT_GOLD, true);

        Thread t = new Thread(this::dungeonLoop, "DungeonThread");
        t.setDaemon(true);
        t.start();
    }

    // ======================================================
    // Dungeon loop (runs on background thread)
    // ======================================================
    private void dungeonLoop() {
        while (dungeonService.hasMoreFloors()) {
            int floor = dungeonService.getCurrentFloor();
            boolean isBoss = dungeonService.isBossFloor(floor);
            Difficulty diff = dungeonService.determineDifficulty(floor);

            // ---- Floor start ----
            dispatchDungeonEvents(dungeonService.buildFloorStartEvents(floor, isBoss, diff));

            // ---- Wave loop ----
            boolean floorCleared = runFloor(floor, isBoss, diff);

            if (!floorCleared) {
                // Player kalah atau kabur
                appendLog("\nGame Over! Kembali ke menu utama...", RPGTheme.HP_RED, true);
                sleep(2000);
                SwingUtilities.invokeLater(this::endDungeonSession);
                return;
            }

            // ---- Skill reward ----
            dispatchDungeonEvents(dungeonService.applySkillReward(floor));

            // ---- Advance floor ----
            List<DungeonEvent> advEvents = dungeonService.advanceFloor();
            dispatchDungeonEvents(advEvents);

            // Cek dungeon complete
            boolean complete = advEvents.stream()
                .anyMatch(e -> e.getType() == DungeonEvent.Type.DUNGEON_COMPLETE);
            if (complete) {
                sleep(3000);
                SwingUtilities.invokeLater(this::endDungeonSession);
                return;
            }

            if (!dungeonService.hasMoreFloors()) break;

            // ---- Tanya mau lanjut? ----
            askAdvance(dungeonService.getCurrentFloor());
            if (!advanceDecision) {
                appendLog("\nAnda memilih untuk mundur. Sampai jumpa!", RPGTheme.ACCENT_SILVER, false);
                sleep(1000);
                SwingUtilities.invokeLater(this::endDungeonSession);
                return;
            }
        }
    }

    private boolean runFloor(int floor, boolean isBoss, Difficulty diff) {
        int totalWaves = dungeonService.wavesForFloor(isBoss);

        for (int wave = 1; wave <= totalWaves; wave++) {
            // Generate enemy
            Enemy enemy = isBoss
                ? dungeonService.generateBossEnemy(diff)
                : dungeonService.generateEnemy(diff);
            dungeonService.scaleEnemyStats(enemy, diff, isBoss);

            // Log wave start
            dispatchDungeonEvents(dungeonService.buildWaveStartEvents(wave, totalWaves, enemy, isBoss));

            // ---- Mulai battle di BattlePanel ----
            BattleResult result = runBattle(enemy);

            // Proses hasil battle
            DungeonService.FloorOutcome outcome = dungeonService.processBattleResult(result);
            dispatchDungeonEvents(outcome.getEvents());

            if (!outcome.isWaveCleared()) {
                return false;
            }

            // Jeda sebentar antar wave
            if (wave < totalWaves) {
                sleep(600);
            }
        }

        dispatchDungeonEvents(List.of(
            new DungeonEvent(DungeonEvent.Type.FLOOR_CLEARED,
                "✓ Floor " + floor + " cleared!")
        ));
        return true;
    }

    /**
     * Jalankan satu battle:
     * 1. Switch ke BattlePanel dengan enemy yang benar
     * 2. Tunggu BattlePanel selesai (via battleLock)
     * 3. Return hasil
     */
    private BattleResult runBattle(Enemy enemy) {
        // Reset flag
        synchronized (battleLock) {
            battleDone = false;
        }

        // Start battle di EDT
        SwingUtilities.invokeLater(() -> {
            showCard(CARD_BATTLE);
            battlePanel.startBattle(player, enemy);
        });

        // Block background thread sampai battle selesai
        synchronized (battleLock) {
            while (!battleDone) {
                try { battleLock.wait(); }
                catch (InterruptedException ie) { Thread.currentThread().interrupt(); break; }
            }
        }

        // Ambil hasil dari BattlePanel
        BattleResult result = battlePanel.getLastResult();

        // Kembali ke log card
        SwingUtilities.invokeLater(() -> showCard(CARD_LOG));
        sleep(300);

        return result != null ? result : BattleResult.LOSE;
    }

    // ======================================================
    // Advance prompt (blocks background thread)
    // ======================================================
    private void askAdvance(int nextFloor) {
        SwingUtilities.invokeLater(() -> {
            advanceLabel.setText("Floor " + (nextFloor - 1)
                + " cleared!  Lanjut ke floor " + nextFloor + "?");
            advanceBar.setVisible(true);
            advanceBar.revalidate();
            advanceBar.repaint();
            refreshStatusBar();
        });

        synchronized (advanceLock) {
            advanceAnswered = false;
            while (!advanceAnswered) {
                try { advanceLock.wait(); }
                catch (InterruptedException ie) { Thread.currentThread().interrupt(); break; }
            }
        }

        SwingUtilities.invokeLater(() -> advanceBar.setVisible(false));
    }

    private void respondAdvance(boolean advance) {
        synchronized (advanceLock) {
            advanceDecision = advance;
            advanceAnswered = true;
            advanceLock.notifyAll();
        }
    }

    // ======================================================
    // Helpers
    // ======================================================
    private void showCard(String name) {
        ((CardLayout) cardHolder.getLayout()).show(cardHolder, name);
    }

    private void refreshStatusBar() {
        int floor = dungeonService != null
            ? dungeonService.getCurrentFloor()
            : player.getCurrentFloor();
        SwingUtilities.invokeLater(() -> {
            floorLabel.setText("DUNGEON — Floor " + floor + " / 100");
            hpBar.setValues(player.getStats().getCurrentHP(), player.getStats().getMaxHP());
            mpBar.setValues(player.getStats().getCurrentMana(), player.getStats().getBaseMana());
        });
    }

    private void endDungeonSession() {
        gameController.onDungeonSessionEnded();
        if (onDungeonEnd != null) onDungeonEnd.run();
    }

    private void dispatchDungeonEvents(List<DungeonEvent> events) {
        for (DungeonEvent e : events) {
            Color c = colorForType(e.getType());
            boolean bold = e.getType() == DungeonEvent.Type.FLOOR_START
                || e.getType() == DungeonEvent.Type.DUNGEON_COMPLETE
                || e.getType() == DungeonEvent.Type.SKILL_UNLOCKED
                || e.getType() == DungeonEvent.Type.BOSS_APPEAR
                || e.getType() == DungeonEvent.Type.FLOOR_CLEARED;
            appendLog(e.getMessage(), c, bold);
        }
        refreshStatusBar();
    }

    private void appendLog(String text, Color color, boolean bold) {
        SwingUtilities.invokeLater(() -> {
            try {
                Style s = logDoc.addStyle("s", null);
                StyleConstants.setForeground(s, color);
                StyleConstants.setBold(s, bold);
                logDoc.insertString(logDoc.getLength(), text + "\n", s);
                dungeonLog.setCaretPosition(logDoc.getLength());
            } catch (BadLocationException ex) { /* ignore */ }
        });
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
    }

    private Color colorForType(DungeonEvent.Type type) {
        return switch (type) {
            case FLOOR_START      -> RPGTheme.ACCENT_GOLD;
            case WAVE_START       -> RPGTheme.TEXT_PRIMARY;
            case BOSS_APPEAR      -> RPGTheme.ACCENT_EMBER;
            case FLOOR_CLEARED    -> RPGTheme.HP_GREEN;
            case SKILL_UNLOCKED   -> RPGTheme.EXP_PURPLE;
            case DUNGEON_COMPLETE -> RPGTheme.ACCENT_GOLD;
            case PLAYER_DEFEATED  -> RPGTheme.HP_RED;
            case PLAYER_ESCAPED   -> RPGTheme.ACCENT_SILVER;
            case BATTLE_RESULT    -> RPGTheme.ACCENT_SILVER;
            default               -> RPGTheme.TEXT_PRIMARY;
        };
    }
}
