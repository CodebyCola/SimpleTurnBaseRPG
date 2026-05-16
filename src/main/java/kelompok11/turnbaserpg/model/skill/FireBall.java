/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.enums.SkillType;
import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

/**
 *
 * @author Pongo
 */
public class FireBall extends Skill {

    public FireBall() {

        super("Fire Ball", "Cast Fire Ball to attack enemy", 15, 10,
                GameConstants.SKILL_COOLDOWN_MEDIUM, SkillType.ATTACK);
    }

    public boolean cast(Character caster, Character target) {

        if (currentCoolDown > 0) {
            System.out.println("Skill is on cooldown!");
            return false;
        }

        if (caster.getStats().getCurrentMana() < manaCost) {
            System.out.println("Not enough mana!");
            return false;
        }

        int totalDamage = effectValue + caster.getStats().getTotalMagic();
        target.takeDamage(totalDamage);
        caster.getStats().decreaseCurrentMana(manaCost);
        currentCoolDown = cooldown;
        System.out.println("player " + caster.getCharacterName() + " Casting " + this.name);
        System.out.println(target.getCharacterName() + " taking " + totalDamage + " damage");
        return true;
    }
}
