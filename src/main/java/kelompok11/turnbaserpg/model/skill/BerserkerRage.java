package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.model.enums.SkillType;
import kelompok11.turnbaserpg.model.buff.AttackBuff;
import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

public class BerserkerRage extends Skill {

    public BerserkerRage() {
        super("Berserker Rage", "Increase attack temporarily", 25, 15,
                GameConstants.SKILL_COOLDOWN_MEDIUM, SkillType.BUFF);
    }

    @Override
    public boolean cast(Character caster, Character target) {
        if (!canCast(caster)) return false;
        caster.addBuff(new AttackBuff(effectValue));
        applyManaAndCooldown(caster);
        return true;
    }
}
