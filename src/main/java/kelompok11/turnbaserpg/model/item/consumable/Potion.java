/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.item.consumable;

import kelompok11.turnbaserpg.enums.ConsumableType;
import kelompok11.turnbaserpg.enums.PotionTier;
import kelompok11.turnbaserpg.model.item.Item;
import kelompok11.turnbaserpg.model.Character.Character;

/**
 *
 * @author Pongo
 */
public abstract class Potion extends Item {

    protected int effectValue;
    protected PotionTier tier;
    private ConsumableType type;

    public Potion(String name, String description, int effectValue, int price, PotionTier tier, ConsumableType type) {
        super(name, description, price);
        this.tier = tier;
        this.type = type;
        this.effectValue = effectValue;

    }

    public PotionTier getTier() {
            return tier;
    }

}
