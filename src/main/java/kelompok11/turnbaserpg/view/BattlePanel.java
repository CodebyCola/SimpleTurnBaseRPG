package kelompok11.turnbaserpg.view;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;

import kelompok11.turnbaserpg.enums.BattleResult;
import kelompok11.turnbaserpg.game.controller.BattleController;
import kelompok11.turnbaserpg.game.services.BattleEvent;
import kelompok11.turnbaserpg.model.character.Enemy;
import kelompok11.turnbaserpg.model.character.InventorySlot;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.skill.Skill;

/**
 * BattlePanel — tampilan pertarungan player vs enemy.
 *
 * Cara pakai:
 *   battlePanel.startBattle(player, enemy);
 *   // Ketika selesai, onBattleEnd.run() dipanggil
 *   // Ambil hasil: battlePanel.getLastResult()
 */
public class BattlePanel extends JPanel {

    private Player player;
    private Enemy  enemy;
    private BattleController battleController;
    private BattleResult lastResult;

    // Callback saat battle selesai
    private final Runnable onBattleEnd;

    // Stat bars — VS header
    private RPGComponents.StatBar playerHpBar, playerMpBar, enemyHpBar;
    private JLabel playerNameLbl, playerLevelLbl, enemyNameLbl, enemyHpLabel;

    // Battle log
    private JTextPane battleLog;
    private StyledDocument logDoc;

    // Action buttons
    private RPGComponents.RPGButton attackBtn, defendBtn, skillBtn, itemBtn, escapeBtn;
    private JPanel skillSubPanel, itemSubPanel;
    private boolean skillSubVisible = false;
    private boolean itemSubVisible  = false;

    public BattlePanel(Runnable onBattleEnd) {
        this.onBattleEnd = onBattleEnd;
        setLayout(new BorderLayout(0, 0));
        setBackground(RPGTheme.BG_DARKEST);
        buildUI();
    }

    // ======================================================
    // Start / Reset battle
    // ======================================================
    public void startBattle(Player player, Enemy enemy) {
        this.player     = player;
        this.enemy      = enemy;
        this.lastResult = null;

        // Reset UI
        clearLog();
        skillSubVisible = false;
        itemSubVisible  = false;
        skillSubPanel.setVisible(false);
        itemSubPanel.setVisible(false);
        setActionsEnabled(true);
        refreshPlayerStats();
        refreshEnemyStats();

        // Wire controller
        battleController = new BattleController(player, enemy, this::handleEvents);
        battleController.startBattle();
    }

    public BattleResult getLastResult() { return lastResult; }

    // ======================================================
    // Event handler dari BattleController
    // ======================================================
    private void handleEvents(List<BattleEvent> events) {
        SwingUtilities.invokeLater(() -> {
            for (BattleEvent e : events) appendLog(e);
            refreshPlayerStats();
            refreshEnemyStats();

            if (battleController != null && battleController.isBattleOver()) {
                lastResult = battleController.getResult();
                setActionsEnabled(false);
                // Jeda agar player membaca log, lalu panggil callback
                Timer t = new Timer(1800, ev -> {
                    if (onBattleEnd != null) onBattleEnd.run();
                });
                t.setRepeats(false);
                t.start();
            }
        });
    }

