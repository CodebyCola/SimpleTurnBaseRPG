/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.item.consumable;

import kelompok11.turnbaserpg.enums.ConsumableType;
import kelompok11.turnbaserpg.enums.PotionTier;
import kelompok11.turnbaserpg.model.character.Character;

/**
 *
 * @author Pongo
 */
public class HealthPotion extends Potion {
    
    public HealthPotion(PotionTier tier) {
        super(
                tier.getDisplayName() + " Health Potion",
                "Restore " + tier.getEffectValue() + " Hp to player",
                tier.getEffectValue(),
                (int) (50 * tier.getMultiplier()),
                tier, ConsumableType.HEALTH);
    }
    
    @Override
    public void use(Character target) {
        target.heal(this.effectValue);
    }
    
}
