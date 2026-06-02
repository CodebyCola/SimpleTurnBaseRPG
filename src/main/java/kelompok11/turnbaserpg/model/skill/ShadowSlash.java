package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.model.enums.SkillType;
import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

public class ShadowSlash extends Skill {

    public ShadowSlash() {
        super("Shadow Slash", "Perform fast shadow blade attack", 30, 10,
                GameConstants.SKILL_COOLDOWN_DEFAULT, SkillType.ATTACK);
    }

    @Override
    public boolean cast(Character caster, Character target) {
        if (!canCast(caster)) return false;
        target.takeDamage(effectValue + caster.getStats().getTotalMagic());
        applyManaAndCooldown(caster);
        return true;
    }
}
