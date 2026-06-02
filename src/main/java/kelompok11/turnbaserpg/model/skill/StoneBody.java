package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.model.enums.SkillType;
import kelompok11.turnbaserpg.model.buff.DefenseBuff;
import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

public class StoneBody extends Skill {

    public StoneBody() {
        super("Stone Body", "Temporarily strengthen defense", 20, 10,
                GameConstants.SKILL_COOLDOWN_DEFAULT, SkillType.DEFEND);
    }

    @Override
    public boolean cast(Character caster, Character target) {
        if (!canCast(caster)) return false;
        caster.addBuff(new DefenseBuff(effectValue));
        applyManaAndCooldown(caster);
        return true;
    }
}
