
package kelompok11.turnbaserpg.model.buff;

import kelompok11.turnbaserpg.model.enums.BuffType;
import kelompok11.turnbaserpg.model.character.Character;

public class AttackBuff extends Buff {

    private int effectValue;

    public AttackBuff(int effectValue) {
        super("Attack Buff", 3);
        this.effectValue = effectValue;
    }

    @Override
    public void use(Character target) {
        target.getStats().applyBuff(effectValue, BuffType.ATTACK);
    }

    @Override
    public void remove(Character target) {
        target.getStats().removeBuff(effectValue, BuffType.ATTACK);
    }

}
