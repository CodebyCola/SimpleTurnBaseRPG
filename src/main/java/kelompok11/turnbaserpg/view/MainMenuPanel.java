package kelompok11.turnbaserpg.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import kelompok11.turnbaserpg.game.controller.MainMenuController;
import kelompok11.turnbaserpg.model.character.InventorySlot;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.skill.Skill;

// Main menu — kartu player, navigasi, dan dialog Inventory/Skills.
public class MainMenuPanel extends JPanel {

    // Controller handles all model access
    private MainMenuController controller;

    // Callback
    private final Runnable onEnterDungeon;
    private final Runnable onLogout;

    // Live labels
    private JLabel nameLabel, levelLabel, roleLabel, goldLabel, floorLabel;
    private RPGComponents.StatBar hpBar, manaBar, expBar;

    // Menu buttons that need refreshing
    private RPGComponents.RPGButton dungeonBtn;
    private JLabel deadStatusLabel;

    public MainMenuPanel(Runnable onEnterDungeon, Runnable onLogout) {
        this.onEnterDungeon = onEnterDungeon;
        this.onLogout = onLogout;
        setLayout(new BorderLayout());
        setBackground(RPGTheme.BG_DARKEST);
        buildUI();
    }

    // ---- Update via Player (delegates to controller) ----
    public void setPlayer(Player player) {
        this.controller = new MainMenuController(player);
        refreshUI();
    }

    private void refreshUI() {
        if (controller == null) {
            return;
        }
        MainMenuController.PlayerSnapshot snap = controller.getPlayerSnapshot();
        if (snap == null) {
            return;
        }
        setPlayerData(
                snap.name(), snap.role(), snap.level(),
                snap.currentHp(), snap.maxHp(),
                snap.currentMana(), snap.maxMana(),
                snap.currentExp(), snap.maxExp(),
                snap.gold(), snap.floor()
        );

        // Show dead warning and dim dungeon button when HP = 0
        boolean dead = snap.isDead();
        if (deadStatusLabel != null) {
            deadStatusLabel.setVisible(dead);
        }
        if (dungeonBtn != null) {
            dungeonBtn.setEnabled(!dead);
            dungeonBtn.setForeground(dead ? RPGTheme.TEXT_DISABLED : RPGTheme.ACCENT_GOLD);
        }
    }

    // kept for backward compat
    public void setPlayerData(String name, String role, int level,
            int hp, int maxHp, int mana, int maxMana,
            int exp, int maxExp, int gold, int floor) {
        if (nameLabel != null) {
            nameLabel.setText(name);
        }
        if (levelLabel != null) {
            levelLabel.setText("Level " + level);
        }
        if (roleLabel != null) {
            roleLabel.setText(RPGTheme.roleIcon(role) + "  " + role);
            roleLabel.setForeground(RPGTheme.roleColor(role));
        }
        if (goldLabel != null) {
            goldLabel.setText("  " + gold + " Gold");
        }
        if (floorLabel != null) {
            floorLabel.setText("Floor " + floor + " / 100");
        }
        if (hpBar != null) {
            hpBar.setValues(hp, maxHp);
        }
        if (manaBar != null) {
            manaBar.setValues(mana, maxMana);
        }
        if (expBar != null) {
            expBar.setValues(exp, maxExp);
        }
        repaint();
    }

