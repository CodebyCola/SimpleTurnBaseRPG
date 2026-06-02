
package kelompok11.turnbaserpg.model.buff;

import kelompok11.turnbaserpg.model.enums.BuffType;
import kelompok11.turnbaserpg.model.character.Character;

public class MagicBuff extends Buff {

    private int effectValue;

    public MagicBuff(int effectValue) {
        super("Magic Buff", 3);
        this.effectValue = effectValue;
    }

    @Override
    public void use(Character target) {
        target.getStats().applyBuff(effectValue, BuffType.MAGIC);
    }

    @Override
    public void remove(Character target) {
        target.getStats().removeBuff(effectValue, BuffType.MAGIC);
    }
}
