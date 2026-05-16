/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package kelompok11.turnbaserpg.enums;

import kelompok11.turnbaserpg.utils.GameConstants;

/**
 *
 * @author Pongo
 */
public enum PotionTier {
    SMALL("Small", GameConstants.SMALL_POTION_VALUE, 1),
    MEDIUM("Medium", GameConstants.MEDIUM_POTION_VALUE, 1.2),
    LARGE("Large", GameConstants.LARGE_POTION_VALUE, 1.5);

    private String displayName;
    private double multiplier;
    private int effectValue;

    PotionTier(String displayName, int effectValue, double multiplier) {
        this.displayName = displayName;
        this.effectValue = effectValue;
        this.multiplier = multiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getEffectValue() {
        return (int)(effectValue * multiplier);
    }

    public double getMultiplier() {
        return multiplier;
    }
    

}
