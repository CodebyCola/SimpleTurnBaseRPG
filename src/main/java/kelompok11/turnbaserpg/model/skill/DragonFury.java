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
public class DragonFury extends Skill {

    public DragonFury() {
        super("Dragon Fury", "Release destructive dragon power", 90, 40,
                GameConstants.SKILL_COOLDOWN_ULTIMATE, SkillType.ATTACK
        );
    }
    
    public void use(Character caster, Character target) {
        dealDamage(caster, target);
    }

}
