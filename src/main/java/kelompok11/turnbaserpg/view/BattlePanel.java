package kelompok11.turnbaserpg.view;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;

import kelompok11.turnbaserpg.model.enums.BattleResult;
import kelompok11.turnbaserpg.game.controller.BattleController;
import kelompok11.turnbaserpg.game.controller.BattleViewController;
import kelompok11.turnbaserpg.game.services.BattleEvent;
import kelompok11.turnbaserpg.model.character.Enemy;
import kelompok11.turnbaserpg.model.character.Player;

public class BattlePanel extends JPanel {

    private BattleController battleController;
    
    private final BattleViewController viewController = new BattleViewController();
    private BattleResult lastResult;

    
    private final Runnable onBattleEnd;

    
    private RPGComponents.StatBar playerHpBar, playerMpBar, enemyHpBar;
    private JLabel playerNameLbl, playerLevelLbl, enemyNameLbl, enemyHpLabel;

    
    private JTextPane battleLog;
    private StyledDocument logDoc;

    
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

    
    
    
    public void startBattle(Player player, Enemy enemy) {
        
        viewController.setContext(player, enemy);
        this.lastResult = null;

        
        clearLog();
        skillSubVisible = false;
        itemSubVisible  = false;
        skillSubPanel.setVisible(false);
        itemSubPanel.setVisible(false);
        setActionsEnabled(true);
        refreshPlayerStats();
        refreshEnemyStats();

        
        battleController = new BattleController(player, enemy, this::handleEvents);
        battleController.startBattle();
    }

    public BattleResult getLastResult() { return lastResult; }

    
    
    
    private void handleEvents(List<BattleEvent> events) {
        SwingUtilities.invokeLater(() -> {
            for (BattleEvent e : events) appendLog(e);
            refreshPlayerStats();
            refreshEnemyStats();

            if (battleController != null && battleController.isBattleOver()) {
                lastResult = battleController.getResult();
                setActionsEnabled(false);
                Timer t = new Timer(1800, ev -> {
                    if (onBattleEnd != null) onBattleEnd.run();
                });
                t.setRepeats(false);
                t.start();
            }
        });
    }

    
    
    
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

        
        JLabel vs = new JLabel("VS", SwingConstants.CENTER);
        vs.setFont(new Font("Georgia", Font.BOLD, 26));
        vs.setForeground(RPGTheme.ACCENT_EMBER);

        
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

        attackBtn = makeActionBtn("  Basic Attack",  RPGTheme.ACCENT_EMBER);
        defendBtn = makeActionBtn("  Defend",        RPGTheme.ACCENT_SILVER);
        skillBtn  = makeActionBtn("  Use Skill ",  RPGTheme.EXP_PURPLE);
        itemBtn   = makeActionBtn("  Use Item ",   RPGTheme.HP_GREEN);
        escapeBtn = makeActionBtn("  Escape",        RPGTheme.TEXT_SECONDARY);

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

    
    
    
    private void populateSkillPanel() {
        skillSubPanel.removeAll();

        List<BattleViewController.SkillInfo> skills = viewController.getSkillInfoList();
        if (skills.isEmpty()) {
            skillSubPanel.add(disabledLabel("  Belum ada skill"));
        } else {
            int currentMana = viewController.getPlayerCurrentMana();
            for (int i = 0; i < skills.size(); i++) {
                BattleViewController.SkillInfo sk = skills.get(i);
                boolean onCd   = sk.currentCooldown() > 0;
                boolean noMana = currentMana < sk.manaCost();
                String cdText  = onCd ? "  [CD:" + sk.currentCooldown() + "]" : "";
                String txt = "  " + sk.name() + "  (MP:" + sk.manaCost() + ")" + cdText;

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

        List<BattleViewController.ItemInfo> items = viewController.getItemInfoList();
        if (items.isEmpty()) {
            itemSubPanel.add(disabledLabel("  Inventory kosong"));
        } else {
            for (int i = 0; i < items.size(); i++) {
                BattleViewController.ItemInfo item = items.get(i);
                String txt = "  " + item.name() + "  x" + item.quantity();
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

    
    
    
    private void refreshPlayerStats() {
        BattleViewController.PlayerBattleSnapshot snap = viewController.getPlayerSnapshot();
        if (snap == null) return;
        playerNameLbl.setText(snap.name());
        playerLevelLbl.setText("Lv. " + snap.level() + "  [" + snap.role() + "]");
        playerHpBar.setValues(snap.currentHp(), snap.maxHp());
        playerMpBar.setValues(snap.currentMana(), snap.maxMana());
    }

    private void refreshEnemyStats() {
        BattleViewController.EnemyBattleSnapshot snap = viewController.getEnemySnapshot();
        if (snap == null) return;
        enemyNameLbl.setText(snap.name());
        enemyHpLabel.setText("HP: " + snap.currentHp() + "/" + snap.maxHp());
        enemyHpBar.setValues(snap.currentHp(), snap.maxHp());
    }

    
    
    
    private void appendLog(BattleEvent event) {
        try {
            Style style = logDoc.addStyle("s", null);
            StyleConstants.setForeground(style, colorFor(event.getType()));
            StyleConstants.setBold(style, isBold(event.getType()));
            logDoc.insertString(logDoc.getLength(), event.getMessage() + "\n", style);
            battleLog.setCaretPosition(logDoc.getLength());
        } catch (BadLocationException e) {  }
    }

    private void clearLog() {
        try { logDoc.remove(0, logDoc.getLength()); }
        catch (BadLocationException e) {  }
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
