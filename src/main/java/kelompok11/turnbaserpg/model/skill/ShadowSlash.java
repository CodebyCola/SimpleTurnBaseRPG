/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.enums.SkillType;
import kelompok11.turnbaserpg.model.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

/**
 *
 * @author Pongo
 */
public class ShadowSlash extends Skill {

    public ShadowSlash() {
        super("Shadow Slash", "Perform fast shadow blade attack", 30, 10, 
                GameConstants.SKILL_COOLDOWN_DEFAULT, SkillType.ATTACK
        );
    }

    public void use(Character caster, Character target) {
        dealDamage(caster, target);
    }

}
