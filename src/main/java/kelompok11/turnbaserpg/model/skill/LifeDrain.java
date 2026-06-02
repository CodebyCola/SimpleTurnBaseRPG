package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.model.enums.SkillType;
import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

public class LifeDrain extends Skill {

    public LifeDrain() {
        super("Life Drain", "Absorb enemy HP", 35, 20,
                GameConstants.SKILL_COOLDOWN_MEDIUM, SkillType.HEAL);
    }

    @Override
    public boolean cast(Character caster, Character target) {
        if (!canCast(caster)) return false;
        caster.heal(effectValue);
        target.takeDamage(effectValue);
        applyManaAndCooldown(caster);
        return true;
    }
}
