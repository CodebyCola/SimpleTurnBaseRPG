/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.item.consumable;

import kelompok11.turnbaserpg.enums.ConsumableType;
import kelompok11.turnbaserpg.enums.PotionTier;

import kelompok11.turnbaserpg.model.character.Player;

/**
 *
 * @author Pongo
 */
public class RevivePotion extends Potion {

    public RevivePotion(PotionTier tier, ConsumableType type) {
        super("Revive Potion", "Use this potion to revive your character", 1, 1000, tier, type);
    }

    @Override
    public void use(Player target) {
        target.revive();
    }

}
