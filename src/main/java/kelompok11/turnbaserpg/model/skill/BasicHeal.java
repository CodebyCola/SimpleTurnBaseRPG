package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.model.enums.SkillType;
import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

public class BasicHeal extends Skill {

    public BasicHeal() {
        super("Basic Heal", "Restore HP", 25, 20,
                GameConstants.SKILL_COOLDOWN_DEFAULT, SkillType.HEAL);
    }

    @Override
    public boolean cast(Character caster, Character target) {
        if (!canCast(caster)) return false;
        caster.heal(effectValue);
        applyManaAndCooldown(caster);
        return true;
    }
}
