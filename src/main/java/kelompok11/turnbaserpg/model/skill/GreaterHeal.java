package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.model.enums.SkillType;
import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

public class GreaterHeal extends Skill {

    public GreaterHeal() {
        super("Greater Heal", "Restore large amount of HP", 150, 35,
                GameConstants.SKILL_COOLDOWN_HEAVY, SkillType.HEAL);
    }

    @Override
    public boolean cast(Character caster, Character target) {
        if (!canCast(caster)) return false;
        caster.heal(effectValue);
        applyManaAndCooldown(caster);
        return true;
    }
}
