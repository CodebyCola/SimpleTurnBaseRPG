package kelompok11.turnbaserpg.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class RPGComponents {

    
    
    
    public static class RPGButton extends JButton {

        private Color baseColor;
        private Color hoverColor;
        private Color pressColor;
        private boolean hovered = false;
        private boolean pressed = false;

        public RPGButton(String text) {
            this(text, RPGTheme.ACCENT_GOLD, RPGTheme.BG_MID);
        }

        public RPGButton(String text, Color accentColor, Color bgColor) {
            super(text);
            this.baseColor  = accentColor;
            this.hoverColor = accentColor.brighter();
            this.pressColor = accentColor.darker();
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(RPGTheme.TEXT_PRIMARY);
            setFont(RPGTheme.FONT_BODY_BOLD);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(getPreferredSize().width + 20, 40));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                @Override public void mousePressed(MouseEvent e) { pressed = true; repaint(); }
                @Override public void mouseReleased(MouseEvent e){ pressed = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            Color bg = pressed ? pressColor.darker() : (hovered ? RPGTheme.BG_LIGHT : RPGTheme.BG_MID);
            Color border = pressed ? pressColor : (hovered ? hoverColor : baseColor);

            
            if (hovered) {
                g2.setColor(new Color(border.getRed(), border.getGreen(), border.getBlue(), 60));
                g2.fillRoundRect(-3, -3, w + 6, h + 6, 10, 10);
            }

            
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 8, 8);

            
            g2.setColor(border);
            g2.setStroke(new BasicStroke(hovered ? 1.5f : 1.0f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 8, 8);

            
            GradientPaint sheen = new GradientPaint(0, 0,
                new Color(255, 255, 255, hovered ? 20 : 10), 0, h / 2,
                new Color(255, 255, 255, 0));
            g2.setPaint(sheen);
            g2.fillRoundRect(1, 1, w - 2, h / 2, 6, 6);

            g2.dispose();

            
            FontMetrics fm = g.getFontMetrics(getFont());
            int tx = (w - fm.stringWidth(getText())) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            g.setFont(getFont());
            g.setColor(pressed ? baseColor.darker() : (hovered ? hoverColor : getForeground()));
            g.drawString(getText(), tx, ty);
        }
    }

    
    
    
    public static class StatBar extends JPanel {

        private int current, max;
        private Color barColor;
        private String label;

        public StatBar(String label, int current, int max, Color barColor) {
            this.label    = label;
            this.current  = current;
            this.max      = max;
            this.barColor = barColor;
            setPreferredSize(new Dimension(200, 22));
            setOpaque(false);
        }

        public void setValues(int current, int max) {
            this.current = current;
            this.max = max;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int barH = h - 4;
            int barY = 2;

            
            g2.setColor(RPGTheme.BG_DARKEST);
            g2.fillRoundRect(0, barY, w - 1, barH, 6, 6);
            g2.setColor(RPGTheme.BORDER_DARK);
            g2.drawRoundRect(0, barY, w - 1, barH, 6, 6);

            
            double ratio = max > 0 ? (double) current / max : 0;
            int fillW = (int) ((w - 2) * Math.min(ratio, 1.0));
            if (fillW > 0) {
                
                GradientPaint gp = new GradientPaint(
                    1, barY + 1, barColor.brighter(),
                    1, barY + barH - 2, barColor.darker()
                );
                g2.setPaint(gp);
                g2.fillRoundRect(1, barY + 1, fillW, barH - 2, 5, 5);

                
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(1, barY + 1, fillW, (barH - 2) / 2, 5, 5);
            }

            
            String text = label + ": " + current + "/" + max;
            g2.setFont(RPGTheme.FONT_SMALL);
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(text)) / 2;
            int ty = barY + (barH + fm.getAscent() - fm.getDescent()) / 2;

            
            g2.setColor(new Color(0, 0, 0, 160));
            g2.drawString(text, tx + 1, ty + 1);
            g2.setColor(Color.WHITE);
            g2.drawString(text, tx, ty);

            g2.dispose();
        }
    }

    
    
    
    public static class DarkPanel extends JPanel {

        private Color bgColor;
        private Color borderColor;
        private int arc;

        public DarkPanel() {
            this(RPGTheme.BG_DARK, RPGTheme.BORDER_DARK, 8);
        }

        public DarkPanel(Color bg, Color border, int arc) {
            this.bgColor = bg;
            this.borderColor = border;
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    
    
    
    public static JLabel goldLabel(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(RPGTheme.TEXT_GOLD);
        lbl.setFont(font);
        return lbl;
    }

    public static JLabel label(String text, Color color, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(color);
        lbl.setFont(font);
        return lbl;
    }

    
    
    
    public static class RPGTextField extends JTextField {
        public RPGTextField(int cols) {
            super(cols);
            setBackground(RPGTheme.BG_DARKEST);
            setForeground(RPGTheme.TEXT_PRIMARY);
            setCaretColor(RPGTheme.ACCENT_GOLD);
            setFont(RPGTheme.FONT_BODY);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RPGTheme.BORDER_GOLD, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
        }
    }

    
    
    
    public static class RPGPasswordField extends JPasswordField {
        public RPGPasswordField(int cols) {
            super(cols);
            setBackground(RPGTheme.BG_DARKEST);
            setForeground(RPGTheme.TEXT_PRIMARY);
            setCaretColor(RPGTheme.ACCENT_GOLD);
            setFont(RPGTheme.FONT_BODY);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RPGTheme.BORDER_GOLD, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
        }
    }

    
    
    
    public static class SeparatorLine extends JPanel {
        private final Color color;
        public SeparatorLine(Color color) {
            this.color = color;
            setPreferredSize(new Dimension(1, 1));
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(color);
            g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
        }
    }
}
