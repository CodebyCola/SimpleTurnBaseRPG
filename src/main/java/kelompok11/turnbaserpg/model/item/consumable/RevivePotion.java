package kelompok11.turnbaserpg.model.item.consumable;

import kelompok11.turnbaserpg.model.enums.ConsumableType;
import kelompok11.turnbaserpg.model.enums.PotionTier;
import kelompok11.turnbaserpg.model.character.Player;

public class RevivePotion extends Potion {

    public RevivePotion(PotionTier tier) {
        super("Revive Potion", "Use this potion to revive your character", 1, 1000,
                tier, ConsumableType.REVIVE);
    }

    @Override
    public void use(Player target) {
        target.revive();
    }
}
