package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.model.enums.SkillType;
import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

public class EarthCrusher extends Skill {

    public EarthCrusher() {
        super("Earth Crusher", "Smash enemy with earth power", 55, 30,
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
