/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package kelompok11.turnbaserpg.enums;

/**
 *
 * @author Pongo
 */
public enum Role {
    WARRIOR("Warrior", "Tanker dengan DEF tinggi"),
    MAGE("Mage", "Magician dengan Magic tinggi"),
    ARCHER("Archer", "Ranged Attacker dengan attack tinggi");

    private final String displayName;
    private final String description;

    Role(String displayName, String description) {
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
