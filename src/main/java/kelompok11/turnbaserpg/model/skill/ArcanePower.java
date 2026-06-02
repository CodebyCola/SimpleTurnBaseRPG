package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.model.enums.SkillType;
import kelompok11.turnbaserpg.model.buff.MagicBuff;
import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

public class ArcanePower extends Skill {

    public ArcanePower() {
        super("Arcane Power", "Increase magic power temporarily", 30, 20,
                GameConstants.SKILL_COOLDOWN_MEDIUM, SkillType.BUFF);
    }

    @Override
    public boolean cast(Character caster, Character target) {
        if (!canCast(caster)) return false;
        caster.addBuff(new MagicBuff(effectValue));
        applyManaAndCooldown(caster);
        return true;
    }
}