    // ======================================================
    // Build UI
    // ======================================================
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBg(g);
            }
        };
        root.setBackground(RPGTheme.BG_DARKEST);
        root.setOpaque(true);
        add(root, BorderLayout.CENTER);

        root.add(buildTopBar(), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 0.38;
        gbc.insets = new Insets(0, 0, 0, 20);
        center.add(buildPlayerCard(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.62;
        gbc.insets = new Insets(0, 0, 0, 0);
        center.add(buildMenuPanel(), gbc);

        root.add(center, BorderLayout.CENTER);
        root.add(buildBottomBar(), BorderLayout.SOUTH);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(RPGTheme.ACCENT_GOLD);
                g.fillRect(0, getHeight() - 2, getWidth(), 2);
            }
        };
        bar.setBackground(RPGTheme.BG_DARK);
        bar.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));

        bar.add(RPGComponents.goldLabel("  DUNGEON REALM", RPGTheme.FONT_HEADING),
                BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        goldLabel = RPGComponents.goldLabel("  0 Gold", RPGTheme.FONT_BODY_BOLD);
        right.add(goldLabel);

        RPGComponents.RPGButton logoutBtn = new RPGComponents.RPGButton(
                "Logout", RPGTheme.ACCENT_EMBER, RPGTheme.BG_DARK);
        logoutBtn.setPreferredSize(new Dimension(100, 32));
        logoutBtn.addActionListener(e -> {
            if (onLogout != null) {
                onLogout.run();
            }
        });
        right.add(logoutBtn);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildPlayerCard() {
        RPGComponents.DarkPanel card = new RPGComponents.DarkPanel(
                RPGTheme.BG_DARK, RPGTheme.BORDER_GOLD, 10);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Avatar — reads role from controller snapshot, not directly from player
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                String role = (controller != null && controller.getPlayerSnapshot() != null)
                        ? controller.getPlayerSnapshot().role()
                        : "WARRIOR";
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int s = Math.min(getWidth(), getHeight()) - 4;
                int x = (getWidth() - s) / 2, y = (getHeight() - s) / 2;
                Color rc = RPGTheme.roleColor(role);
                g2.setColor(new Color(rc.getRed(), rc.getGreen(), rc.getBlue(), 50));
                g2.fillOval(x - 6, y - 6, s + 12, s + 12);
                g2.setColor(RPGTheme.BG_DARKEST);
                g2.fillOval(x, y, s, s);
                g2.setColor(rc);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(x, y, s, s);
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
                FontMetrics fm = g2.getFontMetrics();
                String icon = RPGTheme.roleIcon(role);
                g2.drawString(icon, x + (s - fm.stringWidth(icon)) / 2,
                        y + (s + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(90, 90));
        avatar.setMaximumSize(new Dimension(90, 90));
        avatar.setOpaque(false);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameLabel = centeredLabel("Hero", RPGTheme.TEXT_GOLD, RPGTheme.FONT_HEADING);
        roleLabel = centeredLabel("  WARRIOR", RPGTheme.WARRIOR_COLOR, RPGTheme.FONT_SUB);
        levelLabel = centeredLabel("Level 1", RPGTheme.ACCENT_SILVER, RPGTheme.FONT_BODY_BOLD);
        floorLabel = centeredLabel("Floor 1/100", RPGTheme.TEXT_SECONDARY, RPGTheme.FONT_SMALL);

        hpBar = new RPGComponents.StatBar("HP", 100, 100, RPGTheme.HP_RED);
        manaBar = new RPGComponents.StatBar("MP", 30, 30, RPGTheme.MANA_BLUE);
        expBar = new RPGComponents.StatBar("EXP", 0, 500, RPGTheme.EXP_PURPLE);
        hpBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        manaBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        expBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        card.add(avatar);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(3));
        card.add(roleLabel);
        card.add(Box.createVerticalStrut(2));
        card.add(levelLabel);
        card.add(Box.createVerticalStrut(2));
        card.add(floorLabel);
        card.add(Box.createVerticalStrut(14));
        card.add(makeSep());
        card.add(Box.createVerticalStrut(12));
        card.add(hpBar);
        card.add(Box.createVerticalStrut(6));
        card.add(manaBar);
        card.add(Box.createVerticalStrut(6));
        card.add(expBar);
        card.add(Box.createVerticalGlue());
        return card;
    }

    private JPanel buildMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(7, 0, 7, 0);

        JLabel title = RPGComponents.goldLabel("— MAIN MENU —", RPGTheme.FONT_HEADING);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.ipady = 0;
        panel.add(title, gbc);

        gbc.ipady = 12;

        RPGComponents.RPGButton dungeonBtnLocal = makeMenuBtn(
                "   Enter Dungeon", RPGTheme.ACCENT_GOLD);
        dungeonBtnLocal.addActionListener(e -> {
            if (controller != null) {
                MainMenuController.DungeonEntryResult result = controller.canEnterDungeon();
                switch (result) {
                    case PLAYER_DEAD -> {
                        JOptionPane.showMessageDialog(this,
                                "You are dead! Use a potion from your Inventory to recover first.",
                                "Cannot Enter Dungeon", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    case OK -> {
                    } // fall through
                }
            }
            if (onEnterDungeon != null) {
                onEnterDungeon.run();
            }
        });
        this.dungeonBtn = dungeonBtnLocal;
        gbc.gridy = 1;
        panel.add(dungeonBtnLocal, gbc);

        // Dead-status warning label (hidden when alive)
        deadStatusLabel = RPGComponents.label(
                "⚠  You are DEAD — use a potion to recover!", RPGTheme.ACCENT_EMBER, RPGTheme.FONT_SMALL);
        deadStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        deadStatusLabel.setVisible(false);
        gbc.gridy = 2;
        gbc.ipady = 0;
        panel.add(deadStatusLabel, gbc);
        gbc.ipady = 12;

        RPGComponents.RPGButton replayBtn = makeMenuBtn(
                "   Replay Floor", RPGTheme.ACCENT_SILVER);
        replayBtn.addActionListener(e -> showReplayFloorDialog());
        gbc.gridy = 3;
        panel.add(replayBtn, gbc);

        RPGComponents.RPGButton invBtn = makeMenuBtn(
                "   Inventory", RPGTheme.ACCENT_SILVER);
        invBtn.addActionListener(e -> showInventoryDialog());
        gbc.gridy = 4;
        panel.add(invBtn, gbc);

        RPGComponents.RPGButton skillBtn = makeMenuBtn(
                "   Skills", RPGTheme.EXP_PURPLE.brighter());
        skillBtn.addActionListener(e -> showSkillsDialog());
        gbc.gridy = 5;
        panel.add(skillBtn, gbc);

        RPGComponents.RPGButton statsBtn = makeMenuBtn(
                "   Character Stats", RPGTheme.MANA_BLUE.brighter());
        statsBtn.addActionListener(e -> showCharacterStatsDialog());
        gbc.gridy = 6;
        panel.add(statsBtn, gbc);

        gbc.gridy = 7;
        gbc.ipady = 0;
        JLabel sep = RPGComponents.label("────  x  ────", RPGTheme.TEXT_DISABLED, RPGTheme.FONT_SMALL);
        sep.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(sep, gbc);

        gbc.gridy = 8;
        gbc.ipady = 8;
        RPGComponents.RPGButton logoutBtn = makeMenuBtn("←   Logout", RPGTheme.ACCENT_EMBER);
        logoutBtn.addActionListener(e -> {
            if (onLogout != null) {
                onLogout.run();
            }
        });
        panel.add(logoutBtn, gbc);

        gbc.gridy = 9;
        gbc.weighty = 1;
        panel.add(Box.createVerticalGlue(), gbc);
        return panel;
    }

    private RPGComponents.RPGButton makeMenuBtn(String text, Color color) {
        RPGComponents.RPGButton btn = new RPGComponents.RPGButton(text, color, RPGTheme.BG_DARK);
        btn.setPreferredSize(new Dimension(260, 52));
        btn.setFont(RPGTheme.FONT_BODY_BOLD);
        return btn;
    }

    private JPanel buildBottomBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(RPGTheme.BG_DARK);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, RPGTheme.BORDER_DARK),
                BorderFactory.createEmptyBorder(8, 24, 8, 24)
        ));
        bar.add(RPGComponents.label("Dungeon Realm v1.0  •  Kelompok 11",
                RPGTheme.TEXT_DISABLED, RPGTheme.FONT_SMALL), BorderLayout.WEST);
        return bar;
    }

    // ======================================================
    // Character Stats Dialog — data fetched via controller
    // ======================================================
    private void showCharacterStatsDialog() {
        JDialog dialog = makeDialog("  Character Stats");

        if (controller == null || !controller.hasPlayer()) {
            dialog.add(RPGComponents.label("Tidak ada data player.", RPGTheme.ACCENT_EMBER, RPGTheme.FONT_BODY));
            dialog.pack();
            dialog.setVisible(true);
            return;
        }

        // Controller provides all stats — no direct model access here
        MainMenuController.StatsSnapshot s = controller.getCharacterStats();

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(RPGTheme.BG_DARK);
        content.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        // -- Header row: name / role / level --
        JPanel headerRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        headerRow.setOpaque(false);
        JLabel nameLbl = RPGComponents.goldLabel(s.name(), RPGTheme.FONT_HEADING);
        JLabel roleLbl = RPGComponents.label(
                RPGTheme.roleIcon(s.role()) + "  " + s.role(),
                RPGTheme.roleColor(s.role()), RPGTheme.FONT_SUB);
        JLabel lvlLbl = RPGComponents.label("Level " + s.level(), RPGTheme.ACCENT_SILVER, RPGTheme.FONT_BODY_BOLD);
        headerRow.add(nameLbl);
        headerRow.add(roleLbl);
        headerRow.add(lvlLbl);
        content.add(headerRow, BorderLayout.NORTH);

        // -- Stats grid --
        JPanel grid = new JPanel(new GridLayout(0, 2, 24, 6));
        grid.setOpaque(false);

        // HP
        grid.add(statLabel("❤  HP"));
        grid.add(statValue(s.currentHp() + " / " + s.maxHp(), RPGTheme.HP_RED));

        // Attack
        grid.add(statLabel("⚔  Attack"));
        grid.add(statValueWithBonus(s.baseAttack(), s.totalAttack(), RPGTheme.ACCENT_EMBER));

        // Defense
        grid.add(statLabel("🛡  Defense"));
        grid.add(statValueWithBonus(s.baseDefense(), s.totalDefense(), RPGTheme.ACCENT_SILVER));

        // Magic
        grid.add(statLabel("✨  Magic"));
        grid.add(statValueWithBonus(s.baseMagic(), s.totalMagic(), RPGTheme.EXP_PURPLE));

        // Mana
        grid.add(statLabel("💧  Mana"));
        grid.add(statValue(s.currentMana() + " / " + s.baseMana(), RPGTheme.MANA_BLUE));

        // EXP
        grid.add(statLabel("⭐  EXP"));
        grid.add(statValue(s.currentExp() + " / " + s.maxExp(), RPGTheme.EXP_PURPLE));

        // Gold
        grid.add(statLabel("💰  Gold"));
        grid.add(statValue(s.gold() + " G", RPGTheme.ACCENT_GOLD));

        // Floor
        grid.add(statLabel("🗺  Current Floor"));
        grid.add(statValue(String.valueOf(s.floor()), RPGTheme.TEXT_PRIMARY));

        // Highest cleared floor
        grid.add(statLabel("🏆  Highest Cleared"));
        grid.add(statValue(s.highestClearedFloor() + " / 100", RPGTheme.ACCENT_GOLD));

        // Dead status
        if (s.isDead()) {
            grid.add(statLabel("💀  Status"));
            grid.add(statValue("DEAD — use a potion!", RPGTheme.ACCENT_EMBER));
        }

        // Wrap grid in a card panel
        RPGComponents.DarkPanel gridCard = new RPGComponents.DarkPanel(
                RPGTheme.BG_DARKEST, RPGTheme.BORDER_DARK, 8);
        gridCard.setLayout(new BorderLayout());
        gridCard.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        gridCard.add(grid, BorderLayout.CENTER);

        content.add(gridCard, BorderLayout.CENTER);

        // -- Bonus note --
        JLabel note = RPGComponents.label(
                "* Values shown as Base (+ Bonus) when buffs are active",
                RPGTheme.TEXT_DISABLED, RPGTheme.FONT_SMALL);
        content.add(note, BorderLayout.SOUTH);

        dialog.add(content);
        dialog.setSize(480, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Helper labels for the stats grid
    private JLabel statLabel(String text) {
        JLabel l = RPGComponents.label(text, RPGTheme.TEXT_SECONDARY, RPGTheme.FONT_BODY_BOLD);
        l.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        return l;
    }

    private JLabel statValue(String text, Color color) {
        JLabel l = RPGComponents.label(text, color, RPGTheme.FONT_BODY);
        l.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        return l;
    }

    // Shows base value and total (with bonus) if they differ
    private JLabel statValueWithBonus(int base, int total, Color color) {
        String text = (total != base)
                ? base + "  (+" + (total - base) + " = " + total + ")"
                : String.valueOf(base);
        return statValue(text, color);
    }

    // ======================================================
    // Inventory Dialog — data fetched via controller
    // ======================================================
    private void showInventoryDialog() {
        JDialog dialog = makeDialog("  Inventory");

        if (controller == null || !controller.hasPlayer()) {
            dialog.add(RPGComponents.label("Tidak ada data player.", RPGTheme.ACCENT_EMBER, RPGTheme.FONT_BODY));
            dialog.pack();
            dialog.setVisible(true);
            return;
        }

        // Use a wrapper so we can rebuild the content on item use
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(RPGTheme.BG_DARK);
        dialog.add(wrapper);

        // Build a refreshable content builder
        Runnable[] refreshRef = new Runnable[1];
        refreshRef[0] = () -> {
            wrapper.removeAll();
            List<InventorySlot> slots = controller.getInventorySlots();

            JPanel content = new JPanel(new BorderLayout(0, 12));
            content.setBackground(RPGTheme.BG_DARK);
            content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            if (slots.isEmpty()) {
                JLabel empty = RPGComponents.label(
                        "Inventory kosong. Kalahkan musuh untuk mendapatkan item!",
                        RPGTheme.TEXT_SECONDARY, RPGTheme.FONT_BODY);
                empty.setHorizontalAlignment(SwingConstants.CENTER);
                empty.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
                content.add(empty, BorderLayout.CENTER);
            } else {
                // Table + Use button column
                String[] cols = {"Item", "Deskripsi", "Qty", "Harga", "Aksi"};
                Object[][] data = new Object[slots.size()][5];
                for (int i = 0; i < slots.size(); i++) {
                    InventorySlot slot = slots.get(i);
                    data[i][0] = slot.getItem().getName();
                    data[i][1] = slot.getItem().getDescription();
                    data[i][2] = slot.getQuantity();
                    data[i][3] = slot.getItem().getPrice() + " G";
                    data[i][4] = "Use";
                }

                JTable table = new JTable(data, cols) {
                    @Override
                    public boolean isCellEditable(int r, int c) {
                        return false;
                    }
                };
                styleTable(table);

                // Button renderer + click handler for "Use" column
                table.getColumn("Aksi").setCellRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
                    RPGComponents.RPGButton btn = new RPGComponents.RPGButton("Use", RPGTheme.HP_GREEN, RPGTheme.BG_DARKEST);
                    btn.setFont(RPGTheme.FONT_SMALL);
                    return btn;
                });
                table.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        int row = table.rowAtPoint(e.getPoint());
                        int col = table.columnAtPoint(e.getPoint());
                        if (col == 4 && row >= 0) {
                            String msg = controller.useItem(row);
                            JOptionPane.showMessageDialog(dialog, msg, "Item Used", JOptionPane.INFORMATION_MESSAGE);
                            refreshRef[0].run(); // rebuild table after use
                            refreshUI();          // refresh HP bar in main menu
                        }
                    }
                });
                table.getColumnModel().getColumn(4).setPreferredWidth(55);
                table.setRowHeight(28);

                JScrollPane scroll = new JScrollPane(table);
                scroll.setBorder(BorderFactory.createLineBorder(RPGTheme.BORDER_GOLD, 1));
                scroll.getViewport().setBackground(RPGTheme.BG_DARKEST);
                content.add(scroll, BorderLayout.CENTER);

                JLabel summary = RPGComponents.label(
                        "Total item: " + slots.size() + " jenis  |  Slot: " + slots.size() + "/30",
                        RPGTheme.TEXT_SECONDARY, RPGTheme.FONT_SMALL);
                content.add(summary, BorderLayout.SOUTH);
            }

            wrapper.add(content);
            wrapper.revalidate();
            wrapper.repaint();
        };
        refreshRef[0].run();

        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // ======================================================
    // Skills Dialog — data fetched via controller
    // ======================================================
    private void showSkillsDialog() {
        JDialog dialog = makeDialog("  Skill List");

        if (controller == null || !controller.hasPlayer()) {
            dialog.add(RPGComponents.label("Tidak ada data player.", RPGTheme.ACCENT_EMBER, RPGTheme.FONT_BODY));
            dialog.pack();
            dialog.setVisible(true);
            return;
        }

        // Controller provides skill data — no direct model access here
        List<Skill> skills = controller.getUnlockedSkills();

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setBackground(RPGTheme.BG_DARK);
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        if (skills.isEmpty()) {
            JLabel empty = RPGComponents.label(
                    "Belum ada skill. Selesaikan floor milestone untuk unlock skill!",
                    RPGTheme.TEXT_SECONDARY, RPGTheme.FONT_BODY);
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            empty.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
            content.add(empty, BorderLayout.CENTER);
        } else {
            String[] cols = {"Skill", "Tipe", "Effect", "Mana", "Cooldown", "Deskripsi"};
            Object[][] data = new Object[skills.size()][6];
            for (int i = 0; i < skills.size(); i++) {
                Skill sk = skills.get(i);
                data[i][0] = sk.getName();
                data[i][1] = sk.getClass().getSuperclass().getSimpleName();
                data[i][2] = "-";
                data[i][3] = sk.getManaCost();
                data[i][4] = sk.getCurrentCooldown() > 0
                        ? "CD: " + sk.getCurrentCooldown()
                        : "Ready";
                data[i][5] = sk.getDescription();
            }

            JTable table = new JTable(data, cols) {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }

                @Override
                public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                    Component c = super.prepareRenderer(renderer, row, col);
                    if (c instanceof JLabel lbl) {
                        String cdVal = (String) getValueAt(row, 4);
                        if ("Ready".equals(cdVal)) {
                            lbl.setForeground(col == 4 ? RPGTheme.HP_GREEN : RPGTheme.TEXT_PRIMARY);
                        } else {
                            lbl.setForeground(col == 4 ? RPGTheme.ACCENT_EMBER : RPGTheme.TEXT_PRIMARY);
                        }
                    }
                    return c;
                }
            };
            styleTable(table);
            table.getColumnModel().getColumn(5).setPreferredWidth(200);

            JScrollPane scroll = new JScrollPane(table);
            scroll.setBorder(BorderFactory.createLineBorder(RPGTheme.BORDER_GOLD, 1));
            scroll.getViewport().setBackground(RPGTheme.BG_DARKEST);
            content.add(scroll, BorderLayout.CENTER);

            JLabel summary = RPGComponents.label(
                    "Skill unlocked: " + skills.size() + " / 11  (skill baru tiap 10 floor)",
                    RPGTheme.TEXT_SECONDARY, RPGTheme.FONT_SMALL);
            content.add(summary, BorderLayout.SOUTH);
        }

        dialog.add(content);
        dialog.setSize(680, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // ======================================================
    // Helpers
    // ======================================================
    // ======================================================
    // Replay Floor Dialog
    // ======================================================
    private void showReplayFloorDialog() {
        JDialog dialog = makeDialog("  Replay Floor");

        if (controller == null || !controller.hasPlayer()) {
            dialog.add(RPGComponents.label("Tidak ada data player.", RPGTheme.ACCENT_EMBER, RPGTheme.FONT_BODY));
            dialog.pack();
            dialog.setVisible(true);
            return;
        }

        MainMenuController.PlayerSnapshot snap = controller.getPlayerSnapshot();
        int highest = snap.highestClearedFloor();

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setBackground(RPGTheme.BG_DARK);
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        MainMenuController.DungeonEntryResult entryResult = controller.canEnterDungeon();
        if (entryResult == MainMenuController.DungeonEntryResult.PLAYER_DEAD) {
            // show dead message
            JLabel empty = RPGComponents.label(
                    "Selisih level player dan level lantai melebihi 1!",
                    RPGTheme.TEXT_SECONDARY, RPGTheme.FONT_BODY);
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            empty.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
            content.add(empty, BorderLayout.CENTER);
        } else if (entryResult == MainMenuController.DungeonEntryResult.OVER_LEVELED) {
            // show over-level message  
            JLabel empty = RPGComponents.label(
                    "Selisih level player dan level lantai melebihi 1!",
                    RPGTheme.TEXT_SECONDARY, RPGTheme.FONT_BODY);
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            empty.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
            content.add(empty, BorderLayout.CENTER);
        }

        if (highest < 1) {
            JLabel empty = RPGComponents.label(
                    "Belum ada floor yang diselesaikan. Clear floor pertama dulu!",
                    RPGTheme.TEXT_SECONDARY, RPGTheme.FONT_BODY);
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            empty.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
            content.add(empty, BorderLayout.CENTER);
        } else {
            // Info label
            JLabel info = RPGComponents.label(
                    "Pilih floor yang ingin kamu ulangi (1 – " + highest + "):",
                    RPGTheme.TEXT_PRIMARY, RPGTheme.FONT_BODY);
            content.add(info, BorderLayout.NORTH);

            // Spinner for floor selection
            SpinnerNumberModel spinModel = new SpinnerNumberModel(1, 1, highest, 1);
            JSpinner spinner = new JSpinner(spinModel);
            spinner.setFont(RPGTheme.FONT_BODY);
            spinner.setBackground(RPGTheme.BG_DARKEST);
            spinner.setForeground(RPGTheme.TEXT_PRIMARY);
            ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField()
                    .setBackground(RPGTheme.BG_DARKEST);
            ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField()
                    .setForeground(RPGTheme.TEXT_PRIMARY);

            JPanel spinPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
            spinPanel.setBackground(RPGTheme.BG_DARK);
            spinPanel.add(RPGComponents.label("Floor: ", RPGTheme.TEXT_SECONDARY, RPGTheme.FONT_BODY));
            spinPanel.add(spinner);
            content.add(spinPanel, BorderLayout.CENTER);

            // Confirm button
            RPGComponents.RPGButton confirmBtn = new RPGComponents.RPGButton(
                    "Go to Floor", RPGTheme.ACCENT_GOLD, RPGTheme.BG_DARK);
            confirmBtn.setFont(RPGTheme.FONT_BODY_BOLD);
            confirmBtn.addActionListener(e -> {
                MainMenuController.DungeonEntryResult result = controller.canEnterDungeon();
                switch (result) {
                    case PLAYER_DEAD -> {
                        JOptionPane.showMessageDialog(this,
                                "You are dead! Use a potion from your Inventory to recover first.",
                                "Cannot Enter Dungeon", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    case OVER_LEVELED -> {
                        JOptionPane.showMessageDialog(this,
                                "Your level exceeds the floor cap.\nYou can only enter floors up to your current level.",
                                "Cannot Enter Dungeon", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    case OK -> {
                    } // fall through
                }
                int chosen = (int) spinner.getValue();
                boolean ok = controller.setReplayFloor(chosen);
                if (ok) {
                    dialog.dispose();
                    if (onEnterDungeon != null) {
                        onEnterDungeon.run();
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Floor " + chosen + " belum diselesaikan.",
                            "Invalid Floor", JOptionPane.WARNING_MESSAGE);
                }
            });

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            btnPanel.setBackground(RPGTheme.BG_DARK);
            btnPanel.add(confirmBtn);
            content.add(btnPanel, BorderLayout.SOUTH);
        }

        dialog.add(content);
        dialog.setSize(420, 260);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JDialog makeDialog(String title) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog d = new JDialog(owner instanceof Frame f ? f : null, title, true);
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(RPGTheme.BG_DARK);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(RPGTheme.BG_DARKEST);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, RPGTheme.BORDER_GOLD),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));
        JLabel titleLbl = RPGComponents.goldLabel(title, RPGTheme.FONT_SUB);
        header.add(titleLbl, BorderLayout.WEST);
        RPGComponents.RPGButton closeBtn = new RPGComponents.RPGButton("✕", RPGTheme.ACCENT_EMBER, RPGTheme.BG_DARKEST);
        closeBtn.setPreferredSize(new Dimension(34, 28));
        closeBtn.addActionListener(e -> d.dispose());
        header.add(closeBtn, BorderLayout.EAST);
        d.add(header, BorderLayout.NORTH);

        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        d.setResizable(true);
        return d;
    }

    private void styleTable(JTable table) {
        table.setBackground(RPGTheme.BG_DARKEST);
        table.setForeground(RPGTheme.TEXT_PRIMARY);
        table.setFont(RPGTheme.FONT_BODY);
        table.setGridColor(RPGTheme.BORDER_DARK);
        table.setRowHeight(28);
        table.setShowGrid(true);
        table.setSelectionBackground(RPGTheme.BG_LIGHT);
        table.setSelectionForeground(RPGTheme.ACCENT_GOLD);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(RPGTheme.BG_MID);
        header.setForeground(RPGTheme.ACCENT_GOLD);
        header.setFont(RPGTheme.FONT_BODY_BOLD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, RPGTheme.BORDER_GOLD));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setBackground(RPGTheme.BG_DARKEST);
        centerRenderer.setForeground(RPGTheme.TEXT_PRIMARY);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JLabel centeredLabel(String text, Color color, Font font) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(color);
        l.setFont(font);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private JSeparator makeSep() {
        JSeparator sep = new JSeparator();
        sep.setForeground(RPGTheme.BORDER_GOLD);
        sep.setBackground(RPGTheme.BORDER_DARK);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        return sep;
    }

    private void drawBg(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth(), h = getHeight();
        g2.setColor(RPGTheme.BG_DARKEST);
        g2.fillRect(0, 0, w, h);
        g2.setColor(new Color(255, 255, 255, 4));
        g2.setStroke(new BasicStroke(1));
        for (int i = -h; i < w + h; i += 50) {
            g2.drawLine(i, 0, i + h, h);
        }
        g2.dispose();
    }
}
