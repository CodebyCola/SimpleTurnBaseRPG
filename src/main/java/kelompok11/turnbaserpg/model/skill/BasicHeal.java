/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;
import kelompok11.turnbaserpg.enums.SkillType;

/**
 *
 * @author Pongo
 */
public class BasicHeal extends Skill {

    public BasicHeal() {
        super(
                "Basic Heal",
                "Restore HP",
                25,
                20,
                GameConstants.SKILL_COOLDOWN_DEFAULT,
                SkillType.HEAL
        );
    }

    @Override
    public boolean cast(Character caster, Character target) {

        if (currentCoolDown > 0) {
            System.out.println("Skill is on cooldown!");
            return false;
        }

        if (caster.getStats().getCurrentMana() < manaCost) {
            System.out.println("Not enough mana!");
            return false;
        }

        caster.heal(effectValue);
        caster.getStats().decreaseCurrentMana(manaCost);
        currentCoolDown = cooldown;
        System.out.println("player " + caster.getCharacterName() + " Casting " + this.name);
        System.out.println("Player heal " + effectValue + " HP");
        return true;
    }

}
