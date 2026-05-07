/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package kelompok11.turnbaserpg.enums;

/**
 *
 * @author Pongo
 */
public enum BuffType {
    ATTACK("Attack Buff", "Gain attack stat bonus to your character"),
    DEFENSE("Defense Buff", "Gain defense stat bonus to your character"),
    MAGIC("Magic Buff", "Gain magic stat bonus to your character"),
    MANA("Mana Buff", "Gain mana stat bonus to your character");

    private final String displayName;
    private final String description;

    BuffType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
