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
public class ManaPotion extends Potion {

    public ManaPotion(PotionTier tier) {
        super(
                tier.getDisplayName() + " Mana Potion",
                "Restore " + tier.getEffectValue() + " Mana to player",
                tier.getEffectValue(),
                (int) (50 * tier.getMultiplier()),
                tier,
                ConsumableType.MANA);
    }
    
    public void use(Character target) {
        target.getStats().increaseCurrentMana(this.effectValue);
    }
    

}
