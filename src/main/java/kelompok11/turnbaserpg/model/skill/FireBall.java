package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.model.enums.SkillType;
import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

public class FireBall extends Skill {

    public FireBall() {
        super("Fire Ball", "Cast Fire Ball to attack enemy", 15, 10,
                GameConstants.SKILL_COOLDOWN_MEDIUM, SkillType.ATTACK);
    }

    @Override
    public boolean cast(Character caster, Character target) {
        if (!canCast(caster)) return false;
        target.takeDamage(effectValue + caster.getStats().getTotalMagic());
        applyManaAndCooldown(caster);
        return true;
    }
}
