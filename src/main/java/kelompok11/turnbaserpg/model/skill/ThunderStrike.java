package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.model.enums.SkillType;
import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

public class ThunderStrike extends Skill {

    public ThunderStrike() {
        super("Thunder Strike", "Strike enemy using lightning power", 45, 20,
                GameConstants.SKILL_COOLDOWN_HEAVY, SkillType.ATTACK);
    }

    @Override
    public boolean cast(Character caster, Character target) {
        if (!canCast(caster)) return false;
        target.takeDamage(effectValue + caster.getStats().getTotalMagic());
        applyManaAndCooldown(caster);
        return true;
    }
}
