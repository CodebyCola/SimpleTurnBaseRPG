/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.enums.SkillType;
import kelompok11.turnbaserpg.model.Character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

/**
 *
 * @author Pongo
 */
public class LifeDrain extends Skill {

    public LifeDrain() {

        super(
                "Life Drain",
                "Absorb enemy HP",
                35,
                20,
                GameConstants.SKILL_COOLDOWN_MEDIUM,
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
        target.takeDamage(effectValue);
        caster.getStats().decreaseCurrentMana(manaCost);
        currentCoolDown = cooldown;
        System.out.println("player " + caster.getCharacterName() + " Casting " + this.name);
        System.out.println("Player heal " + effectValue + " HP");
        return true;
    }

}
