package kelompok11.turnbaserpg.model.item.consumable;

import kelompok11.turnbaserpg.model.enums.ConsumableType;
import kelompok11.turnbaserpg.model.enums.PotionTier;
import kelompok11.turnbaserpg.model.character.Player;

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
    public void use(Player target) {
        target.heal(this.effectValue);
    }
}
