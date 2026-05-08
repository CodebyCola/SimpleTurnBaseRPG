/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.skill;
import kelompok11.turnbaserpg.enums.SkillType;
import kelompok11.turnbaserpg.model.Character;
import kelompok11.turnbaserpg.utils.GameConstants;
/**
 *
 * @author Pongo
 */
public class EarthCrusher extends Skill {
    
    public EarthCrusher() {
        super("Earth Crusher", "Smash enemy with earth power", 55, 30, 
                GameConstants.SKILL_COOLDOWN_HEAVY, SkillType.ATTACK
                );
    }
    
    public void use(Character caster, Character target) {
        dealDamage(caster, target);
    }
    
}
