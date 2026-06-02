package kelompok11.turnbaserpg.model.item.consumable;

import kelompok11.turnbaserpg.model.enums.ConsumableType;
import kelompok11.turnbaserpg.model.enums.PotionTier;
import kelompok11.turnbaserpg.model.character.Player;

public class ManaPotion extends Potion {

    public ManaPotion(PotionTier tier) {
        super(
                tier.getDisplayName() + " Mana Potion",
                "Restore " + tier.getEffectValue() + " Mana to player",
                tier.getEffectValue(),
                (int) (50 * tier.getMultiplier()),
                tier, ConsumableType.MANA);
    }

    @Override
    public void use(Player target) {
        target.getStats().increaseCurrentMana(this.effectValue);
    }
}
