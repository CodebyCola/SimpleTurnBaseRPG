/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package kelompok11.turnbaserpg.enums;

/**
 *
 * @author Pongo
 */
public enum Difficulty {
    EASY("Easy", 1.0),
    NORMAL("Normal", 1.1),
    HARD("Hard", 1.2),
    NIGHTMARE("Nightmare", 1.3);

    private final String displayName;
    private final double statMultiplier;

    public String getDisplayName() {
        return displayName;
    }

    public double getStatMultiplier() {
        return statMultiplier;
    }

    Difficulty(String displayName, double statMultiplier) {
        this.displayName = displayName;
        this.statMultiplier = statMultiplier;
    }
}
