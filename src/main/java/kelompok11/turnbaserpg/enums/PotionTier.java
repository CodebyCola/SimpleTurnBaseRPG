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
    SMALL("Small", GameConstants.SMALL_POTION_VALUE),
    MEDIUM("Medium", GameConstants.MEDIUM_POTION_VALUE),
    LARGE("Large", GameConstants.LARGE_POTION_VALUE);

    private String displayName;
    private int effectValue;

    PotionTier(String displayName, int effectValue
    ) {
        this.displayName = displayName;
        this.effectValue = effectValue;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    public int getEffectValue() {
        return effectValue;
    }
}
