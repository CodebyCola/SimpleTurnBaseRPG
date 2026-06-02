package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.model.enums.SkillType;
import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

public class DragonFury extends Skill {

    public DragonFury() {
        super("Dragon Fury", "Release destructive dragon power", 90, 40,
                GameConstants.SKILL_COOLDOWN_ULTIMATE, SkillType.ATTACK);
    }

    @Override
    public boolean cast(Character caster, Character target) {
        if (!canCast(caster)) return false;
        target.takeDamage(effectValue + caster.getStats().getTotalMagic());
        applyManaAndCooldown(caster);
        return true;
    }
}
