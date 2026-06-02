package kelompok11.turnbaserpg.view;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class RPGTheme {

    
    public static final Color BG_DARKEST    = new Color(0x0A, 0x0C, 0x12); 
    public static final Color BG_DARK       = new Color(0x10, 0x14, 0x1E); 
    public static final Color BG_MID        = new Color(0x18, 0x1E, 0x2E); 
    public static final Color BG_LIGHT      = new Color(0x22, 0x2A, 0x3E); 

    public static final Color ACCENT_GOLD   = new Color(0xE8, 0xB4, 0x2A); 
    public static final Color ACCENT_EMBER  = new Color(0xE8, 0x5A, 0x2A); 
    public static final Color ACCENT_SILVER = new Color(0xAA, 0xB8, 0xCC); 
    public static final Color ACCENT_CYAN   = new Color(0x2A, 0xD4, 0xE8); 

    public static final Color HP_RED        = new Color(0xE8, 0x3A, 0x3A); 
    public static final Color HP_GREEN      = new Color(0x3A, 0xE8, 0x6A); 
    public static final Color MANA_BLUE     = new Color(0x3A, 0x8A, 0xE8); 
    public static final Color EXP_PURPLE    = new Color(0xA0, 0x4A, 0xE8); 

    public static final Color TEXT_PRIMARY  = new Color(0xF0, 0xE8, 0xD0); 
    public static final Color TEXT_SECONDARY= new Color(0x88, 0x98, 0xAA); 
    public static final Color TEXT_GOLD     = new Color(0xE8, 0xB4, 0x2A); 
    public static final Color TEXT_DISABLED = new Color(0x44, 0x50, 0x60); 

    public static final Color BORDER_DARK   = new Color(0x2A, 0x34, 0x48); 
    public static final Color BORDER_GOLD   = new Color(0x78, 0x5A, 0x1A); 

    public static final Color WARRIOR_COLOR = new Color(0xE8, 0x6A, 0x3A); 
    public static final Color MAGE_COLOR    = new Color(0x6A, 0x3A, 0xE8); 
    public static final Color ARCHER_COLOR  = new Color(0x3A, 0xC8, 0x68); 

    
    public static final Font FONT_TITLE   = new Font("Georgia", Font.BOLD, 32);
    public static final Font FONT_HEADING = new Font("Georgia", Font.BOLD, 20);
    public static final Font FONT_SUB     = new Font("Georgia", Font.BOLD, 15);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO    = new Font("Consolas", Font.PLAIN, 12);
    public static final Font FONT_BATTLE  = new Font("Consolas", Font.PLAIN, 13);

    
    public static Border panelBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_DARK, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        );
    }

    public static Border goldBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GOLD, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        );
    }

    public static Border titleBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_GOLD, 1),
            " " + title + " ",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            FONT_SMALL,
            ACCENT_GOLD
        );
    }

    
    public static Color roleColor(String role) {
        if (role == null) return ACCENT_SILVER;
        return switch (role.toUpperCase()) {
            case "WARRIOR" -> WARRIOR_COLOR;
            case "MAGE"    -> MAGE_COLOR;
            case "ARCHER"  -> ARCHER_COLOR;
            default        -> ACCENT_SILVER;
        };
    }

    public static String roleIcon(String role) {
        if (role == null) return "⚔";
        return switch (role.toUpperCase()) {
            case "WARRIOR" -> " ";
            case "MAGE"    -> " ";
            case "ARCHER"  -> " ";
            default        -> " ";
        };
    }
}