    // ======================================================
    // UI Building
    // ======================================================
    private void buildUI() {
        add(buildVsBar(),      BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            buildLogPanel(), buildActionsPanel());
        split.setResizeWeight(0.62);
        split.setDividerSize(4);
        split.setBorder(null);
        split.setBackground(RPGTheme.BG_DARKEST);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildVsBar() {
        JPanel bar = new JPanel(new GridLayout(1, 3, 10, 0));
        bar.setBackground(RPGTheme.BG_DARK);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, RPGTheme.BORDER_GOLD),
            BorderFactory.createEmptyBorder(14, 20, 14, 20)
        ));

        // --- Player side ---
        JPanel playerSide = new JPanel();
        playerSide.setOpaque(false);
        playerSide.setLayout(new BoxLayout(playerSide, BoxLayout.Y_AXIS));

        playerNameLbl = new JLabel("Player");
        playerNameLbl.setFont(RPGTheme.FONT_SUB);
        playerNameLbl.setForeground(RPGTheme.ACCENT_GOLD);

        playerLevelLbl = new JLabel("Lv. 1");
        playerLevelLbl.setFont(RPGTheme.FONT_SMALL);
        playerLevelLbl.setForeground(RPGTheme.TEXT_SECONDARY);

        playerHpBar = new RPGComponents.StatBar("HP", 100, 100, RPGTheme.HP_RED);
        playerHpBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        playerMpBar = new RPGComponents.StatBar("MP", 30,  30,  RPGTheme.MANA_BLUE);
        playerMpBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));

        playerSide.add(playerNameLbl);
        playerSide.add(playerLevelLbl);
        playerSide.add(Box.createVerticalStrut(6));
        playerSide.add(playerHpBar);
        playerSide.add(Box.createVerticalStrut(4));
        playerSide.add(playerMpBar);

        // --- VS ---
        JLabel vs = new JLabel("VS", SwingConstants.CENTER);
        vs.setFont(new Font("Georgia", Font.BOLD, 26));
        vs.setForeground(RPGTheme.ACCENT_EMBER);

        // --- Enemy side ---
        JPanel enemySide = new JPanel();
        enemySide.setOpaque(false);
        enemySide.setLayout(new BoxLayout(enemySide, BoxLayout.Y_AXIS));

        enemyNameLbl = new JLabel("Enemy", SwingConstants.RIGHT);
        enemyNameLbl.setFont(RPGTheme.FONT_SUB);
        enemyNameLbl.setForeground(RPGTheme.HP_RED);
        enemyNameLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);

        enemyHpLabel = new JLabel("HP: 0/0", SwingConstants.RIGHT);
        enemyHpLabel.setFont(RPGTheme.FONT_SMALL);
        enemyHpLabel.setForeground(RPGTheme.TEXT_SECONDARY);
        enemyHpLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        enemyHpBar = new RPGComponents.StatBar("HP", 100, 100, RPGTheme.ACCENT_EMBER);
        enemyHpBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));

        enemySide.add(enemyNameLbl);
        enemySide.add(enemyHpLabel);
        enemySide.add(Box.createVerticalStrut(6));
        enemySide.add(enemyHpBar);

        bar.add(playerSide);
        bar.add(vs);
        bar.add(enemySide);
        return bar;
    }

    private JPanel buildLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(RPGTheme.BG_DARKEST);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 6));

        JLabel title = RPGComponents.goldLabel("BATTLE LOG", RPGTheme.FONT_SUB);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        panel.add(title, BorderLayout.NORTH);

        battleLog = new JTextPane();
        battleLog.setEditable(false);
        battleLog.setBackground(RPGTheme.BG_DARK);
        battleLog.setForeground(RPGTheme.TEXT_PRIMARY);
        battleLog.setFont(RPGTheme.FONT_BATTLE);
        battleLog.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        logDoc = battleLog.getStyledDocument();

        JScrollPane scroll = new JScrollPane(battleLog);
        scroll.setBorder(BorderFactory.createLineBorder(RPGTheme.BORDER_DARK, 1));
        scroll.setBackground(RPGTheme.BG_DARK);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildActionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(new Color(0x0D, 0x11, 0x1A));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 6, 12, 12));

        JLabel title = RPGComponents.goldLabel("ACTIONS", RPGTheme.FONT_SUB);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));

        attackBtn = makeActionBtn("⚔  Basic Attack",  RPGTheme.ACCENT_EMBER);
        defendBtn = makeActionBtn("🛡  Defend",        RPGTheme.ACCENT_SILVER);
        skillBtn  = makeActionBtn("✨  Use Skill  ▾",  RPGTheme.EXP_PURPLE);
        itemBtn   = makeActionBtn("🎒  Use Item  ▾",   RPGTheme.HP_GREEN);
        escapeBtn = makeActionBtn("💨  Escape",        RPGTheme.TEXT_SECONDARY);

        // Sub panels
        skillSubPanel = new JPanel();
        skillSubPanel.setOpaque(false);
        skillSubPanel.setLayout(new BoxLayout(skillSubPanel, BoxLayout.Y_AXIS));
        skillSubPanel.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, RPGTheme.EXP_PURPLE));
        skillSubPanel.setVisible(false);

        itemSubPanel = new JPanel();
        itemSubPanel.setOpaque(false);
        itemSubPanel.setLayout(new BoxLayout(itemSubPanel, BoxLayout.Y_AXIS));
        itemSubPanel.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, RPGTheme.HP_GREEN));
        itemSubPanel.setVisible(false);

        attackBtn.addActionListener(e -> doAction(1, -1));
        defendBtn.addActionListener(e -> doAction(2, -1));

        skillBtn.addActionListener(e -> {
            skillSubVisible = !skillSubVisible;
            itemSubVisible  = false;
            itemSubPanel.setVisible(false);
            if (skillSubVisible) populateSkillPanel();
            skillSubPanel.setVisible(skillSubVisible);
            revalidate(); repaint();
        });

        itemBtn.addActionListener(e -> {
            itemSubVisible  = !itemSubVisible;
            skillSubVisible = false;
            skillSubPanel.setVisible(false);
            if (itemSubVisible) populateItemPanel();
            itemSubPanel.setVisible(itemSubVisible);
            revalidate(); repaint();
        });

        escapeBtn.addActionListener(e -> doAction(5, -1));

        actions.add(attackBtn);
        actions.add(Box.createVerticalStrut(6));
        actions.add(defendBtn);
        actions.add(Box.createVerticalStrut(6));
        actions.add(skillBtn);
        actions.add(skillSubPanel);
        actions.add(Box.createVerticalStrut(6));
        actions.add(itemBtn);
        actions.add(itemSubPanel);
        actions.add(Box.createVerticalStrut(14));

        JSeparator sep = new JSeparator();
        sep.setForeground(RPGTheme.BORDER_DARK);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        actions.add(sep);
        actions.add(Box.createVerticalStrut(10));
        actions.add(escapeBtn);

        JScrollPane scroll = new JScrollPane(actions);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private RPGComponents.RPGButton makeActionBtn(String text, Color color) {
        RPGComponents.RPGButton btn = new RPGComponents.RPGButton(text, color, RPGTheme.BG_DARK);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    // ======================================================
    // Populate skill / item sub-panels
    // ======================================================
    private void populateSkillPanel() {
        skillSubPanel.removeAll();
        if (player == null) return;

        List<Skill> skills = player.getUnlockedSkills();
        if (skills.isEmpty()) {
            skillSubPanel.add(disabledLabel("  Belum ada skill"));
        } else {
            for (int i = 0; i < skills.size(); i++) {
                Skill sk = skills.get(i);
                boolean onCd   = sk.getCurrentCooldown() > 0;
                boolean noMana = player.getStats().getCurrentMana() < sk.getManaCost();
                String cdText  = onCd ? "  [CD:" + sk.getCurrentCooldown() + "]" : "";
                String txt = "  " + sk.getName()
                    + "  (MP:" + sk.getManaCost() + ")" + cdText;

                final int idx = i;
                RPGComponents.RPGButton b = makeSubBtn(txt, RPGTheme.EXP_PURPLE);
                b.setEnabled(!onCd && !noMana);
                b.addActionListener(ev -> {
                    skillSubPanel.setVisible(false);
                    skillSubVisible = false;
                    doAction(3, idx);
                });
                skillSubPanel.add(b);
                skillSubPanel.add(Box.createVerticalStrut(3));
            }
        }
        skillSubPanel.revalidate();
        skillSubPanel.repaint();
    }

    private void populateItemPanel() {
        itemSubPanel.removeAll();
        if (player == null) return;

        List<InventorySlot> slots = player.getInventory().getSlots();
        if (slots.isEmpty()) {
            itemSubPanel.add(disabledLabel("  Inventory kosong"));
        } else {
            for (int i = 0; i < slots.size(); i++) {
                InventorySlot slot = slots.get(i);
                String txt = "  " + slot.getItem().getName() + "  x" + slot.getQuantity();
                final int idx = i;
                RPGComponents.RPGButton b = makeSubBtn(txt, RPGTheme.HP_GREEN);
                b.addActionListener(ev -> {
                    itemSubPanel.setVisible(false);
                    itemSubVisible = false;
                    doAction(4, idx);
                });
                itemSubPanel.add(b);
                itemSubPanel.add(Box.createVerticalStrut(3));
            }
        }
        itemSubPanel.revalidate();
        itemSubPanel.repaint();
    }

    private RPGComponents.RPGButton makeSubBtn(String text, Color color) {
        RPGComponents.RPGButton b = new RPGComponents.RPGButton(text, color, RPGTheme.BG_DARKEST);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setFont(RPGTheme.FONT_SMALL);
        return b;
    }

    private JLabel disabledLabel(String text) {
        JLabel l = RPGComponents.label(text, RPGTheme.TEXT_DISABLED, RPGTheme.FONT_SMALL);
        l.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        return l;
    }

    // ======================================================
    // Action dispatch
    // ======================================================
    private void doAction(int action, int secondary) {
        if (battleController == null || battleController.isBattleOver()) return;
        setActionsEnabled(false);

        SwingWorker<Void, Void> w = new SwingWorker<>() {
            @Override protected Void doInBackground() {
                battleController.handleAction(action, secondary);
                return null;
            }
            @Override protected void done() {
                if (battleController != null && !battleController.isBattleOver()) {
                    setActionsEnabled(true);
                }
            }
        };
        w.execute();
    }

    private void setActionsEnabled(boolean enabled) {
        attackBtn.setEnabled(enabled);
        defendBtn.setEnabled(enabled);
        skillBtn.setEnabled(enabled);
        itemBtn.setEnabled(enabled);
        escapeBtn.setEnabled(enabled);
    }

    // ======================================================
    // Stat refresh
    // ======================================================
    private void refreshPlayerStats() {
        if (player == null) return;
        playerNameLbl.setText(player.getCharacterName());
        playerLevelLbl.setText("Lv. " + player.getLevel()
            + "  [" + player.getRole().getDisplayName() + "]");
        playerHpBar.setValues(player.getStats().getCurrentHP(),  player.getStats().getMaxHP());
        playerMpBar.setValues(player.getStats().getCurrentMana(), player.getStats().getBaseMana());
    }

    private void refreshEnemyStats() {
        if (enemy == null) return;
        enemyNameLbl.setText(enemy.getCharacterName());
        int cur = enemy.getStats().getCurrentHP();
        int max = enemy.getStats().getMaxHP();
        enemyHpLabel.setText("HP: " + cur + "/" + max);
        enemyHpBar.setValues(cur, max);
    }

    // ======================================================
    // Battle log
    // ======================================================
    private void appendLog(BattleEvent event) {
        try {
            Style style = logDoc.addStyle("s", null);
            StyleConstants.setForeground(style, colorFor(event.getType()));
            StyleConstants.setBold(style, isBold(event.getType()));
            logDoc.insertString(logDoc.getLength(), event.getMessage() + "\n", style);
            battleLog.setCaretPosition(logDoc.getLength());
        } catch (BadLocationException e) { /* ignore */ }
    }

    private void clearLog() {
        try { logDoc.remove(0, logDoc.getLength()); }
        catch (BadLocationException e) { /* ignore */ }
    }

    private Color colorFor(BattleEvent.Type type) {
        return switch (type) {
            case BATTLE_START   -> RPGTheme.ACCENT_GOLD;
            case PLAYER_STATUS  -> RPGTheme.ACCENT_SILVER;
            case ACTION_RESULT  -> RPGTheme.TEXT_PRIMARY;
            case ENEMY_TURN     -> RPGTheme.HP_RED;
            case SKILL_CAST     -> RPGTheme.EXP_PURPLE;
            case ITEM_USED      -> RPGTheme.HP_GREEN;
            case ESCAPE_SUCCESS -> RPGTheme.HP_GREEN;
            case ESCAPE_FAILED  -> RPGTheme.ACCENT_EMBER;
            case BATTLE_END     -> RPGTheme.ACCENT_GOLD;
            case ERROR          -> RPGTheme.ACCENT_EMBER;
            default             -> RPGTheme.TEXT_PRIMARY;
        };
    }

    private boolean isBold(BattleEvent.Type type) {
        return type == BattleEvent.Type.BATTLE_START || type == BattleEvent.Type.BATTLE_END;
    }
}
