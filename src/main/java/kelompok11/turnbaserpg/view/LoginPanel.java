package kelompok11.turnbaserpg.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LoginPanel extends JPanel {

    
    private BiConsumer<String, String> onLogin;
    private TriConsumer<String, String, String> onRegister;

    @FunctionalInterface
    public interface TriConsumer<A, B, C> {
        void accept(A a, B b, C c);
    }

    
    private JPanel loginTab;
    private JPanel registerTab;
    private JButton tabLogin;
    private JButton tabRegister;
    private boolean showLogin = true;

    
    private RPGComponents.RPGTextField loginName;
    private RPGComponents.RPGPasswordField loginPassword;

    
    private RPGComponents.RPGTextField regName;
    private RPGComponents.RPGPasswordField regPassword;
    private JComboBox<String> roleCombo;

    
    private JLabel statusLabel;

    public LoginPanel(BiConsumer<String, String> onLogin,
                      TriConsumer<String, String, String> onRegister) {
        this.onLogin    = onLogin;
        this.onRegister = onRegister;
        setLayout(new BorderLayout());
        setBackground(RPGTheme.BG_DARKEST);
        buildUI();
    }

    private void buildUI() {
        
        JPanel root = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBackground(g);
            }
        };
        root.setBackground(RPGTheme.BG_DARKEST);
        add(root, BorderLayout.CENTER);

        
        RPGComponents.DarkPanel card = new RPGComponents.DarkPanel(
            RPGTheme.BG_DARK, RPGTheme.BORDER_GOLD, 12);
        card.setLayout(new BorderLayout(0, 0));
        card.setPreferredSize(new Dimension(420, 560));
        card.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        
        card.add(buildHeader(), BorderLayout.NORTH);

        
        JPanel center = new JPanel(new BorderLayout(0, 0));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(0, 24, 24, 24));
        center.add(buildTabBar(), BorderLayout.NORTH);

        
        JPanel formHolder = new JPanel(new CardLayout());
        formHolder.setOpaque(false);
        loginTab    = buildLoginForm();
        registerTab = buildRegisterForm();
        formHolder.add(loginTab,    "login");
        formHolder.add(registerTab, "register");
        center.add(formHolder, BorderLayout.CENTER);

        
        statusLabel = RPGComponents.label("", RPGTheme.ACCENT_EMBER, RPGTheme.FONT_SMALL);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusRow.setOpaque(false);
        statusRow.add(statusLabel);
        center.add(statusRow, BorderLayout.SOUTH);

        card.add(center, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        root.add(card, gbc);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                
                g2.setColor(RPGTheme.ACCENT_GOLD);
                g2.fillRect(0, 0, getWidth(), 3);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(32, 24, 20, 24));

        
        JLabel icon = new JLabel("⚔", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        icon.setForeground(RPGTheme.ACCENT_GOLD);
        header.add(icon);

        header.add(Box.createVerticalStrut(8));

        JLabel title = new JLabel("DUNGEON REALM", SwingConstants.CENTER);
        title.setFont(RPGTheme.FONT_TITLE);
        title.setForeground(RPGTheme.ACCENT_GOLD);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(title);

        JLabel sub = new JLabel("Turn-Based RPG Adventure", SwingConstants.CENTER);
        sub.setFont(RPGTheme.FONT_SMALL);
        sub.setForeground(RPGTheme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(sub);

        header.add(Box.createVerticalStrut(12));
        return header;
    }

    private JPanel buildTabBar() {
        JPanel tabBar = new JPanel(new GridLayout(1, 2, 4, 0));
        tabBar.setOpaque(false);
        tabBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        tabLogin    = createTabButton("Login", true);
        tabRegister = createTabButton("Register", false);

        tabLogin.addActionListener(e -> switchTab(true, tabBar));
        tabRegister.addActionListener(e -> switchTab(false, tabBar));

        tabBar.add(tabLogin);
        tabBar.add(tabRegister);
        return tabBar;
    }

    private JButton createTabButton(String text, boolean active) {
        JButton btn = new JButton(text) {
            boolean isActive = active;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isActive) {
                    g2.setColor(RPGTheme.BG_MID);
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);
                    g2.setColor(RPGTheme.ACCENT_GOLD);
                    g2.fillRect(0, getHeight()-2, getWidth(), 2);
                } else {
                    g2.setColor(RPGTheme.BG_DARKEST);
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);
                }
                g2.dispose();
                g.setFont(getFont());
                g.setColor(isActive ? RPGTheme.ACCENT_GOLD : RPGTheme.TEXT_SECONDARY);
                FontMetrics fm = g.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g.drawString(getText(), tx, ty);
            }
            
            public void setActive(boolean a) { isActive = a; repaint(); }
        };
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(RPGTheme.FONT_BODY_BOLD);
        btn.setPreferredSize(new Dimension(120, 36));
        return btn;
    }

    @SuppressWarnings("unchecked")
    private void switchTab(boolean toLogin, JPanel tabBar) {
        showLogin = toLogin;
        
        Component[] tabs = tabBar.getComponents();
        for (Component c : tabs) {
            if (c instanceof JButton btn) {
                boolean isLogin = btn.getText().equals("Login");
                try {
                    btn.getClass().getMethod("setActive", boolean.class)
                       .invoke(btn, isLogin == toLogin);
                } catch (Exception ex) {  }
            }
        }
        
        Container parent = loginTab.getParent();
        if (parent.getLayout() instanceof CardLayout cl) {
            cl.show(parent, toLogin ? "login" : "register");
        }
        statusLabel.setText("");
    }

    private JPanel buildLoginForm() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        loginName     = new RPGComponents.RPGTextField(20);
        loginPassword = new RPGComponents.RPGPasswordField(20);

        p.add(fieldRow("Username", loginName));
        p.add(Box.createVerticalStrut(12));
        p.add(fieldRow("Password", loginPassword));
        p.add(Box.createVerticalStrut(20));

        RPGComponents.RPGButton loginBtn = new RPGComponents.RPGButton("⚔  Enter the Realm");
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> doLogin());
        p.add(loginBtn);

        
        loginPassword.addActionListener(e -> doLogin());

        return p;
    }

    private JPanel buildRegisterForm() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        regName     = new RPGComponents.RPGTextField(20);
        regPassword = new RPGComponents.RPGPasswordField(20);

        String[] roles = {"  WARRIOR", "  MAGE", "  ARCHER"};
        roleCombo = new JComboBox<>(roles);
        roleCombo.setBackground(RPGTheme.BG_DARKEST);
        roleCombo.setForeground(RPGTheme.TEXT_PRIMARY);
        roleCombo.setFont(RPGTheme.FONT_BODY);
        roleCombo.setBorder(BorderFactory.createLineBorder(RPGTheme.BORDER_GOLD, 1));

        
        JLabel roleDesc = RPGComponents.label(getRoleDesc(0), RPGTheme.TEXT_SECONDARY, RPGTheme.FONT_SMALL);
        roleDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        roleCombo.addActionListener(e -> roleDesc.setText(getRoleDesc(roleCombo.getSelectedIndex())));

        p.add(fieldRow("Username", regName));
        p.add(Box.createVerticalStrut(10));
        p.add(fieldRow("Password", regPassword));
        p.add(Box.createVerticalStrut(10));
        p.add(fieldRow("Choose Class", roleCombo));
        p.add(Box.createVerticalStrut(4));
        JPanel descRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        descRow.setOpaque(false);
        descRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        descRow.add(roleDesc);
        p.add(descRow);
        p.add(Box.createVerticalStrut(14));

        RPGComponents.RPGButton regBtn = new RPGComponents.RPGButton("✦  Begin Your Journey");
        regBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        regBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        regBtn.addActionListener(e -> doRegister());
        p.add(regBtn);

        return p;
    }

    private String getRoleDesc(int idx) {
        return switch (idx) {
            case 0 -> "  High defense tank, starts with strong HP & DEF";
            case 1 -> "  Powerful magic attacks, high Mana pool";
            case 2 -> "  Balanced ATK & speed, ranged attacker";
            default -> "";
        };
    }

    private JPanel fieldRow(String labelText, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(0, 5));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel lbl = RPGComponents.label(labelText, RPGTheme.ACCENT_SILVER, RPGTheme.FONT_SMALL);
        row.add(lbl, BorderLayout.NORTH);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private void doLogin() {
        String name = loginName.getText().trim();
        String pass = new String(loginPassword.getPassword());
        if (name.isEmpty() || pass.isEmpty()) {
            setStatus("Please fill in all fields.", RPGTheme.ACCENT_EMBER);
            return;
        }
        if (onLogin != null) onLogin.accept(name, pass);
    }

    private void doRegister() {
        String name = regName.getText().trim();
        String pass = new String(regPassword.getPassword());
        String roleRaw = (String) roleCombo.getSelectedItem();
        String role = roleRaw != null ? roleRaw.replaceAll("[^A-Z]", "").trim() : "WARRIOR";
        if (name.isEmpty() || pass.isEmpty()) {
            setStatus("Please fill in all fields.", RPGTheme.ACCENT_EMBER);
            return;
        }
        if (onRegister != null) onRegister.accept(name, pass, role);
    }

    public void setStatus(String msg, Color color) {
        statusLabel.setText(msg);
        statusLabel.setForeground(color);
    }

    
    private void drawBackground(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth(), h = getHeight();

        
        RadialGradientPaint vignette = new RadialGradientPaint(
            new Point(w / 2, h / 2),
            Math.max(w, h) * 0.7f,
            new float[]{0f, 1f},
            new Color[]{new Color(0x14, 0x1A, 0x28), RPGTheme.BG_DARKEST}
        );
        g2.setPaint(vignette);
        g2.fillRect(0, 0, w, h);

        
        g2.setColor(new Color(255, 255, 255, 6));
        g2.setStroke(new BasicStroke(1));
        for (int x = 0; x < w; x += 40) g2.drawLine(x, 0, x, h);
        for (int y = 0; y < h; y += 40) g2.drawLine(0, y, w, y);

        
        drawCornerOrn(g2, 40, 40, 0);
        drawCornerOrn(g2, w - 40, 40, 90);
        drawCornerOrn(g2, w - 40, h - 40, 180);
        drawCornerOrn(g2, 40, h - 40, 270);

        g2.dispose();
    }

    private void drawCornerOrn(Graphics2D g2, int x, int y, int rot) {
        g2.setColor(new Color(RPGTheme.ACCENT_GOLD.getRed(),
            RPGTheme.ACCENT_GOLD.getGreen(), RPGTheme.ACCENT_GOLD.getBlue(), 60));
        g2.setStroke(new BasicStroke(1.5f));
        Graphics2D g3 = (Graphics2D) g2.create();
        g3.translate(x, y);
        g3.rotate(Math.toRadians(rot));
        g3.drawLine(0, 0, -20, 0);
        g3.drawLine(0, 0, 0, -20);
        g3.drawOval(-4, -4, 8, 8);
        g3.dispose();
    }
}
