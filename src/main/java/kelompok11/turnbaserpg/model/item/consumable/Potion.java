package kelompok11.turnbaserpg.model.item.consumable;

import kelompok11.turnbaserpg.model.enums.ConsumableType;
import kelompok11.turnbaserpg.model.enums.PotionTier;
import kelompok11.turnbaserpg.model.item.Item;
import kelompok11.turnbaserpg.model.item.Usable;

public abstract class Potion extends Item implements Usable {

    protected int effectValue;
    protected PotionTier tier;
    private ConsumableType type;

    public Potion(String name, String description, int effectValue, int price,
                  PotionTier tier, ConsumableType type) {
        super(name, description, price);
        this.tier = tier;
        this.type = type;
        this.effectValue = effectValue;
    }

    public PotionTier getTier() {
        return tier;
    }

    public ConsumableType getType() {
        return type;
    }
}
